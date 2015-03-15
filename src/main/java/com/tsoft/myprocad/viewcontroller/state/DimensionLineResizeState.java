package com.tsoft.myprocad.viewcontroller.state;

import com.tsoft.myprocad.model.DimensionLine;
import com.tsoft.myprocad.viewcontroller.PlanController;
import com.tsoft.myprocad.viewcontroller.PlanController.Mode;

import java.awt.geom.Point2D;

public class DimensionLineResizeState extends ControllerState {
    private DimensionLine selectedDimensionLine;
    private DimensionLine historyDimensionLine;

    private boolean editingStartPoint;
    private float oldX;
    private float oldY;
    private float deltaXToResizePoint;
    private float deltaYToResizePoint;
    private float distanceFromResizePointToDimensionBaseLine;

    public DimensionLineResizeState(PlanController planController) {
        super(planController);
    }

    @Override
    public Mode getMode() {
        return Mode.SELECTION;
    }

    @Override
    public void enter() {
        planPanel.setResizeIndicatorVisible(true);

        selectedDimensionLine = (DimensionLine)planController.getSelectedItems().get(0);
        historyDimensionLine = (DimensionLine)selectedDimensionLine.historyClone();

        editingStartPoint = (selectedDimensionLine
                == getDimensionLineController().getResizedDimensionLineStartAt(getXLastMousePress(), getYLastMousePress()));
        if (editingStartPoint) {
            oldX = selectedDimensionLine.getXStart();
            oldY = selectedDimensionLine.getYStart();
        } else {
            oldX = selectedDimensionLine.getXEnd();
            oldY = selectedDimensionLine.getYEnd();
        }

        float xResizePoint;
        float yResizePoint;

        // Compute the closest resize point placed on the extension line and the distance
        // between that point and the dimension line base
        float alpha1 = (float)(selectedDimensionLine.getYEnd() - selectedDimensionLine.getYStart())
                / (selectedDimensionLine.getXEnd() - selectedDimensionLine.getXStart());

        // If line is vertical
        if (Math.abs(alpha1) > 1E5) {
            xResizePoint = getXLastMousePress();
            if (this.editingStartPoint) {
                yResizePoint = selectedDimensionLine.getYStart();
            } else {
                yResizePoint = selectedDimensionLine.getYEnd();
            }
        } else if (selectedDimensionLine.getYDistance() == 0) {
            if (this.editingStartPoint) {
                xResizePoint = selectedDimensionLine.getXStart();
            } else {
                xResizePoint = selectedDimensionLine.getXEnd();
            }
            yResizePoint = getYLastMousePress();
        } else {
            float beta1 = getYLastMousePress() - alpha1 * getXLastMousePress();
            float alpha2 = -1 / alpha1;
            float beta2;

            if (this.editingStartPoint) {
                beta2 = this.selectedDimensionLine.getYStart() - alpha2 * selectedDimensionLine.getXStart();
            } else {
                beta2 = this.selectedDimensionLine.getYEnd() - alpha2 * selectedDimensionLine.getXEnd();
            }
            xResizePoint = (beta2 - beta1) / (alpha1 - alpha2);
            yResizePoint = alpha1 * xResizePoint + beta1;
        }

        this.deltaXToResizePoint = getXLastMousePress() - xResizePoint;
        this.deltaYToResizePoint = getYLastMousePress() - yResizePoint;
        if (this.editingStartPoint) {
            this.distanceFromResizePointToDimensionBaseLine = (float) Point2D.distance(xResizePoint, yResizePoint,
                    this.selectedDimensionLine.getXStart(), selectedDimensionLine.getYStart());
        } else {
            this.distanceFromResizePointToDimensionBaseLine = (float)Point2D.distance(xResizePoint, yResizePoint,
                    this.selectedDimensionLine.getXEnd(), selectedDimensionLine.getYEnd());
        }
    }

