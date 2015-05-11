package com.tsoft.myprocad.util.script;

import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.model.Plan;
import com.tsoft.myprocad.swing.PlanPanel;
import com.tsoft.myprocad.util.StringUtil;
import com.tsoft.myprocad.util.SwingTools;

import javax.script.ScriptException;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class JavaScriptPanel extends JPanel {
    public static String REGULAR_OUTPUT = "regular";
    public static String ERROR_OUTPUT = "error";

    private JavaScript js;
    private JTextArea commandArea;
    private JTextPane resultsArea;
    private StyledDocument output;

    private JButton btnExecute = new JButton(L10.get(L10.EXECUTE_JAVA_SCRIPT));
    private JButton btnClear = new JButton(L10.get(L10.CLEAR_JAVA_SCRIPT_OUTPUT));
    private JButton btnHide = new JButton(L10.get(L10.HIDE_JAVA_SCRIPT_PANEL));

    public JavaScriptPanel(Plan plan, PlanPanel planPanel) {
        super();

        js = new JavaScript();
        js.addBinding("Plan", plan);

        final OutputBinding outputBinding = new OutputBinding(js) {
            public void print(String text) {
                doPrint(text, JavaScriptPanel.REGULAR_OUTPUT);
            }

            public void error(String text) {
                doPrint(text, JavaScriptPanel.ERROR_OUTPUT);
            }

            public void clear() { resultsArea.setText(""); }

            private void doPrint(String text, String style) {
                try {
                    output.insertString(output.getLength(), text + '\n', output.getStyle(style));
                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                    SwingTools.showError(ex.getMessage());
                }
            }
        };

        setLayout(new BorderLayout());

        commandArea = new JTextArea(20, 60);
        commandArea.setLineWrap(false);
        commandArea.setFont(new Font("Monospaced", Font.PLAIN, 15));
        commandArea.setText(plan.getScript());
        commandArea.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                plan.setScript(commandArea.getText());
            }
        });
        JScrollPane commandPanel = new JScrollPane(commandArea);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.LINE_AXIS));
        btnExecute.addActionListener((e) -> {
            String commands = commandArea.getSelectedText();
            if (StringUtil.isEmpty(commands)) commands = commandArea.getText();
            try {
                outputBinding.clear();
                js.execute(commands, outputBinding);
                plan.getController().itemListChanged();
            } catch (ScriptException ex) {
                outputBinding.error(ex.getMessage());
            }
        });
        btnClear.addActionListener((e) -> resultsArea.setText(""));

        buttonsPanel.add(Box.createHorizontalStrut(8));
        buttonsPanel.add(btnExecute);
        buttonsPanel.add(Box.createHorizontalStrut(8));
        buttonsPanel.add(btnClear);
        buttonsPanel.add(Box.createHorizontalGlue());
        buttonsPanel.add(btnHide);
        buttonsPanel.add(Box.createHorizontalStrut(8));
        btnHide.addActionListener((e) -> planPanel.hideCommandWindow());

        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BorderLayout());
        resultPanel.add(buttonsPanel, BorderLayout.NORTH);

        resultsArea = new JTextPane();
        output = resultsArea.getStyledDocument();
        addStylesToDocument(output);

        JScrollPane resultScrollPane = new JScrollPane(resultsArea);
        resultScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        resultPanel.add(resultScrollPane, BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, commandPanel, resultPanel);
        splitPane.setContinuousLayout(true);
        splitPane.setOneTouchExpandable(true);
        splitPane.setResizeWeight(0.7);
        splitPane.setBorder(null);

        add(splitPane, BorderLayout.CENTER);
    }

    // Initialize styles
    private void addStylesToDocument(StyledDocument doc) {
        Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);

        Style regular = doc.addStyle(REGULAR_OUTPUT, def);
        StyleConstants.setFontFamily(def, "SansSerif");

        Style s = doc.addStyle(ERROR_OUTPUT, regular);
        StyleConstants.setForeground(s, Color.RED);
    }
}
