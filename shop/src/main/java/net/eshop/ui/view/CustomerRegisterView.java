package net.eshop.ui.view;

import net.eshop.ui.viewmodel.CustomerRegistrationViewModel;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

public class CustomerRegisterView {

    private static final Logger logger = Logger.getLogger(CustomerRegisterView.class.getName());

    private final CustomerRegistrationViewModel customerRegistrationViewModel;

    public CustomerRegisterView(CustomerRegistrationViewModel customerRegistrationViewModel) {
        this.customerRegistrationViewModel = customerRegistrationViewModel;
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

        JButton registerButton = new JButton("Register");
        JButton backButton = new JButton("Back");
        Dimension buttonDimension = new Dimension(200, 35);

        // Login for customer
        registerButton.addActionListener(actionEvent -> customerRegistrationViewModel.registerButtonClickHandler(userNameTextField.getText(), passwordField.getPassword(), registrationMainPanel, addressTextField.getText()));

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
        registerChildPanel.add(backButton);
        registerChildPanel.add(Box.createVerticalStrut(10));
        registerChildPanel.add(registerButton);
        registerChildPanel.add(Box.createVerticalStrut(10));

        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        registrationMainPanel.add(registerChildPanel);

        return registrationMainPanel;
    }
}