    @Override
    public void moveMouse(float x, float y) {
        float xResizePoint = x - deltaXToResizePoint;
        float yResizePoint = y - deltaYToResizePoint;

        if (editingStartPoint) {
            // Compute the new start point of the dimension line knowing that the distance
            // from resize point to dimension line base is constant,
            // and that the end point of the dimension line doesn't move
            double distanceFromResizePointToDimensionLineEnd = Point2D.distance(xResizePoint, yResizePoint,
                    selectedDimensionLine.getXEnd(), selectedDimensionLine.getYEnd());
            double distanceFromDimensionLineStartToDimensionLineEnd = Math.sqrt(
                    distanceFromResizePointToDimensionLineEnd * distanceFromResizePointToDimensionLineEnd
                            - distanceFromResizePointToDimensionBaseLine * distanceFromResizePointToDimensionBaseLine);
            if (distanceFromDimensionLineStartToDimensionLineEnd > 0) {
                double dimensionLineRelativeAngle = -Math.atan2(distanceFromResizePointToDimensionBaseLine,
                        distanceFromDimensionLineStartToDimensionLineEnd);
                if (selectedDimensionLine.getOffset() >= 0) {
                    dimensionLineRelativeAngle = -dimensionLineRelativeAngle;
                }

                double resizePointToDimensionLineEndAngle = Math.atan2(yResizePoint - selectedDimensionLine.getYEnd(),
                        xResizePoint - selectedDimensionLine.getXEnd());
                double dimensionLineStartToDimensionLineEndAngle = dimensionLineRelativeAngle + resizePointToDimensionLineEndAngle;
                float xNewStartPoint = selectedDimensionLine.getXEnd() + (float)(distanceFromDimensionLineStartToDimensionLineEnd
                        * Math.cos(dimensionLineStartToDimensionLineEndAngle));
                float yNewStartPoint = selectedDimensionLine.getYEnd() + (float)(distanceFromDimensionLineStartToDimensionLineEnd
                        * Math.sin(dimensionLineStartToDimensionLineEndAngle));

                getDimensionLineController().moveDimensionLinePoint(selectedDimensionLine, xNewStartPoint, yNewStartPoint, editingStartPoint);
            }
        } else {
            // Compute the new end point of the dimension line knowing that the distance
            // from resize point to dimension line base is constant,
            // and that the start point of the dimension line doesn't move
            double distanceFromResizePointToDimensionLineStart = Point2D.distance(xResizePoint, yResizePoint,
                    selectedDimensionLine.getXStart(), selectedDimensionLine.getYStart());
            double distanceFromDimensionLineStartToDimensionLineEnd = Math.sqrt(
                    distanceFromResizePointToDimensionLineStart * distanceFromResizePointToDimensionLineStart
                            - distanceFromResizePointToDimensionBaseLine * distanceFromResizePointToDimensionBaseLine);
            if (distanceFromDimensionLineStartToDimensionLineEnd > 0) {
                double dimensionLineRelativeAngle = Math.atan2(distanceFromResizePointToDimensionBaseLine,
                        distanceFromDimensionLineStartToDimensionLineEnd);
                if (selectedDimensionLine.getOffset() >= 0) {
                    dimensionLineRelativeAngle = -dimensionLineRelativeAngle;
                }
                double resizePointToDimensionLineStartAngle = Math.atan2(yResizePoint - selectedDimensionLine.getYStart(),
                        xResizePoint - selectedDimensionLine.getXStart());
                double dimensionLineStartToDimensionLineEndAngle = dimensionLineRelativeAngle + resizePointToDimensionLineStartAngle;
                float xNewEndPoint = selectedDimensionLine.getXStart() + (float)(distanceFromDimensionLineStartToDimensionLineEnd
                        * Math.cos(dimensionLineStartToDimensionLineEndAngle));
                float yNewEndPoint = selectedDimensionLine.getYStart() + (float)(distanceFromDimensionLineStartToDimensionLineEnd
                        * Math.sin(dimensionLineStartToDimensionLineEndAngle));

                getDimensionLineController().moveDimensionLinePoint(selectedDimensionLine, xNewEndPoint, yNewEndPoint, editingStartPoint);
            }
        }

        // Ensure point at (x,y) is visible
        planPanel.makePointVisible(x, y);
    }

    @Override
    public void releaseMouse(float x, float y) {
        setState(getSelectionState());
        history.push(historyDimensionLine);
    }

    @Override
    public void escape() {
        getDimensionLineController().moveDimensionLinePoint(selectedDimensionLine, oldX, oldY, editingStartPoint);
        setState(getSelectionState());
    }

    @Override
    public void exit() {
        planPanel.setResizeIndicatorVisible(false);
        planPanel.deleteFeedback();
        selectedDimensionLine = null;
    }
}
