package net.eshop.ui.view;

import net.eshop.ui.viewmodel.LoginAndRegistrationViewModel;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

public class LoginAndRegistrationView {

    private static final Logger logger = Logger.getLogger(LoginAndRegistrationView.class.getName());

    private final LoginAndRegistrationViewModel loginAndRegistrationViewModel;

    public LoginAndRegistrationView(LoginAndRegistrationViewModel loginAndRegistrationViewModel) {
        this.loginAndRegistrationViewModel = loginAndRegistrationViewModel;
    }

    public JPanel login() {

        JPanel loginMainPanel = new JPanel(new GridBagLayout());
        loginMainPanel.setPreferredSize(new Dimension(350, 200));

        JPanel loginChildPanel = new JPanel();
        loginChildPanel.setPreferredSize(new Dimension(350, 200));
        loginChildPanel.setLayout(new BoxLayout(loginChildPanel, BoxLayout.Y_AXIS));

        JTextField userNameTextField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        JButton customerRegisterButton = new JButton("Register");
        JButton customerLoginButton = new JButton("Login");
        Dimension buttonDimension = new Dimension(200, 35);

        // Login for customer
        customerLoginButton.addActionListener(actionEvent -> loginAndRegistrationViewModel.customerLoginButtonClickHandler(userNameTextField.getText(), passwordField.getPassword(), loginMainPanel));

        customerRegisterButton.addActionListener(actionEvent -> {
            loginChildPanel.setVisible(false);
            loginMainPanel.add(customerRegistration());
        });

        customerLoginButton.setPreferredSize(buttonDimension);
        customerRegisterButton.setPreferredSize(buttonDimension);
        customerLoginButton.setMaximumSize(buttonDimension);
        customerRegisterButton.setMaximumSize(buttonDimension);

        loginChildPanel.add(Box.createVerticalStrut(10));
        loginChildPanel.add(userNameTextField);
        loginChildPanel.add(Box.createVerticalStrut(10));
        loginChildPanel.add(passwordField);
        loginChildPanel.add(Box.createVerticalStrut(10));
        loginChildPanel.add(customerLoginButton);
        loginChildPanel.add(Box.createVerticalStrut(10));
        loginChildPanel.add(customerRegisterButton);
        loginChildPanel.add(Box.createVerticalStrut(10));

        JLabel employeeLogin = new JLabel("<HTML><U>Employee login</U></HTML>", SwingConstants.CENTER);
        employeeLogin.setForeground(Color.BLUE);
        employeeLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        employeeLogin.setAlignmentX(Component.RIGHT_ALIGNMENT);
        loginChildPanel.add(employeeLogin);

        // Login for staff member
        loginAndRegistrationViewModel.staffMemberLoginButtonClickHandler(userNameTextField.getText(), passwordField.getPassword(), loginMainPanel, employeeLogin);

        customerRegisterButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        customerLoginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        employeeLogin.setAlignmentX(Component.CENTER_ALIGNMENT);

        loginMainPanel.add(loginChildPanel);

        return loginMainPanel;
    }

    public JPanel customerRegistration() {

        JPanel registrationMainPanel = new JPanel(new GridBagLayout());
        registrationMainPanel.setPreferredSize(new Dimension(350, 200));

        JPanel registerChildPanel = new JPanel();
        registerChildPanel.setPreferredSize(new Dimension(350, 200));
        registerChildPanel.setLayout(new BoxLayout(registerChildPanel, BoxLayout.Y_AXIS));

        JTextField userNameTextField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JTextField addressTextField = new JTextField();

        JButton backButton = new JButton("Back");
        JButton registerButton = new JButton("Register");
        Dimension buttonDimension = new Dimension(200, 35);

        // Login for customer
        registerButton.addActionListener(actionEvent -> loginAndRegistrationViewModel.registerButtonClickHandler(userNameTextField.getText(), passwordField.getPassword(), addressTextField.getText(),registrationMainPanel));

        // Back to log in
        backButton.addActionListener(actionEvent -> {
            registerChildPanel.setVisible(false);
            registrationMainPanel.add(login());
        });

        registerButton.setPreferredSize(buttonDimension);
        backButton.setPreferredSize(buttonDimension);
        registerButton.setMaximumSize(buttonDimension);
        backButton.setMaximumSize(buttonDimension);

        registerChildPanel.add(Box.createVerticalStrut(10));
        registerChildPanel.add(userNameTextField);
        registerChildPanel.add(Box.createVerticalStrut(10));
        registerChildPanel.add(passwordField);
        registerChildPanel.add(Box.createVerticalStrut(10));
        registerChildPanel.add(addressTextField);
        registerChildPanel.add(Box.createVerticalStrut(10));
        registerChildPanel.add(registerButton);
        registerChildPanel.add(Box.createVerticalStrut(10));
        registerChildPanel.add(backButton);
        registerChildPanel.add(Box.createVerticalStrut(10));

        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        registrationMainPanel.add(registerChildPanel);

        return registrationMainPanel;
    }
}
