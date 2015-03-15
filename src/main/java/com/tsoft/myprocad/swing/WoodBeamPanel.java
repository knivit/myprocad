package com.tsoft.myprocad.swing;

import com.tsoft.myprocad.MyProCAD;
import com.tsoft.myprocad.swing.properties.PropertiesManagerPanel;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

public class WoodBeamPanel extends JPanel implements Printable {
    private JLabel[] labels = new JLabel[1];

    private JComponent parentComponent;
    private JSplitPane splitPane;
    private JEditorPane textPane;

    public WoodBeamPanel() {
        super(new BorderLayout());

        // center
        textPane = new JEditorPane();
        textPane.setContentType("text/html");
        Font font = textPane.getFont();
        JScrollPane textScrollPane = new JScrollPane(textPane);

        // right
        labels[0] = new JLabel(new ImageIcon(MyProCAD.class.getResource("resources/calculations/woodbeam.jpg")));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        panel.setBackground(Color.WHITE);

        for (int i = 0; i < labels.length; i++) {
            JPanel figPanel = new JPanel();
            figPanel.setOpaque(false);
            figPanel.setLayout(new BoxLayout(figPanel, BoxLayout.LINE_AXIS));
            figPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 4, 8));
            figPanel.add(Box.createHorizontalGlue());
            figPanel.add(labels[i]);
            figPanel.add(Box.createHorizontalGlue());
            panel.add(figPanel);
        }

        JScrollPane picScrollPane = new JScrollPane(panel);
        picScrollPane.setOpaque(false);
        picScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, textScrollPane, picScrollPane);
        splitPane.setDividerLocation(350);
        splitPane.setContinuousLayout(true);
        splitPane.setOneTouchExpandable(true);
        splitPane.setResizeWeight(0.3);
        splitPane.setBorder(null);
    }

    public void afterOpen(PropertiesManagerPanel propertiesManagerPanel) {
        parentComponent = propertiesManagerPanel.linkTo(splitPane);
        add(BorderLayout.CENTER, parentComponent);
    }

    public JComponent getParentComponent() {
        return parentComponent;
    }

    public void setText(String text) {
        textPane.setText("<html><body style='font-size: 22'>" + text + "</body></html>");
        textPane.setCaretPosition(0);
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        return 0;
    }
}
