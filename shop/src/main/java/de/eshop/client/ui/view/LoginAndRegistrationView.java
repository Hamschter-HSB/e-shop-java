package de.eshop.client.ui.view;

import de.eshop.client.ui.viewmodel.LoginAndRegistrationViewModel;
import de.eshop.shared.domain.exceptions.CustomerNotFoundException;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginAndRegistrationView {

    private static final Logger logger = Logger.getLogger(LoginAndRegistrationView.class.getName());

    private final LoginAndRegistrationViewModel loginAndRegistrationViewModel;

    private Runnable backToLoginListener;
    private Runnable customerRegistrationListener;

    public LoginAndRegistrationView(LoginAndRegistrationViewModel loginAndRegistrationViewModel) {
        this.loginAndRegistrationViewModel = loginAndRegistrationViewModel;
    }

    public JPanel login() {

        JPanel loginMainPanel = new JPanel(new GridBagLayout());

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        Dimension textFieldDimension = new Dimension(400, 35);

        // Store Label
        JLabel staffMemberRegistrationLabel = new JLabel("E-Shop", SwingConstants.CENTER);
        centerPanel.add(staffMemberRegistrationLabel);
        staffMemberRegistrationLabel.setFont(new Font("Arial", Font.BOLD, 60));
        centerPanel.add(Box.createVerticalStrut(40));

        // Name-Field
        JPanel namePanel = new JPanel(new GridBagLayout());
        namePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        GridBagConstraints gbc1 = new GridBagConstraints();
        gbc1.gridx = 0;
        gbc1.gridy = 0;
        gbc1.anchor = GridBagConstraints.WEST;
        gbc1.insets = new Insets(0, 0, 5, 0);
        namePanel.add(new JLabel("Name:"), gbc1);

        gbc1.gridy++;
        gbc1.fill = GridBagConstraints.HORIZONTAL;
        gbc1.weightx = 1.0;
        JTextField userNameTextField = new JTextField(20);
        userNameTextField.setPreferredSize(textFieldDimension);
        namePanel.add(userNameTextField, gbc1);
        centerPanel.add(namePanel);
        centerPanel.add(Box.createVerticalStrut(10));

        // Password-Field
        JPanel passwordPanel = new JPanel(new GridBagLayout());
        passwordPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.gridx = 0;
        gbc2.gridy = 0;
        gbc2.anchor = GridBagConstraints.WEST;
        gbc2.insets = new Insets(0, 0, 5, 0);
        passwordPanel.add(new JLabel("Password:"), gbc2);

        gbc2.gridy++;
        gbc2.fill = GridBagConstraints.HORIZONTAL;
        gbc2.weightx = 1.0;
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setPreferredSize(textFieldDimension);
        passwordPanel.add(passwordField, gbc2);
        centerPanel.add(passwordPanel);
        centerPanel.add(Box.createVerticalStrut(10));

        Dimension buttonDimension = new Dimension(200, 35);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton customerLoginButton = new JButton("Login");
        JButton customerRegisterButton = new JButton("Register");
        customerLoginButton.setPreferredSize(buttonDimension);
        customerRegisterButton.setPreferredSize(buttonDimension);
        buttonPanel.add(customerLoginButton);
        buttonPanel.add(customerRegisterButton);
        centerPanel.add(buttonPanel);

        // Login for customer button function
        customerLoginButton.addActionListener(actionEvent -> {
            try {
                loginAndRegistrationViewModel.customerLoginButtonClickHandler(userNameTextField, passwordField, loginMainPanel);
            } catch (CustomerNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        // Register customer Button function
        customerRegisterButton.addActionListener(actionEvent -> {
            if (customerRegistrationListener != null) {
                customerRegistrationListener.run();

                userNameTextField.setText("");
                passwordField.setText("");
            }
        });

        JPanel employeeLoginPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel employeeLogin = new JLabel("<HTML><U>Employee login</U></HTML>", SwingConstants.CENTER);
        employeeLogin.setForeground(Color.BLUE);
        employeeLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        employeeLogin.setAlignmentX(Component.RIGHT_ALIGNMENT);
        employeeLoginPanel.add(employeeLogin);

        centerPanel.add(employeeLoginPanel);

        // Login for staff member
        loginAndRegistrationViewModel.staffMemberLoginButtonClickHandler(userNameTextField, passwordField, loginMainPanel, employeeLogin);
        loginMainPanel.add(centerPanel);

        return loginMainPanel;
    }

    public JPanel customerRegistration() {

        // Hauptpanel mit GridBagLayout für vertikale UND horizontale Zentrierung
        JPanel registerChildPanel = new JPanel(new GridBagLayout());

        // Container für Inhalte (damit man sie zentriert platzieren kann)
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        Dimension textFieldDimension = new Dimension(400, 35);

        // Registration Main Label
        JLabel staffMemberRegistrationLabel = new JLabel("Registration", SwingConstants.CENTER);
        centerPanel.add(staffMemberRegistrationLabel);
        staffMemberRegistrationLabel.setFont(new Font("Arial", Font.BOLD, 60));

        centerPanel.add(Box.createVerticalStrut(40));

        // Name-Field
        JPanel namePanel = new JPanel(new GridBagLayout());
        namePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        GridBagConstraints gbc1 = new GridBagConstraints();
        gbc1.gridx = 0;
        gbc1.gridy = 0;
        gbc1.anchor = GridBagConstraints.WEST;
        gbc1.insets = new Insets(0, 0, 5, 0);
        namePanel.add(new JLabel("Name:"), gbc1);

        gbc1.gridy++;
        gbc1.fill = GridBagConstraints.HORIZONTAL;
        gbc1.weightx = 1.0;
        JTextField userNameTextField = new JTextField(20);
        userNameTextField.setPreferredSize(textFieldDimension);
        namePanel.add(userNameTextField, gbc1);
        centerPanel.add(namePanel);
        centerPanel.add(Box.createVerticalStrut(10));

        // Password-Field
        JPanel passwordPanel = new JPanel(new GridBagLayout());
        passwordPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.gridx = 0;
        gbc2.gridy = 0;
        gbc2.anchor = GridBagConstraints.WEST;
        gbc2.insets = new Insets(0, 0, 5, 0);
        passwordPanel.add(new JLabel("Password:"), gbc2);

        gbc2.gridy++;
        gbc2.fill = GridBagConstraints.HORIZONTAL;
        gbc2.weightx = 1.0;
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setPreferredSize(textFieldDimension);
        passwordPanel.add(passwordField, gbc2);
        centerPanel.add(passwordPanel);
        centerPanel.add(Box.createVerticalStrut(10));

        // Address-Field
        JPanel addressPanel = new JPanel(new GridBagLayout());
        addressPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        GridBagConstraints gbc3 = new GridBagConstraints();
        gbc3.gridx = 0;
        gbc3.gridy = 0;
        gbc3.anchor = GridBagConstraints.WEST;
        gbc3.insets = new Insets(0, 0, 5, 0);
        addressPanel.add(new JLabel("Address:"), gbc3);

        gbc3.gridy++;
        gbc3.fill = GridBagConstraints.HORIZONTAL;
        gbc3.weightx = 1.0;
        JTextField addressTextField = new JTextField(20);
        addressTextField.setPreferredSize(textFieldDimension);
        addressPanel.add(addressTextField, gbc3);
        centerPanel.add(addressPanel);
        centerPanel.add(Box.createVerticalStrut(10));


        // Buttons nebeneinander erstellen

        Dimension buttonDimension = new Dimension(200, 35);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton registerButton = new JButton("Register");
        JButton backButton = new JButton("Back");
        registerButton.setPreferredSize(buttonDimension);
        backButton.setPreferredSize(buttonDimension);
        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);
        centerPanel.add(buttonPanel);

        // Buttons Funktionen
        // Registration and login for customer
        registerButton.addActionListener(actionEvent -> {
            try {
                loginAndRegistrationViewModel.registerButtonClickHandler(userNameTextField.getText(), passwordField.getPassword(), addressTextField.getText(), registerChildPanel);
            } catch (CustomerNotFoundException exception) {
                logger.log(Level.SEVERE, "An Error has occurred: No customer with user id " + exception.getUserID() + " found!");
                System.out.println("Could not find user with id: " + exception.getUserID() + ".");
            }

            userNameTextField.setText("");
            passwordField.setText("");
            addressTextField.setText("");
        });

        // Back to log in
        backButton.addActionListener(actionEvent -> {
            if (backToLoginListener != null) {
                backToLoginListener.run();

                userNameTextField.setText("");
                passwordField.setText("");
                addressTextField.setText("");
            }
        });

        registerChildPanel.add(centerPanel);
        return registerChildPanel;
    }

    public void registrationButtonPressed(Runnable registrationButtonListener) {
        this.customerRegistrationListener = registrationButtonListener;
    }

    public void backToLoginButtonPressed(Runnable loggedInListener) {
        this.backToLoginListener = loggedInListener;
    }

}
