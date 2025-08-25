package de.eshop.client.ui;

import javax.swing.*;

public class DialogUtils {

    public static void emptyInputFields(JPanel panel) {

        JOptionPane.showMessageDialog(panel,
                "One or more input fields are empty.",
                "Empty input field(s).",
                JOptionPane.WARNING_MESSAGE);
    }

    public static void stockIsNotMultipleOfBulkSize(JPanel panel) {

        JOptionPane.showMessageDialog(panel,
                "Stock is not a multiple of bulk size.",
                "Stock not fitting for bulk size",
                JOptionPane.WARNING_MESSAGE);
    }
}
