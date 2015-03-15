package com.tsoft.myprocad.viewcontroller.component;

import com.tsoft.myprocad.model.DimensionLine;
import com.tsoft.myprocad.model.ItemPoints;
import com.tsoft.myprocad.model.Plan;
import com.tsoft.myprocad.model.Item;
import com.tsoft.myprocad.model.Wall;
import com.tsoft.myprocad.viewcontroller.PlanController;
import com.tsoft.myprocad.viewcontroller.state.ControllerState;
import com.tsoft.myprocad.viewcontroller.state.DimensionLineCreationState;
import com.tsoft.myprocad.viewcontroller.state.DimensionLineDrawingState;
import com.tsoft.myprocad.viewcontroller.state.DimensionLineOffsetState;
import com.tsoft.myprocad.viewcontroller.state.DimensionLineResizeState;

import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DimensionLineController {
    private Plan plan;
    private PlanController planController;

    private Area wallsAreaCache;
    private List<GeneralPath> roomPathsCache;

    public final ControllerState dimensionLineCreationState;
    public final ControllerState dimensionLineDrawingState;
    public final ControllerState dimensionLineResizeState;
    public final ControllerState dimensionLineOffsetState;

    public DimensionLineController(PlanController planController) {
        this.planController = planController;
        this.plan = planController.getPlan();

        dimensionLineCreationState = new DimensionLineCreationState(planController);
        dimensionLineDrawingState = new DimensionLineDrawingState(planController);
        dimensionLineResizeState = new DimensionLineResizeState(planController);
        dimensionLineOffsetState = new DimensionLineOffsetState(planController);
    }

    /**
     * Returns the dimension line that measures the side of a piece, the length of a room side
     * or the length of a wall side at (<code>x</code>, <code>y</code>) point,
     * or <code>null</code> if it doesn't exist.
     */
    public DimensionLine getMeasuringDimensionLineAt(float x, float y) {
        float margin = PlanController.PIXEL_MARGIN / planController.getScale();
        for (GeneralPath roomPath : getRoomPathsFromWalls()) {
            if (roomPath.intersects(x - margin, y - margin, 2 * margin, 2 * margin)) {
                DimensionLine dimensionLine = getDimensionLineBetweenPointsAt(getPathPoints(roomPath, true), x, y, margin);
                if (dimensionLine != null) {
                    return dimensionLine;
                }
            }
        }
        return null;
    }

    /**
     * Returns the dimension line that measures the side of the given polygon at (<code>x</code>, <code>y</code>) point,
     * or <code>null</code> if it doesn't exist.
     */
    private DimensionLine getDimensionLineBetweenPointsAt(float [][] points, float x, float y, float margin) {
        for (int i = 0; i < points.length; i++) {
            int nextPointIndex = (i + 1) % points.length;

            // Ignore sides with a length smaller than 0.1 cm
            double distanceBetweenPointsSq = Point2D.distanceSq(points[i][0], points[i][1], points[nextPointIndex][0], points[nextPointIndex][1]);
            double ptSegDistSq = Line2D.ptSegDistSq(points[i][0], points[i][1], points[nextPointIndex][0], points[nextPointIndex][1], x, y);

            if (distanceBetweenPointsSq > 0.01 && ptSegDistSq <= margin * margin) {
                double angle = Math.atan2(points [i][1] - points [nextPointIndex][1], points [nextPointIndex][0] - points [i][0]);
                boolean reverse = angle < -Math.PI / 2 || angle > Math.PI / 2;

                float xStart;
                float yStart;
                float xEnd;
                float yEnd;
                if (reverse) {
                    // Avoid reversed text on the dimension line
                    xStart = points [nextPointIndex][0];
                    yStart = points [nextPointIndex][1];
                    xEnd = points [i][0];
                    yEnd = points [i][1];
                } else {
                    xStart = points [i][0];
                    yStart = points [i][1];
                    xEnd = points [nextPointIndex][0];
                    yEnd = points [nextPointIndex][1];
                }

                DimensionLine dimensionLine = plan.createDimensionLine(xStart, xEnd, yStart, yEnd, 0);
                return dimensionLine;
            }
        }
        return null;
    }

    /**
     * Returns a new dimension instance joining (<code>xStart</code>,
     * <code>yStart</code>) and (<code>xEnd</code>, <code>yEnd</code>) points.
     * The new dimension line is added to home.
     */
    public DimensionLine createDimensionLine(float xStart, float yStart, float xEnd, float yEnd, int offset) {
        return plan.createDimensionLine(xStart, xEnd, yStart, yEnd, offset);
    }

    /**
     * Returns the selected dimension line with an end extension line
     * at (<code>x</code>, <code>y</code>).
     */
    public DimensionLine getResizedDimensionLineStartAt(float x, float y) {
        List<Item> selectedItems = planController.getSelectedItems();
        if (selectedItems.size() == 1 && selectedItems.get(0) instanceof DimensionLine) {
            DimensionLine dimensionLine = (DimensionLine)selectedItems.get(0);
            float margin = PlanController.INDICATOR_PIXEL_MARGIN / planController.getScale();
            if (dimensionLine.containsStartExtensionLinetAt(x, y, margin)) {
                return dimensionLine;
            }
        }
        return null;
    }

    /**
     * Returns the selected dimension line with an end extension line
     * at (<code>x</code>, <code>y</code>).
     */
    public DimensionLine getResizedDimensionLineEndAt(float x, float y) {
        List<Item> selectedItems = planController.getSelectedItems();
        if (selectedItems.size() == 1 && selectedItems.get(0) instanceof DimensionLine) {
            DimensionLine dimensionLine = (DimensionLine)selectedItems.get(0);
            float margin = PlanController.INDICATOR_PIXEL_MARGIN / planController.getScale();
            if (dimensionLine.containsEndExtensionLineAt(x, y, margin)) {
                return dimensionLine;
            }
        }
        return null;
    }

    /**
     * Returns the selected dimension line with a point
     * at (<code>x</code>, <code>y</code>) at its middle.
     */
    public DimensionLine getOffsetDimensionLineAt(float x, float y) {
        List<Item> selectedItems = planController.getSelectedItems();
        if (selectedItems.size() == 1 && selectedItems.get(0) instanceof DimensionLine) {
            DimensionLine dimensionLine = (DimensionLine)selectedItems.get(0);
            float margin = PlanController.INDICATOR_PIXEL_MARGIN / planController.getScale();
            if (dimensionLine.isMiddlePointAt(x, y, margin)) {
                return dimensionLine;
            }
        }
        return null;
    }

    /**
     * Moves <code>dimensionLine</code> start point to (<code>x</code>, <code>y</code>)
     * if <code>editingStartPoint</code> is true or <code>dimensionLine</code> end point
     * to (<code>x</code>, <code>y</code>) if <code>editingStartPoint</code> is false.
     */
    public void moveDimensionLinePoint(DimensionLine dimensionLine, float x, float y, boolean startPoint) {
        if (startPoint) {
            dimensionLine.setXStart(x);
            dimensionLine.setYStart(y);
        } else {
            dimensionLine.setXEnd(x);
            dimensionLine.setYEnd(y);
        }
    }

    /**
     * Returns the list of closed paths that may define rooms from
     * the current set of home walls.
     */
    private List<GeneralPath> getRoomPathsFromWalls() {
        if (roomPathsCache == null) {
            // Iterate over all the paths the walls area contains
            Area wallsArea = getWallsArea();
            List<GeneralPath> roomPaths = getAreaPaths(wallsArea);
            Area insideWallsArea = new Area(wallsArea);
            for (GeneralPath roomPath : roomPaths) {
                insideWallsArea.add(new Area(roomPath));
            }

            roomPathsCache = roomPaths;
        }
        return roomPathsCache;
    }

    /**
     * Returns the area covered by walls.
     */
    private Area getWallsArea() {
        if (wallsAreaCache == null) {
            wallsAreaCache = plan.getLevelWalls().getItemsArea();
        }
        return wallsAreaCache;
    }

    /**
     * Returns the paths described by the given <code>area</code>.
     */
    private List<GeneralPath> getAreaPaths(Area area) {
        List<GeneralPath> roomPaths = new ArrayList<>();
        GeneralPath roomPath = new GeneralPath();
        for (PathIterator it = area.getPathIterator(null, 0.5f); !it.isDone(); ) {
            float [] roomPoint = new float[2];
            switch (it.currentSegment(roomPoint)) {
                case PathIterator.SEG_MOVETO :
                    roomPath.moveTo(roomPoint [0], roomPoint [1]);
                    break;
                case PathIterator.SEG_LINETO :
                    roomPath.lineTo(roomPoint [0], roomPoint [1]);
                    break;
                case PathIterator.SEG_CLOSE :
                    roomPath.closePath();
                    roomPaths.add(roomPath);
                    roomPath = new GeneralPath();
                    break;
            }
            it.next();
        }
        return roomPaths;
    }

    /**
     * Returns the points of a general path which contains only one path.
     */
    private float[][] getPathPoints(GeneralPath path, boolean removeAlignedPoints) {
        List<float []> pathPoints = new ArrayList<>();
        float [] previousPathPoint = null;
        for (PathIterator it = path.getPathIterator(null); !it.isDone(); ) {
            float [] pathPoint = new float[2];
            if (it.currentSegment(pathPoint) != PathIterator.SEG_CLOSE
                    && (previousPathPoint == null
                    || !Arrays.equals(pathPoint, previousPathPoint))) {
                boolean replacePoint = false;
                if (removeAlignedPoints && pathPoints.size() > 1) {
                    // Check if pathPoint is aligned with the last line added to pathPoints
                    float [] lastLineStartPoint = pathPoints.get(pathPoints.size() - 2);
                    float [] lastLineEndPoint = previousPathPoint;
                    replacePoint = Line2D.ptLineDistSq(lastLineStartPoint [0], lastLineStartPoint [1],
                            lastLineEndPoint [0], lastLineEndPoint [1],
                            pathPoint [0], pathPoint [1]) < 0.0001;
                }
                if (replacePoint) {
                    pathPoints.set(pathPoints.size() - 1, pathPoint);
                } else {
                    pathPoints.add(pathPoint);
                }
                previousPathPoint = pathPoint;
            }
            it.next();
        }

        // Remove last point if it's equal to first point
        if (pathPoints.size() > 1 && Arrays.equals(pathPoints.get(0), pathPoints.get(pathPoints.size() - 1))) {
            pathPoints.remove(pathPoints.size() - 1);
        }

        return pathPoints.toArray(new float [pathPoints.size()][]);
    }
}
