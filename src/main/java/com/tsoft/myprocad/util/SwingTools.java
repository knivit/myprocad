package com.tsoft.myprocad.util;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.text.JTextComponent;

import com.tsoft.myprocad.l10n.L10;

public class SwingTools {
    public enum SaveAnswer {SAVE, CANCEL, DO_NOT_SAVE};

    private static ContentManager contentManager = new FileContentManagerWithRecordedLastDirectories();

    // Borders for focused views
    private static Border unfocusedViewBorder;
    private static Border focusedViewBorder;

    private SwingTools() {
        // This class contains only tools
    }

    public static void invokeLater(Runnable runnable) {
        EventQueue.invokeLater(runnable);
    }

    /**
     * Updates the border of <code>component</code> with an empty border
     * changed to a colored border when it will gain focus.
     * If the <code>component</code> component is the child of a <code>JViewPort</code>
     * instance this border will be installed on its scroll pane parent.
     */
    public static void installFocusBorder(JComponent component) {
        if (unfocusedViewBorder == null) {
            Border unfocusedViewInteriorBorder = new AbstractBorder() {
                private Color  topLeftColor;
                private Color  botomRightColor;
                private Insets insets = new Insets(1, 1, 1, 1);

                {
                    if (OperatingSystem.isMacOSX()) {
                        this.topLeftColor = Color.GRAY;
                        this.botomRightColor = Color.LIGHT_GRAY;
                    } else {
                        this.topLeftColor = UIManager.getColor("TextField.darkShadow");
                        this.botomRightColor  = UIManager.getColor("TextField.shadow");
                    }
                }

                public Insets getBorderInsets(Component c) {
                    return this.insets;
                }

                public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                    Color previousColor = g.getColor();
                    Rectangle rect = getInteriorRectangle(c, x, y, width, height);
                    g.setColor(topLeftColor);
                    g.drawLine(rect.x - 1, rect.y - 1, rect.x + rect.width, rect.y - 1);
                    g.drawLine(rect.x - 1, rect.y - 1, rect.x - 1, rect.y  + rect.height);
                    g.setColor(botomRightColor);
                    g.drawLine(rect.x, rect.y  + rect.height, rect.x + rect.width, rect.y  + rect.height);
                    g.drawLine(rect.x + rect.width, rect.y, rect.x + rect.width, rect.y  + rect.height);
                    g.setColor(previousColor);
                }
            };

            if (OperatingSystem.isMacOSXLeopardOrSuperior()) {
                unfocusedViewBorder = BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(UIManager.getColor("Panel.background"), 2),
                        unfocusedViewInteriorBorder);
                focusedViewBorder = new AbstractBorder() {
                    private Insets insets = new Insets(3, 3, 3, 3);

                    public Insets getBorderInsets(Component c) {
                        return this.insets;
                    }

                    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                        Color previousColor = g.getColor();
                        // Paint a gradient paint around component
                        Rectangle rect = getInteriorRectangle(c, x, y, width, height);
                        g.setColor(Color.GRAY);
                        g.drawLine(rect.x - 1, rect.y - 1, rect.x + rect.width, rect.y - 1);
                        g.drawLine(rect.x - 1, rect.y - 1, rect.x - 1, rect.y  + rect.height);
                        g.setColor(Color.LIGHT_GRAY);
                        g.drawLine(rect.x, rect.y  + rect.height, rect.x + rect.width, rect.y  + rect.height);
                        g.drawLine(rect.x + rect.width, rect.y, rect.x + rect.width, rect.y  + rect.height);
                        Color focusColor = UIManager.getColor("Focus.color");
                        int   transparencyOutline = 128;
                        int   transparencyInline  = 180;
                        if (focusColor == null) {
                            focusColor = UIManager.getColor("textHighlight");
                            transparencyOutline = 128;
                            transparencyInline = 255;
                        }
                        g.setColor(new Color(focusColor.getRed(), focusColor.getGreen(), focusColor.getBlue(), transparencyOutline));
                        g.drawRoundRect(rect.x - 3, rect.y - 3, rect.width + 5, rect.height + 5, 6, 6);
                        g.drawRect(rect.x - 1, rect.y - 1, rect.width + 1, rect.height + 1);
                        g.setColor(new Color(focusColor.getRed(), focusColor.getGreen(), focusColor.getBlue(), transparencyInline));
                        g.drawRoundRect(rect.x - 2, rect.y - 2, rect.width + 3, rect.height + 3, 4, 4);

                        // Draw corners
                        g.setColor(UIManager.getColor("Panel.background"));
                        g.drawLine(rect.x - 3, rect.y - 3, rect.x - 2, rect.y - 3);
                        g.drawLine(rect.x - 3, rect.y - 2, rect.x - 3, rect.y - 2);
                        g.drawLine(rect.x + rect.width + 1, rect.y - 3, rect.x + rect.width + 2, rect.y - 3);
                        g.drawLine(rect.x + rect.width + 2, rect.y - 2, rect.x + rect.width + 2, rect.y - 2);
                        g.drawLine(rect.x - 3, rect.y + rect.height + 2, rect.x - 2, rect.y + rect.height + 2);
                        g.drawLine(rect.x - 3, rect.y + rect.height + 1, rect.x - 3, rect.y + rect.height + 1);
                        g.drawLine(rect.x + rect.width + 1, rect.y + rect.height + 2, rect.x + rect.width + 2, rect.y + rect.height + 2);
                        g.drawLine(rect.x + rect.width + 2, rect.y + rect.height + 1, rect.x + rect.width + 2, rect.y + rect.height + 1);

                        g.setColor(previousColor);
                    }
                };
            } else {
                if (OperatingSystem.isMacOSX()) {
                    unfocusedViewBorder = BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(UIManager.getColor("Panel.background"), 1),
                            unfocusedViewInteriorBorder);
                } else {
                    unfocusedViewBorder = BorderFactory.createCompoundBorder(
                            BorderFactory.createEmptyBorder(1, 1, 1, 1),
                            unfocusedViewInteriorBorder);
                }
                focusedViewBorder = BorderFactory.createLineBorder(UIManager.getColor("textHighlight"), 2);
            }
        }

        final JComponent feedbackComponent;
        if (component.getParent() instanceof JViewport
                && component.getParent().getParent() instanceof JScrollPane) {
            feedbackComponent = (JComponent)component.getParent().getParent();
        } else {
            feedbackComponent = component;
        }
        feedbackComponent.setBorder(unfocusedViewBorder);
        component.addFocusListener(new FocusListener() {
            public void focusLost(FocusEvent ev) {
                if (feedbackComponent.getBorder() == focusedViewBorder) {
                    feedbackComponent.setBorder(unfocusedViewBorder);
                }
            }

            public void focusGained(FocusEvent ev) {
                if (feedbackComponent.getBorder() == unfocusedViewBorder) {
                    feedbackComponent.setBorder(focusedViewBorder);
                }
            }
        });
    }

    /**
     * Updates the Swing resource bundles in use from the current Locale and class loader.
     */
    public static void updateSwingResourceLanguage() {
        updateSwingResourceLanguage(Arrays.asList(new ClassLoader [] {SwingTools.class.getClassLoader()}));
    }

    /**
     * Updates the Swing resource bundles in use from the current Locale and class loaders.
     */
    private static void updateSwingResourceLanguage(List<ClassLoader> classLoaders) {
        // Read Swing localized properties because Swing doesn't update its internal strings automatically
        // when default Locale is updated (see bug http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4884480)
        updateSwingResourceBundle("com.sun.swing.internal.plaf.metal.resources.metal", classLoaders);
        updateSwingResourceBundle("com.sun.swing.internal.plaf.basic.resources.basic", classLoaders);
        if (UIManager.getLookAndFeel().getClass().getName().equals("com.sun.java.swing.plaf.gtk.GTKLookAndFeel")) {
            updateSwingResourceBundle("com.sun.java.swing.plaf.gtk.resources.gtk", classLoaders);
        } else if (UIManager.getLookAndFeel().getClass().getName().equals("com.sun.java.swing.plaf.motif.MotifLookAndFeel")) {
            updateSwingResourceBundle("com.sun.java.swing.plaf.motif.resources.motif", classLoaders);
        }
    }

    /**
     * Updates a Swing resource bundle in use from the current Locale.
     */
    private static void updateSwingResourceBundle(String swingResource, List<ClassLoader> classLoaders) {
        ResourceBundle resource = ResourceBundle.getBundle(swingResource, Locale.ENGLISH);
        try {
            Locale defaultLocale = Locale.getDefault();
            for (ClassLoader classLoader : classLoaders) {
                ResourceBundle bundle = ResourceBundle.getBundle(swingResource, defaultLocale, classLoader);
                if (defaultLocale.equals(bundle.getLocale())) {
                    resource = bundle;
                    break;
                } else if (!resource.getLocale().getLanguage().equals(bundle.getLocale().getLanguage())
                        && defaultLocale.getLanguage().equals(bundle.getLocale().getLanguage())) {
                    resource = bundle;
                    // Don't break in case a bundle with language + country is found with an other class loader
                }
            }
        } catch (MissingResourceException ex) {
        }
        // Update UIManager properties
        for (Enumeration<?> it = resource.getKeys(); it.hasMoreElements(); ) {
            String property = (String)it.nextElement();
            UIManager.put(property, resource.getString(property));
        }
    }

    /**
     * Adds focus and mouse listeners to the given <code>textComponent</code> that will
     * select all its text when it gains focus by transfer.
     */
    public static void addAutoSelectionOnFocusGain(final JTextComponent textComponent) {
        // A focus and mouse listener able to select text field characters
        // when it gains focus after a focus transfer
        class SelectionOnFocusManager extends MouseAdapter implements FocusListener {
            private boolean mousePressedInTextField = false;
            private int selectionStartBeforeFocusLost = -1;
            private int selectionEndBeforeFocusLost = -1;

            @Override
            public void mousePressed(MouseEvent ev) {
                this.mousePressedInTextField = true;
                this.selectionStartBeforeFocusLost = -1;
            }

            public void focusLost(FocusEvent ev) {
                if (ev.getOppositeComponent() == null
                        || SwingUtilities.getWindowAncestor(ev.getOppositeComponent())
                        != SwingUtilities.getWindowAncestor(textComponent)) {
                    // Keep selection indices when focus on text field is transfered
                    // to an other window
                    this.selectionStartBeforeFocusLost = textComponent.getSelectionStart();
                    this.selectionEndBeforeFocusLost = textComponent.getSelectionEnd();
                } else {
                    this.selectionStartBeforeFocusLost = -1;
                }
            }

            public void focusGained(FocusEvent ev) {
                if (this.selectionStartBeforeFocusLost != -1) {
                    EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            // Reselect the same characters in text field
                            textComponent.setSelectionStart(selectionStartBeforeFocusLost);
                            textComponent.setSelectionEnd(selectionEndBeforeFocusLost);
                        }
                    });
                } else if (!this.mousePressedInTextField
                        && ev.getOppositeComponent() != null
                        && SwingUtilities.getWindowAncestor(ev.getOppositeComponent())
                        == SwingUtilities.getWindowAncestor(textComponent)) {
                    EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            // Select all characters when text field got the focus because of a transfer
                            textComponent.selectAll();
                        }
                    });
                }
                this.mousePressedInTextField = false;
            }
        };

        SelectionOnFocusManager selectionOnFocusManager = new SelectionOnFocusManager();
        textComponent.addFocusListener(selectionOnFocusManager);
        textComponent.addMouseListener(selectionOnFocusManager);
    }

    /**
     * Requests the focus for the given component.
     */
    private static void requestFocus(final JComponent focusedComponent) {
        if (!focusedComponent.requestFocusInWindow()) {
            // Prefer to call requestFocusInWindow in a timer with a small delay
            // than calling it with EnventQueue#invokeLater to ensure it always works
            new Timer(50, new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    focusedComponent.requestFocusInWindow();
                    ((Timer)ev.getSource()).stop();
                }
            }).start();
        }
    }

    /**
     * Returns a scroll pane containing the given <code>component</code>
     * that always displays scroll bars under Mac OS X.
     */
    public static JScrollPane createScrollPane(JComponent component) {
        JScrollPane scrollPane = new JScrollPane(component);
        if (OperatingSystem.isMacOSX()) {
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        }
        installFocusBorder(component);
        scrollPane.setMinimumSize(new Dimension());
        return scrollPane;
    }

    /**
     * Returns a new custom cursor.
     */
    public static Cursor createCustomCursor(URL smallCursorImageUrl, URL largeCursorImageUrl, float xCursorHotSpot,
                                            float yCursorHotSpot, String cursorName, Cursor defaultCursor) {
        if (GraphicsEnvironment.isHeadless()) {
            return defaultCursor;
        }

        // Retrieve system cursor size
        Dimension cursorSize = Toolkit.getDefaultToolkit().getBestCursorSize(16, 16);
        URL cursorImageResource;
        // If returned cursor size is 0, system doesn't support custom cursor
        if (cursorSize.width == 0) {
            return defaultCursor;
        } else {
            // Use a different cursor image depending on system cursor size
            if (cursorSize.width > 16) {
                cursorImageResource = largeCursorImageUrl;
            } else {
                cursorImageResource = smallCursorImageUrl;
            }
            try {
                // Read cursor image
                BufferedImage cursorImage = ImageIO.read(cursorImageResource);
                // Create custom cursor from image
                return Toolkit.getDefaultToolkit().createCustomCursor(cursorImage,
                        new Point(Math.min(cursorSize.width - 1, Math.round(cursorSize.width * xCursorHotSpot)),
                                Math.min(cursorSize.height - 1, Math.round(cursorSize.height * yCursorHotSpot))),
                        cursorName);
            } catch (IOException ex) {
                throw new IllegalArgumentException("Unknown resource " + cursorImageResource);
            }
        }
    }

    public static String showOpenDialog(ContentManager.ContentType contentType) {
        return contentManager.showOpenDialog(L10.get(L10.OPEN_DIALOG), contentType);
    }

    public static String showSaveDialog(String fileName, ContentManager.ContentType contentType) {
        return contentManager.showSaveDialog(L10.get(L10.SAVE_DIALOG), contentType, fileName);
    }

    public static void showError(String message) {
        JOptionPane.showMessageDialog(null, message, L10.get(L10.ERROR_DIALOG), JOptionPane.ERROR_MESSAGE);
    }

    public static void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message, L10.get(L10.MESSAGE_DIALOG), JOptionPane.INFORMATION_MESSAGE);
    }

    /** Returns true if it is confirmed */
    public static boolean showConfirmDialog(String message) {
        JOptionPane optionPane = new JOptionPane(message, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
        final JDialog dialog = optionPane.createDialog(null, L10.get(L10.CONFIRM_DIALOG));
        dialog.setVisible(true);

        dialog.dispose();
        Object value = optionPane.getValue();
        if (value instanceof Integer) {
            return (Integer)value == JOptionPane.OK_OPTION;
        }
        return false;
    }

    /** Input text field */
    public static String showInputDialog(String message, String initialValue) {
        return JOptionPane.showInputDialog(message, initialValue);
    }

    /** ComboBox field */
    public static <T> T showInputDialog(String message, Collection<T> values, String initialValue) {
        JComboBox jcb = new JComboBox(values.toArray());
        jcb.setEditable(false);
        if (!StringUtil.isEmpty(initialValue)) {
            int index = 0;
            initialValue = initialValue.toLowerCase();
            for (T value : values) {
                String strValue = value.toString();
                if (strValue.toLowerCase().startsWith(initialValue)) break;
                index ++;
            }
            jcb.setSelectedIndex(index);
        }

        JOptionPane.showMessageDialog(null, jcb, message, JOptionPane.QUESTION_MESSAGE);
        return (T)jcb.getSelectedItem();
    }
}
