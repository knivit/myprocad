package com.tsoft.myprocad.util;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class TextDialog extends JDialog implements ActionListener {
    private JTextArea ta;

    private JButton btnOK = new JButton("   OK   ");
    private JButton btnCancel = new JButton("Cancel");
    private String result;
    private String initialValue;

    private boolean convertLF;

    public TextDialog(String value, boolean convertLF) {
        super();
        this.convertLF = convertLF;

        // return initial value if closes by "close" button
        initialValue = value;
        result = initialValue;

        ta = new JTextArea(20, 60);
        ta.setLineWrap(false);
        ta.setFont(new Font("Monospaced", Font.PLAIN, 15));
        if (value != null && convertLF) value = StringUtil.replaceAll(value, "\\n", Character.toString('\n'));
        ta.setText(value);

        setModal(true);
        getContentPane().setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocation(400, 300);

        JScrollPane scrollPane = new JScrollPane(ta);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        JPanel jp = new JPanel();
        btnOK.addActionListener(this);
        btnCancel.addActionListener(this);
        jp.add(btnOK);
        jp.add(btnCancel);
        getContentPane().add(jp,BorderLayout.SOUTH);

        pack();
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == btnOK) {
            result = ta.getText();
            if (convertLF) result = StringUtil.replaceAll(ta.getText(), Character.toString('\n'), "\\n");
        }
        dispose();
    }

    public String getText() { return result; }
}
