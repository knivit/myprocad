package com.tsoft.myprocad.swing;

import com.tsoft.myprocad.model.Item;
import com.tsoft.myprocad.model.ItemList;
import com.tsoft.myprocad.model.PageSetup;
import com.tsoft.myprocad.model.Plan;
import com.tsoft.myprocad.swing.component.BeamComponent;
import com.tsoft.myprocad.swing.component.DimensionLineComponent;
import com.tsoft.myprocad.swing.component.LabelComponent;
import com.tsoft.myprocad.swing.component.LevelMarkComponent;
import com.tsoft.myprocad.swing.component.PlanGridComponent;
import com.tsoft.myprocad.swing.component.PlanRulerComponent;
import com.tsoft.myprocad.swing.component.WallComponent;
import com.tsoft.myprocad.swing.menu.PlanPanelMenu;
import com.tsoft.myprocad.swing.properties.PropertiesManagerPanel;

import com.tsoft.myprocad.util.LengthUnitUtil;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.io.InterruptedIOException;
import java.util.concurrent.Callable;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JViewport;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.MouseInputAdapter;

import com.tsoft.myprocad.model.property.ListenedField;
import com.tsoft.myprocad.model.property.PlanProperties;

import com.tsoft.myprocad.util.OperatingSystem;
import com.tsoft.myprocad.util.PrintDialog;
import com.tsoft.myprocad.util.SwingTools;
import com.tsoft.myprocad.util.printer.PrinterUtil;
import com.tsoft.myprocad.util.script.JavaScriptPanel;
import com.tsoft.myprocad.viewcontroller.PlanController;

public class PlanPanel extends JComponent implements Scrollable, Printable {
    public enum PaintMode { PAINT, PRINT }

    public static final float MARGIN = 400; // mm

    private PlanController planController;
    private Plan plan;

    private float scale;

    private JScrollPane scrollPane;
    private JSplitPane splitPane;
    private JComponent parentComponent;
    public PlanRulerComponent horizontalRuler;
    public PlanRulerComponent verticalRuler;

    private boolean selectionScrollUpdated;
    private boolean resizeIndicatorVisible;

    private Rectangle2D rectangleFeedback;
    private Rectangle2D planBoundsCache;
    private boolean planBoundsCacheValid = false;

    private WallComponent wallComponent;
    private BeamComponent beamComponent;
    private DimensionLineComponent dimensionLineComponent;
    private LabelComponent labelComponent;
    private LevelMarkComponent levelMarkComponent;
    private PlanGridComponent gridComponent;
    private boolean revalidateDisabled;
    private JavaScriptPanel commandPanel;

    public static PlanPanel createPlanPanel(PlanController planController) {
        PlanPanel planPanel = new PlanPanel(planController);
        return planPanel;
    }

