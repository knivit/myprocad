package com.tsoft.myprocad.swing;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.border.AbstractBorder;

import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.swing.menu.Menu;
import com.tsoft.myprocad.swing.dialog.AbstractDialogPanel;
import com.tsoft.myprocad.swing.ext.ProportionalLayout;
import com.tsoft.myprocad.util.OperatingSystem;

public class PrintPreviewPanel extends AbstractDialogPanel {
    private JToolBar toolBar;
    private PrintableComponent printableComponent;
    private JLabel pageLabel;

    public PrintPreviewPanel(PrintableComponent printableComponent) {
        super(new ProportionalLayout());

        createActions();
        createComponents(printableComponent);
        layoutComponents();
        updateComponents();
    }

    @Override
    public Dimension getDialogPreferredSize() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        return new Dimension((int)Math.round(screenSize.width * 0.6), (int)Math.round(screenSize.height * 0.8));
    }

    private void createActions() {
        ActionListener PRINT_PREVIEW_SHOW_PREVIOUS_PAGE_ACTION = e -> {
            printableComponent.setPage(printableComponent.getPage() - 1);
            updateComponents();
        };

        ActionListener PRINT_PREVIEW_SHOW_NEXT_PAGE_ACTION = e -> {
            printableComponent.setPage(printableComponent.getPage() + 1);
            updateComponents();
        };

        Action showPreviousPageAction = new MenuAction(Menu.PRINT_PREVIEW_SHOW_PREVIOUS_PAGE, PRINT_PREVIEW_SHOW_PREVIOUS_PAGE_ACTION);
        Action showNextPageAction = new MenuAction(Menu.PRINT_PREVIEW_SHOW_NEXT_PAGE, PRINT_PREVIEW_SHOW_NEXT_PAGE_ACTION);

        ActionMap actionMap = getActionMap();
        actionMap.put(Menu.PRINT_PREVIEW_SHOW_PREVIOUS_PAGE, showPreviousPageAction);
        actionMap.put(Menu.PRINT_PREVIEW_SHOW_NEXT_PAGE, showNextPageAction);
    }

    private void createComponents(PrintableComponent printableComponent) {
        this.printableComponent = printableComponent;
        printableComponent.setBorder(BorderFactory.createCompoundBorder(
                new AbstractBorder() {
                    @Override
                    public Insets getBorderInsets(Component c) {
                        return new Insets(0, 0, 5, 5);
                    }

                    @Override
                    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                        Graphics2D g2D = (Graphics2D)g;
                        Color oldColor = g2D.getColor();

                        // Fill left and right border with a gradient
                        for (int i = 0; i < 5; i++) {
                            g2D.setColor(new Color(128, 128, 128, 200 - i * 45));
                            g2D.drawLine(x + width - 5 + i, y + i, x + width - 5 + i, y + height - 5 + i);
                            g2D.drawLine(x + i, y + height - 5 + i, x + width - 5 + i - 1, y + height - 5 + i);
                        }
                        g2D.setColor(oldColor);
                    }
                },
                BorderFactory.createLineBorder(Color.BLACK)));

        pageLabel = new JLabel();

        toolBar = new JToolBar() {
            public void applyComponentOrientation(ComponentOrientation orientation) {
                // Ignore orientation
            }
        };
        toolBar.setFloatable(false);

        ActionMap actions = getActionMap();
        if (OperatingSystem.isMacOSXLeopardOrSuperior() && OperatingSystem.isJavaVersionGreaterOrEqual("1.7")) {
            // Add buttons with higher insets to ensure the top and bottom of segmented buttons are correctly drawn
            class HigherInsetsButton extends JButton {
                public HigherInsetsButton(Action action) {
                    super(action);
                }

                @Override
                public Insets getInsets() {
                    Insets insets = super.getInsets();
                    insets.top += 3;
                    insets.bottom += 3;
                    return insets;
                }
            }
            toolBar.add(new HigherInsetsButton(actions.get(Menu.PRINT_PREVIEW_SHOW_PREVIOUS_PAGE)));
            toolBar.add(new HigherInsetsButton(actions.get(Menu.PRINT_PREVIEW_SHOW_NEXT_PAGE)));
        } else {
            toolBar.add(actions.get(Menu.PRINT_PREVIEW_SHOW_PREVIOUS_PAGE));
            toolBar.add(actions.get(Menu.PRINT_PREVIEW_SHOW_NEXT_PAGE));
        }
        updateToolBarButtonsStyle(toolBar);

        toolBar.add(Box.createHorizontalStrut(20));
        toolBar.add(this.pageLabel);

        // Remove focusable property on buttons
        for (int i = 0, n = toolBar.getComponentCount(); i < n; i++) {
            toolBar.getComponentAtIndex(i).setFocusable(false);
        }
    }

    private void updateToolBarButtonsStyle(JToolBar toolBar) {
        // Use segmented buttons under Mac OS X 10.5
        if (OperatingSystem.isMacOSXLeopardOrSuperior()) {
            // Retrieve component orientation because Mac OS X 10.5 miserably doesn't it take into account
            JComponent previousButton = (JComponent)toolBar.getComponentAtIndex(0);
            previousButton.putClientProperty("JButton.buttonType", "segmentedTextured");
            previousButton.putClientProperty("JButton.segmentPosition", "first");
            JComponent nextButton = (JComponent)toolBar.getComponentAtIndex(1);
            nextButton.putClientProperty("JButton.buttonType", "segmentedTextured");
            nextButton.putClientProperty("JButton.segmentPosition", "last");
        }
    }

    private void layoutComponents() {
        // Add toolbar at top in a flow layout panel to make it centered
        JPanel panel = new JPanel();
        panel.add(this.toolBar);
        add(panel, ProportionalLayout.Constraints.TOP);

        // Add printable component at bottom of proportional layout panel
        add(printableComponent, ProportionalLayout.Constraints.BOTTOM);
    }

    private void updateComponents() {
        ActionMap actions = getActionMap();
        actions.get(Menu.PRINT_PREVIEW_SHOW_PREVIOUS_PAGE).setEnabled(printableComponent.getPage() > 0);
        actions.get(Menu.PRINT_PREVIEW_SHOW_NEXT_PAGE).setEnabled(printableComponent.getPage() < printableComponent.getPageCount() - 1);
        pageLabel.setText(L10.get(L10.PRINT_PREVIEW_PAGE_LABEL, printableComponent.getPage() + 1, printableComponent.getPageCount()));
    }
}
