package net.eshop.ui;

import javax.swing.*;

public class DialogUtils {

    public static void emptyInputFields(JPanel panel) {

        JOptionPane.showMessageDialog(panel,
                "One or more fields are empty.",
                "Empty input field(s).",
                JOptionPane.WARNING_MESSAGE);
    }
}