    private PlanPanel(PlanController planController) {
        this.plan = planController.getPlan();
        this.planController = planController;

        setOpaque(true);

        scrollPane = SwingTools.createScrollPane(this);
        commandPanel = null;

        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollPane, commandPanel);
        splitPane.setFocusable(false);
        splitPane.setContinuousLayout(true);
        splitPane.setOneTouchExpandable(true);
        splitPane.setResizeWeight(0.8);
        splitPane.setBorder(null);
    }

    public void toggleCommandWindow() {
        if (splitPane.getRightComponent() == null) {
            if (commandPanel == null) commandPanel = new JavaScriptPanel(plan, this);
            splitPane.setRightComponent(commandPanel);
            splitPane.setDividerLocation(0.7);
        } else splitPane.setRightComponent(null);
    }

    public void hideCommandWindow() {
        splitPane.setRightComponent(null);
    }

    public void afterOpen(PropertiesManagerPanel propertiesManagerPanel) {
        parentComponent = propertiesManagerPanel.linkTo(splitPane);

        createSubComponents();
        initPanelComponent();

        // set state according to the plan
        setRulersVisible(plan.isRulersVisible());
    }

    public JComponent getParentComponent() { return parentComponent; }

    private void createSubComponents() {
        wallComponent = new WallComponent(plan);
        beamComponent = new BeamComponent(plan);
        dimensionLineComponent = new DimensionLineComponent(this);
        labelComponent = new LabelComponent(this);
        levelMarkComponent = new LevelMarkComponent(this);
        gridComponent = new PlanGridComponent(this);
    }

    private void initPanelComponent() {
        PlanPanelMenu.createPopupMenu(this);

        // Add listeners
        addMouseListeners(planController);
        addFocusListener(planController);

        PlanPanelMenu.installDefaultKeyboardActions(this);
        setFocusable(true);
        setAutoscrolls(true);

        // Install default colors using same colors as a text field
        super.setForeground(UIManager.getColor("TextField.foreground"));
        super.setBackground(UIManager.getColor("TextField.background"));

        horizontalRuler = new PlanRulerComponent(this, SwingConstants.HORIZONTAL);
        verticalRuler = new PlanRulerComponent(this, SwingConstants.VERTICAL);
    }

    public Plan getPlan() {
        return plan;
    }

    public void selectionChanged() {
        repaint();
    }

    public void planChanged(ListenedField property) {
        if (PlanProperties.RULERS_VISIBLE.equals(property)) {
            setRulersVisible(plan.isRulersVisible());
            return;
        }

        if (PlanProperties.GRID_VISIBLE.equals(property)) {
            repaint();
            return;
        }

        if (PlanProperties.LEVEL.equals(property)) {
            plan.getWalls().resetItemCaches();
            plan.getDimensionLines().resetItemCaches();
            plan.getLabels().resetItemCaches();
            plan.getLevelMarks().resetItemCaches();

            revalidate();
            return;
        }
    }

    public void itemChanged(Item item) { revalidate(); }

    public void itemListChanged() { revalidate(); }

    public void disableRevalidate() {
        revalidateDisabled = true;
    }

    public void enableRevalidate() {
        revalidateDisabled = false;
        revalidate();
    }

    @Override
    public void revalidate() {
        revalidate(true);
    }

    /**
     * Revalidates this component after invalidating plan bounds cache if <code>invalidatePlanBoundsCache</code> is <code>true</code>
     * and updates viewport position if this component is displayed in a scrolled pane.
     */
    private void revalidate(boolean invalidatePlanBoundsCache) {
        if (revalidateDisabled) return;

        boolean planBoundsCacheWereValid = planBoundsCacheValid;
        final float planBoundsMinX = (float)getPlanBounds().getMinX();
        final float planBoundsMinY = (float)getPlanBounds().getMinY();
        if (invalidatePlanBoundsCache && planBoundsCacheWereValid) {
            planBoundsCacheValid = false;
        }

        // Revalidate and repaint
        super.revalidate();
        repaint();

        if (invalidatePlanBoundsCache && getParent() instanceof JViewport) {
            float planBoundsNewMinX = (float)getPlanBounds().getMinX();
            float planBoundsNewMinY = (float)getPlanBounds().getMinY();

            // If plan bounds upper left corner diminished
            if (planBoundsNewMinX < planBoundsMinX || planBoundsNewMinY < planBoundsMinY) {
                JViewport parent = (JViewport)getParent();
                Point viewPosition = parent.getViewPosition();
                Dimension extentSize = parent.getExtentSize();
                Dimension viewSize = parent.getViewSize();

                // Update view position when scroll bars are visible
                if (extentSize.width < viewSize.width || extentSize.height < viewSize.height) {
                    int deltaX = Math.round((planBoundsMinX - planBoundsNewMinX) * plan.getScale());
                    int deltaY = Math.round((planBoundsMinY - planBoundsNewMinY) * plan.getScale());
                    parent.setViewPosition(new Point(viewPosition.x + deltaX, viewPosition.y + deltaY));
                }
            }
        }

        revalidateRulers();
        repaintRulers();
    }

    private void repaintRulers() {
        horizontalRuler.repaint();
        verticalRuler.repaint();
    }

    private void revalidateRulers() {
        horizontalRuler.revalidate();
        verticalRuler.revalidate();
    }

    private void addMouseListeners(final PlanController controller) {
        MouseInputAdapter mouseListener = new MouseInputAdapter() {
            private int lastPressedX;
            private int lastPressedY;
            private long pressedTimeMillis;

            @Override
            public void mousePressed(MouseEvent ev) {
                lastPressedX = ev.getX();
                lastPressedY = ev.getY();
                pressedTimeMillis = System.currentTimeMillis();

                if (isEnabled() && !ev.isPopupTrigger()) {
                    requestFocusInWindow();
                    if (ev.getButton() == MouseEvent.BUTTON1) {
                        boolean duplicationActivated = OperatingSystem.isMacOSX() ? ev.isAltDown() : ev.isControlDown();
                        controller.pressMouse(convertXPixelToModel(ev.getX()), convertYPixelToModel(ev.getY()),
                                ev.getClickCount(), ev.isShiftDown() && !ev.isControlDown() && !ev.isAltDown() && !ev.isMetaDown(),
                                duplicationActivated);
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent ev) {
                lastPressedX = -1;
                if (isEnabled() && !ev.isPopupTrigger() && ev.getButton() == MouseEvent.BUTTON1) {
                    float x = convertXPixelToModel(ev.getX());
                    float y = convertYPixelToModel(ev.getY());
                    controller.releaseMouse(x, y);
                }
            }

            @Override
            public void mouseMoved(MouseEvent ev) {
                // prevent from unwanted movements on a click
                // let's move an item after 0.1 sec after it was clicked on
                if ((System.currentTimeMillis() - pressedTimeMillis) < 100) return;

                float x = convertXPixelToModel(ev.getX());
                float y = convertYPixelToModel(ev.getY());
                controller.moveMouse(x, y);
            }

            @Override
            public void mouseDragged(MouseEvent ev) {
                if (isEnabled()) {
                    mouseMoved(ev);
                }
            }
        };

        addMouseListener(mouseListener);
        addMouseMotionListener(mouseListener);
        addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent ev) {
                if (ev.getModifiers() == getToolkit().getMenuShortcutKeyMask()) {
                    float mouseX = 0;
                    float mouseY = 0;
                    int deltaX = 0;
                    int deltaY = 0;
                    if (getParent() instanceof JViewport) {
                        mouseX = convertXPixelToModel(ev.getX());
                        mouseY = convertYPixelToModel(ev.getY());
                        Rectangle viewRectangle = ((JViewport) getParent()).getViewRect();
                        deltaX = ev.getX() - viewRectangle.x;
                        deltaY = ev.getY() - viewRectangle.y;
                    }

                    float oldScale = plan.getScale();
                    controller.zoom((float) (ev.getWheelRotation() < 0 ? Math.pow(1.05, -ev.getWheelRotation()) : Math.pow(0.95, ev.getWheelRotation())));

                    if (plan.getScale() != oldScale && getParent() instanceof JViewport) {
                        // If scale changed, update viewport position to keep the same coordinates under mouse cursor
                        ((JViewport) getParent()).setViewPosition(new Point());
                        moveView(mouseX - convertXPixelToModel(deltaX), mouseY - convertYPixelToModel(deltaY));
                    }
                } else if (getMouseWheelListeners().length == 1) {
                    // If this listener is the only one registered on this component
                    // redispatch event to its parent (for default scroll bar management)
                    getParent().dispatchEvent(
                            new MouseWheelEvent(getParent(), ev.getID(), ev.getWhen(),
                                    ev.getModifiersEx() | ev.getModifiers(),
                                    ev.getX() - getX(), ev.getY() - getY(),
                                    ev.getClickCount(), ev.isPopupTrigger(), ev.getScrollType(),
                                    ev.getScrollAmount(), ev.getWheelRotation())
                    );
                }
            }
        });
    }

    private void addFocusListener(final PlanController controller) {
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent ev) {
                controller.escape();
            }
        });
    }

    @Override
    public Dimension getPreferredSize() {
        if (isPreferredSizeSet()) {
            return super.getPreferredSize();
        }

        Insets insets = getInsets();
        Rectangle2D planBounds = getPlanBounds();
        int dx = Math.round(((float)planBounds.getWidth() + MARGIN * 2) * plan.getScale()) + insets.left + insets.right;
        int dy = Math.round(((float)planBounds.getHeight() + MARGIN * 2) * plan.getScale()) + insets.top + insets.bottom;
        return new Dimension(dx, dy);
    }

    public Rectangle2D getPlanBounds() {
        if (planBoundsCacheValid) {
            return planBoundsCache;
        }

        // Always enlarge plan bounds only when plan component is a child of a scroll pane
        if (planBoundsCache == null || !(getParent() instanceof JViewport)) {
            // Ensure plan bounds are 10 x 10 meters wide at minimum
            planBoundsCache = new Rectangle2D.Float(0, 0, 1000, 1000);
        }

        Rectangle2D itemsBounds = getItemsBounds(getGraphics(), getPaintedItems());
        if (itemsBounds != null) {
            planBoundsCache.add(itemsBounds);
        }

        planBoundsCacheValid = true;
        return planBoundsCache;
    }

    /**
     * Returns the collection of walls, labels and dimension lines of the plan
     * painted by this component wherever the level they belong to is selected or not.
     */
    public ItemList<Item> getPaintedItems() {
        return plan.getLevelItems();
    }

    /**
     * Returns the bounds of the given collection of <code>items</code>.
     */
    public Rectangle2D getItemsBounds(Graphics g, ItemList<Item> items) {
        Rectangle2D itemsBounds = null;
        for (Item item : items) {
            if (itemsBounds == null) {
                itemsBounds = getItemBounds(g, item);
            } else {
                Rectangle2D nextBounds = getItemBounds(g, item);
                if (nextBounds != null) {
                    itemsBounds.add(nextBounds);
                }
            }
        }
        return itemsBounds;
    }

    /**
     * Returns the bounds of the given <code>item</code>.
     */
    protected Rectangle2D getItemBounds(Graphics g, Item item) {
        return item.getShape().getBounds2D();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2D = (Graphics2D)g.create();
        paintBackground(g2D, getBackgroundColor(PaintMode.PAINT));

        // Clip component to avoid drawing in empty borders
        Insets insets = getInsets();
        g2D.clipRect(insets.left, insets.top, getWidth() - insets.left - insets.right, getHeight() - insets.top - insets.bottom);

        // Change component coordinates system to plan system
        Rectangle2D planBounds = getPlanBounds();
        float paintScale = plan.getScale();
        g2D.translate(insets.left + (MARGIN - planBounds.getMinX()) * paintScale, insets.top + (MARGIN - planBounds.getMinY()) * paintScale);
        g2D.scale(paintScale, paintScale);
        setRenderingHints(g2D);
        //g2D.rotate(Math.toRadians(90));

        try {
            if (plan.isGridVisible()) gridComponent.paintGrid(g2D, paintScale);
            paintItems(g2D, paintScale, PaintMode.PAINT);
        } catch (InterruptedIOException ex) {
            // Ignore exception because it may happen only in EXPORT paint mode 
        }
        g2D.dispose();
    }

    /**
     * Returns the margin that should be added around plan items bounds to ensure their
     * line stroke width is always fully visible.
     */
    private float getStrokeWidthExtraMargin(ItemList items, PaintMode paintMode) {
        return 1f / 2;
    }

    /**
     * Prints this component plan at the scale given in the home print attributes or at a scale 
     * that makes it fill <code>pageFormat</code> imageable size if this attribute is <code>null</code>.
     */
    @Override
    public int print(Graphics g, PageFormat pageFormat, int pageIndex) {
        ItemList<Item> printedItems = getPaintedItems();
        Rectangle2D bounds = getItemsBounds(g, printedItems);
        if (bounds == null) return NO_SUCH_PAGE;

        // add margins to print rulers
        float w = (float)bounds.getWidth() + 4*100f;
        float h = (float)bounds.getHeight() + 3*100f;
        Rectangle2D printedItemBounds = new Rectangle2D.Float((float)bounds.getMinX() - 2*100f, (float)bounds.getMinY() - 1.5f*100f, w, h);

        double imageableX = pageFormat.getImageableX();
        double imageableY = pageFormat.getImageableY();
        double imageableWidth = pageFormat.getImageableWidth();
        double imageableHeight = pageFormat.getImageableHeight();
        float printScale;
        float rowIndex;
        float columnIndex;
        int pagesPerRow;
        int pagesPerColumn;

        // all on the one page
        if (plan.getPageSetup().getPrintScale().equals(PageSetup.PrintScale.ONE_PAGE)) {
            if (pageIndex > 0) return NO_SUCH_PAGE;

            // Compute a scale that ensures the plan will fill the component
            float imageableWidthCm = LengthUnitUtil.inchToCentimeter((float) pageFormat.getImageableWidth() / 72);
            float imageableHeightCm = LengthUnitUtil.inchToCentimeter((float)pageFormat.getImageableHeight() / 72);
            float extraMargin = getStrokeWidthExtraMargin(printedItems, PaintMode.PRINT);

            // Compute the largest integer scale possible
            double pw = (printedItemBounds.getWidth() + 20f * extraMargin);
            double ph = (printedItemBounds.getHeight() + 20f * extraMargin);
            int scaleInverse = (int)Math.ceil(Math.max(pw / imageableWidthCm, ph / imageableHeightCm));
            printScale = 1f / scaleInverse * LengthUnitUtil.centimeterToInch(72);

            pagesPerRow = 1;
            pagesPerColumn = 1;
            rowIndex = 0;
            columnIndex = 0;
        } else {
            // Apply print scale to paper size expressed in 1/72nds of an inch
            printScale = plan.getPageSetup().getPrintScaleValue() * LengthUnitUtil.centimeterToInch(72);
            pagesPerRow = (int)(printedItemBounds.getWidth() * printScale / imageableWidth);
            if (printedItemBounds.getWidth() * printScale != imageableWidth) {
                pagesPerRow ++;
            }

            pagesPerColumn = (int)(printedItemBounds.getHeight() * printScale / imageableHeight);
            if (printedItemBounds.getHeight() * printScale != imageableHeight) {
                pagesPerColumn ++;
            }

            if (pageIndex >= pagesPerRow * pagesPerColumn) {
                return NO_SUCH_PAGE;
            }

            rowIndex = pageIndex / pagesPerRow;
            columnIndex = pageIndex - rowIndex * pagesPerRow;
        }

        Graphics2D g2D = (Graphics2D)g.create();
        g2D.clip(new Rectangle2D.Double(imageableX, imageableY, imageableWidth, imageableHeight));

        // Change coordinates system to paper imageable origin
        g2D.translate(imageableX - columnIndex * imageableWidth, imageableY - rowIndex * imageableHeight);
        g2D.scale(printScale, printScale);
        float extraMargin = getStrokeWidthExtraMargin(printedItems, PaintMode.PRINT);
        g2D.translate(-printedItemBounds.getMinX() + extraMargin, -printedItemBounds.getMinY() + extraMargin);

        // Center plan in component if possible
        g2D.translate(Math.max(0,
                (imageableWidth * pagesPerRow / printScale - printedItemBounds.getWidth() - 2 * extraMargin) / 2),
                Math.max(0, (imageableHeight * pagesPerColumn / printScale - printedItemBounds.getHeight() - 2 * extraMargin) / 2));
        setRenderingHints(g2D);

        try {
            gridComponent.print(g2D, printScale, printedItemBounds, plan.getPageSetup().isGridPrinted(), plan.getPageSetup().isRulersPrinted());

            paintItems(g2D, printScale, PaintMode.PRINT);
        } catch (InterruptedIOException ex) {
            // Ignore exception because it may happen only in EXPORT paint mode
        }
        g2D.dispose();
        return PAGE_EXISTS;
    }

    private SelectionPaintInfo getSelectionPaintInfo(float scale, PaintMode paintMode) {
        SelectionPaintInfo info = new SelectionPaintInfo(scale, paintMode);
        info.scale = scale;
        info.paintMode = paintMode;
        Color selectionColor = UIManager.getColor("textHighlight").darker();
        info.selectionColor = selectionColor;
        info.selectionOutlinePaint = new Color(selectionColor.getRed(), selectionColor.getGreen(), selectionColor.getBlue(), 128);
        info.selectionOutlineStroke = new BasicStroke(6 / scale, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        info.dimensionLinesSelectionOutlineStroke = new BasicStroke(4 / scale, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        info.locationFeedbackStroke = new BasicStroke(
                1 / scale, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL, 0,
                new float [] {20 / scale, 5 / scale, 5 / scale, 5 / scale}, 4 / scale);
        info.backgroundColor = getBackgroundColor(paintMode);
        info.foregroundColor = getForegroundColor(paintMode);

        return info;
    }

    /**
     * Paints items at the given scale, and with background and foreground colors.
     * Outline around selected items will be painted only under <code>PAINT</code> mode.
     */
    private void paintItems(Graphics g, float scale, PaintMode paintMode) throws InterruptedIOException {
        ItemList<Item> selectedItems = planController.getSelectedItems();
        SelectionPaintInfo selectionPaintInfo = getSelectionPaintInfo(scale, paintMode);

        Graphics2D g2D = (Graphics2D)g;
        wallComponent.paintWalls(g2D, selectionPaintInfo);
        beamComponent.paintBeams(g2D, selectionPaintInfo);
        dimensionLineComponent.paintDimensionLines(g2D, plan.getLevelDimensionLines(), selectedItems, selectionPaintInfo);
        labelComponent.paintLabels(g2D, selectedItems, selectionPaintInfo);
        levelMarkComponent.paintLevelMarks(g2D, plan.getLevelLevelMarks(), selectedItems, selectionPaintInfo);

        if (paintMode == PaintMode.PAINT) {
            paintRectangleFeedback(g2D, getSelectionColor(), scale);
        }
    }

    /**
     * Sets rendering hints used to paint plan.
     */
    private void setRenderingHints(Graphics2D g2D) {
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2D.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
    }

    private void paintBackground(Graphics2D g2D, Color backgroundColor) {
        if (isOpaque()) {
            g2D.setColor(backgroundColor);
            g2D.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    private Color getForegroundColor(PaintMode mode) {
        if (mode == PaintMode.PAINT) {
            return getForeground();
        }
        return Color.BLACK;
    }

    private Color getBackgroundColor(PaintMode mode) {
        if (mode == PaintMode.PAINT) {
            return getBackground();
        }
        return Color.WHITE;
    }

    /**
     * Returns the color used to draw selection outlines. 
     */
    public Color getSelectionColor() {
        if (OperatingSystem.isMacOSX()) {
            if (OperatingSystem.isMacOSXLeopardOrSuperior()) {
                Window window = SwingUtilities.getWindowAncestor(this);
                if (window != null && !window.isActive()) {
                    Color selectionColor = UIManager.getColor("List.selectionInactiveBackground");
                    if (selectionColor != null) {
                        return selectionColor.darker();
                    }
                }
                return UIManager.getColor("List.selectionBackground");
            }
            return UIManager.getColor("textHighlight");
        }

        // On systems different from Mac OS X, take a darker color
        return UIManager.getColor("textHighlight").darker();
    }

    /**
     * Ensures selected items are visible at screen and moves
     * scroll bars if needed.
     */
    public void makeSelectionVisible() {
        // As multiple selections may happen during an action, 
        // make the selection visible the latest possible to avoid multiple changes
        if (!selectionScrollUpdated) {
            selectionScrollUpdated = true;

            EventQueue.invokeLater(() -> {
                selectionScrollUpdated = false;
                Rectangle2D selectionBounds = getSelectionBounds();
                if (selectionBounds != null) {
                    Rectangle pixelBounds = getShapePixelBounds(selectionBounds);
                    pixelBounds.grow(5, 5);
                    scrollRectToVisible(pixelBounds);
                }
            });
        }
    }

    /**
     * Returns the bounds of the selected items.
     */
    public Rectangle2D getSelectionBounds() {
        return getItemsBounds(getGraphics(), planController.getSelectedItems());
    }

    /**
     * Ensures the point at (<code>x</code>, <code>y</code>) is visible,
     * moving scroll bars if needed.
     */
    public void makePointVisible(float x, float y) {
        scrollRectToVisible(getShapePixelBounds(new Rectangle2D.Float(x, y, 1 / plan.getScale(), 1 / plan.getScale())));
    }

    public void setRectangleFeedback(float x0, float y0, float x1, float y1) {
        rectangleFeedback = new Rectangle2D.Float(x0, y0, 0, 0);
        rectangleFeedback.add(x1, y1);
        repaint();
    }

    private void paintRectangleFeedback(Graphics2D g2D, Color selectionColor, float planScale) {
        if (rectangleFeedback != null) {
            g2D.setPaint(new Color(selectionColor.getRed(), selectionColor.getGreen(), selectionColor.getBlue(), 32));
            g2D.fill(rectangleFeedback);
            g2D.setPaint(selectionColor);
            g2D.setStroke(new BasicStroke(1 / planScale));
            g2D.draw(rectangleFeedback);
        }
    }

    public void deleteFeedback() {
        rectangleFeedback = null;
        repaint();
    }
    /**
     * Moves the view from (dx, dy) unit in the scrolling zone it belongs to.
     */
    public void moveView(float dx, float dy) {
        if (getParent() instanceof JViewport) {
            JViewport viewport = (JViewport)getParent();
            Rectangle viewRectangle = viewport.getViewRect();
            viewRectangle.translate(Math.round(dx * plan.getScale()), Math.round(dy * plan.getScale()));
            viewRectangle.x = Math.min(Math.max(0, viewRectangle.x), getWidth() - viewRectangle.width);
            viewRectangle.y = Math.min(Math.max(0, viewRectangle.y), getHeight() - viewRectangle.height);
            viewport.setViewPosition(viewRectangle.getLocation());
        }
    }

    /**
     * Sets the scale used to display the plan.
     * If this component is displayed in a viewport the view position is updated
     * to ensure the center's view will remain the same after the scale change.
     */
    public void setScale(float scale) {
        if (this.scale == scale) return;

        JViewport parent = null;
        Rectangle viewRectangle = null;
        float xViewCenterPosition = 0;
        float yViewCenterPosition = 0;
        if (getParent() instanceof JViewport) {
            parent = (JViewport)getParent();
            viewRectangle = parent.getViewRect();
            xViewCenterPosition = convertXPixelToModel(viewRectangle.x + viewRectangle.width / 2);
            yViewCenterPosition = convertYPixelToModel(viewRectangle.y + viewRectangle.height / 2);
        }

        this.scale = scale;
        revalidate(false);

        if (parent instanceof JViewport) {
            Dimension viewSize = parent.getViewSize();
            float viewWidth = convertXPixelToModel(viewRectangle.x + viewRectangle.width) - convertXPixelToModel(viewRectangle.x);
            int xViewLocation = Math.max(0, Math.min(convertXModelToPixel(xViewCenterPosition - viewWidth / 2), viewSize.width - viewRectangle.x));
            float viewHeight = convertYPixelToModel(viewRectangle.y + viewRectangle.height) - convertYPixelToModel(viewRectangle.y);
            int yViewLocation = Math.max(0, Math.min(convertYModelToPixel(yViewCenterPosition - viewHeight / 2), viewSize.height - viewRectangle.y));
            parent.setViewPosition(new Point(xViewLocation, yViewLocation));
        }
    }

    /**
     * Returns <code>x</code> converted in model coordinates space.
     */
    public float convertXPixelToModel(int x) {
        Insets insets = getInsets();
        Rectangle2D planBounds = getPlanBounds();
        return (x - insets.left) / plan.getScale() - MARGIN + (float)planBounds.getMinX();
    }

    /**
     * Returns <code>y</code> converted in model coordinates space.
     */
    public float convertYPixelToModel(int y) {
        Insets insets = getInsets();
        Rectangle2D planBounds = getPlanBounds();
        return (y - insets.top) / plan.getScale() - MARGIN + (float)planBounds.getMinY();
    }

    /**
     * Returns <code>x</code> converted in view coordinates space.
     */
    public int convertXModelToPixel(float x) {
        Insets insets = getInsets();
        Rectangle2D planBounds = getPlanBounds();
        return (int)Math.round((x - planBounds.getMinX() + MARGIN) * plan.getScale()) + insets.left;
    }

    /**
     * Returns <code>y</code> converted in view coordinates space.
     */
    public int convertYModelToPixel(float y) {
        Insets insets = getInsets();
        Rectangle2D planBounds = getPlanBounds();
        return (int)Math.round((y - planBounds.getMinY() + MARGIN) * plan.getScale()) + insets.top;
    }

    /**
     * Returns <code>x</code> converted in screen coordinates space.
     */
    public int convertXModelToScreen(float x) {
        Point point = new Point(convertXModelToPixel(x), 0);
        SwingUtilities.convertPointToScreen(point, this);
        return point.x;
    }

    /**
     * Returns <code>y</code> converted in screen coordinates space.
     */
    public int convertYModelToScreen(float y) {
        Point point = new Point(0, convertYModelToPixel(y));
        SwingUtilities.convertPointToScreen(point, this);
        return point.y;
    }

    /**
     * Returns the bounds of <code>shape</code> in pixels coordinates space.
     */
    private Rectangle getShapePixelBounds(Shape shape) {
        Rectangle2D shapeBounds = shape.getBounds2D();
        return new Rectangle(
                convertXModelToPixel((float)shapeBounds.getMinX()),
                convertYModelToPixel((float)shapeBounds.getMinY()),
                (int)Math.round(shapeBounds.getWidth() * plan.getScale()),
                (int)Math.round(shapeBounds.getHeight() * plan.getScale()));
    }

    @Override
    public void setCursor(Cursor cursor) {
        super.setCursor(cursor);
    }

    /** Sets whether the resize indicator of selected wall should be visible or not */
    public void setResizeIndicatorVisible(boolean resizeIndicatorVisible) {
        this.resizeIndicatorVisible = resizeIndicatorVisible;
        repaint();
    }

    // Scrollable implementation
    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        if (orientation == SwingConstants.HORIZONTAL) {
            return visibleRect.width / 2;
        }

        // SwingConstants.VERTICAL
        return visibleRect.height / 2;
    }

    // Return true if the plan's preferred height is smaller than the viewport height
    @Override
    public boolean getScrollableTracksViewportHeight() {
        return getPreferredSize().height < getParent().getHeight();
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        // Return true if the plan's preferred width is smaller than the viewport width
        return getPreferredSize().width < getParent().getWidth();
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        if (orientation == SwingConstants.HORIZONTAL) {
            return visibleRect.width / 10;
        }
        // SwingConstants.VERTICAL
        return visibleRect.height / 10;
    }

    private void setRulersVisible(boolean visible) {
        if (visible) {
            // Change column and row header views
            scrollPane.setColumnHeaderView(horizontalRuler);
            scrollPane.setRowHeaderView(verticalRuler);
        } else {
            scrollPane.setColumnHeaderView(null);
            scrollPane.setRowHeaderView(null);
        }
    }

    public Callable<Void> showPrintDialog() {
        PageFormat pageFormat = PrinterUtil.getPageFormat(plan.getPageSetup());
        PrintDialog.show(new PlanPrintableComponent(planController, getFont()), pageFormat, plan.getProject().getFileName());
        return null;
    }
}
