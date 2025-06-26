package net.eshop.ui;

import net.eshop.domain.Customer;
import net.eshop.domain.dataaccess.DataPersister;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

public class LoginUI {

    private static final Logger logger = Logger.getLogger(LoginUI.class.getName());

    private final DataPersister dataPersister;

    public LoginUI(DataPersister dataPersister) {
        this.dataPersister = dataPersister;
    }

    public JPanel loginAndRegister() {

        JPanel loginAndRegisterMainPanel = new JPanel(new GridBagLayout());
        loginAndRegisterMainPanel.setPreferredSize(new Dimension(350, 200));

        JPanel loginAndRegisterChildPanel = new JPanel();
        loginAndRegisterChildPanel.setPreferredSize(new Dimension(350, 200));
        loginAndRegisterChildPanel.setLayout(new BoxLayout(loginAndRegisterChildPanel, BoxLayout.Y_AXIS));

        JTextField userNameTextField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        Dimension buttonDimension = new Dimension(200, 35);

        // Login for customer
        loginButton.addActionListener(actionEvent -> {

            StringBuilder stringBuilder = new StringBuilder();

            for (char c : passwordField.getPassword())
                stringBuilder.append(c);

            Customer customer = dataPersister.findUserByCredentials(userNameTextField.getText(), stringBuilder.toString(), Customer.class);

            if (customer == null) {

                logger.info("Login failed for customer.");

                JOptionPane.showMessageDialog(loginAndRegisterMainPanel,
                        "Username or password is incorrect.",
                        "Wrong credentials",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            loginAndRegisterMainPanel.setVisible(false);

            System.setProperty("CURRENT_USER", "CUSTOMER");
            System.setProperty("CURRENT_USER_ID", String.valueOf(customer.getNumber()));

            logger.info("Login successfully for customer " + userNameTextField.getText());
        });

        loginButton.setPreferredSize(buttonDimension);
        registerButton.setPreferredSize(buttonDimension);
        loginButton.setMaximumSize(buttonDimension);
        registerButton.setMaximumSize(buttonDimension);

        loginAndRegisterChildPanel.add(Box.createVerticalStrut(10));
        loginAndRegisterChildPanel.add(userNameTextField);
        loginAndRegisterChildPanel.add(Box.createVerticalStrut(10));
        loginAndRegisterChildPanel.add(passwordField);
        loginAndRegisterChildPanel.add(Box.createVerticalStrut(10));
        loginAndRegisterChildPanel.add(registerButton);
        loginAndRegisterChildPanel.add(Box.createVerticalStrut(10));
        loginAndRegisterChildPanel.add(loginButton);
        loginAndRegisterChildPanel.add(Box.createVerticalStrut(10));

        JLabel employeeLogin = new JLabel("<HTML><U>Employee login</U></HTML>", SwingConstants.CENTER);
        employeeLogin.setForeground(Color.BLUE);
        employeeLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        employeeLogin.setAlignmentX(Component.RIGHT_ALIGNMENT);
        loginAndRegisterChildPanel.add(employeeLogin);

        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        employeeLogin.setAlignmentX(Component.CENTER_ALIGNMENT);

        loginAndRegisterMainPanel.add(loginAndRegisterChildPanel);

        return loginAndRegisterMainPanel;
    }
}
