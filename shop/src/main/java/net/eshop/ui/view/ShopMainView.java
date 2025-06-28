package net.eshop.ui.view;

import net.eshop.ui.events.RegistrationListener;
import net.eshop.ui.viewmodel.ShopMainViewModel;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

public class ShopMainView {

    private static final Logger logger = Logger.getLogger(ShopMainView.class.getName());

    private final ShopMainViewModel shopMainViewModel;

    public ShopMainView(ShopMainViewModel shopMainViewModel) {
        this.shopMainViewModel = shopMainViewModel;
    }

    public JPanel shop() {
        // Panels
        JPanel shopMainPanel = new JPanel(new BorderLayout());

        JPanel shopSidePanel = new JPanel(new BorderLayout());

        JPanel shopCenterPanel = new JPanel();

        // Content of shopMainPanel.NORTH
        JLabel shopLabel = new JLabel("Shop");
        JButton cartButton = new JButton("Cart");

        // Content of shopSidePanel
        JTextField searchTextField = new JTextField();
        JButton searchButton = new JButton("Search");

        JLabel hamschterLabel = new JLabel("Hamschter Inc.");
        JLabel logoutLabel = new JLabel("<HTML><U>Logout</U></HTML>");
        logoutLabel.setForeground(Color.BLUE);

        // Filling shopMainPanel
        shopMainPanel.add(shopLabel, BorderLayout.NORTH);
        shopMainPanel.add(cartButton, BorderLayout.NORTH);

        shopMainPanel.add(shopSidePanel, BorderLayout.WEST);
        shopMainPanel.add(shopCenterPanel, BorderLayout.CENTER);

        // Filling shopSidePanel
        shopSidePanel.add(searchTextField, BorderLayout.NORTH);
        shopSidePanel.add(searchButton, BorderLayout.NORTH);

        shopSidePanel.add(hamschterLabel, BorderLayout.SOUTH);
        shopSidePanel.add(logoutLabel, BorderLayout.SOUTH);

        shopMainPanel.setVisible(true);

        return shopMainPanel;
    }

    public JPanel staffMemberRegistration() {

        JPanel staffMemberRegistrationMainPanel = new JPanel(new GridBagLayout());
        staffMemberRegistrationMainPanel.setPreferredSize(new Dimension(350, 200));

        JPanel staffMemberRegisterChildPanel = new JPanel();
        staffMemberRegisterChildPanel.setPreferredSize(new Dimension(350, 200));
        staffMemberRegisterChildPanel.setLayout(new BoxLayout(staffMemberRegisterChildPanel, BoxLayout.Y_AXIS));

        JTextField userNameTextField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        JButton backButton = new JButton("Back");
        JButton registerButton = new JButton("Register");
        Dimension buttonDimension = new Dimension(200, 35);

        // Registration for customer
        registerButton.addActionListener(actionEvent -> shopMainViewModel.registerButtonClickHandler(userNameTextField.getText(), passwordField.getPassword(), staffMemberRegistrationMainPanel));

        // Back to Menu
        backButton.addActionListener(actionEvent -> {
            //TODO WEITER
            staffMemberRegisterChildPanel.setVisible(false);
        });

        registerButton.setPreferredSize(buttonDimension);
        backButton.setPreferredSize(buttonDimension);
        registerButton.setMaximumSize(buttonDimension);
        backButton.setMaximumSize(buttonDimension);

        staffMemberRegisterChildPanel.add(Box.createVerticalStrut(10));
        staffMemberRegisterChildPanel.add(userNameTextField);
        staffMemberRegisterChildPanel.add(Box.createVerticalStrut(10));
        staffMemberRegisterChildPanel.add(passwordField);
        staffMemberRegisterChildPanel.add(Box.createVerticalStrut(10));
        staffMemberRegisterChildPanel.add(registerButton);
        staffMemberRegisterChildPanel.add(Box.createVerticalStrut(10));
        staffMemberRegisterChildPanel.add(backButton);
        staffMemberRegisterChildPanel.add(Box.createVerticalStrut(10));

        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        staffMemberRegistrationMainPanel.add(staffMemberRegisterChildPanel);

        staffMemberRegistrationMainPanel.setVisible(true);

        return staffMemberRegistrationMainPanel;
    }
}
