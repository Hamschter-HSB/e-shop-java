package net.eshop.ui.view;

import net.eshop.domain.Customer;
import net.eshop.domain.StaffMember;
import net.eshop.domain.dataaccess.DataPersister;
import net.eshop.ui.viewmodel.CustomerRegistrationViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;

public class LoginView {

    private static final Logger logger = Logger.getLogger(LoginView.class.getName());

    private final DataPersister dataPersister;
    private final JPanel customerRegistrationPanel;

    public LoginView(DataPersister dataPersister) {
        this.dataPersister = dataPersister;
        this.customerRegistrationPanel = new CustomerRegisterView(new CustomerRegistrationViewModel(dataPersister)).customerRegistration();
    }

    public JPanel loginAndRegister() {

        JPanel loginMainPanel = new JPanel(new GridBagLayout());
        loginMainPanel.setPreferredSize(new Dimension(350, 200));

        JPanel loginChildPanel = new JPanel();
        loginChildPanel.setPreferredSize(new Dimension(350, 200));
        loginChildPanel.setLayout(new BoxLayout(loginChildPanel, BoxLayout.Y_AXIS));

        JTextField userNameTextField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        JButton customerLoginButton = new JButton("Login");
        JButton customerRegisterButton = new JButton("Register");
        Dimension buttonDimension = new Dimension(200, 35);

        // Login for customer
        customerLoginButton.addActionListener(actionEvent -> {

            String password = getPasswordStringFromPasswordField(passwordField.getPassword());

            Customer customer = dataPersister.findUserByCredentials(userNameTextField.getText(), password, Customer.class);

            if (customer == null) {

                logger.info("Login failed for customer " + userNameTextField.getText());

                JOptionPane.showMessageDialog(loginMainPanel,
                        "Username or password is incorrect.",
                        "Wrong credentials",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            loginMainPanel.setVisible(false);

            System.setProperty("CURRENT_USER", "CUSTOMER");
            System.setProperty("CURRENT_USER_ID", String.valueOf(customer.getNumber()));

            logger.info("Login successfully for customer " + userNameTextField.getText());
        });

        customerRegisterButton.addActionListener(actionEvent -> {
            loginChildPanel.setVisible(false);
            loginMainPanel.add(customerRegistrationPanel);
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
        loginChildPanel.add(customerRegisterButton);
        loginChildPanel.add(Box.createVerticalStrut(10));
        loginChildPanel.add(customerLoginButton);
        loginChildPanel.add(Box.createVerticalStrut(10));

        JLabel employeeLogin = new JLabel("<HTML><U>Employee login</U></HTML>", SwingConstants.CENTER);
        employeeLogin.setForeground(Color.BLUE);
        employeeLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        employeeLogin.setAlignmentX(Component.RIGHT_ALIGNMENT);
        loginChildPanel.add(employeeLogin);

        //login for staff member
        employeeLogin.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                String password = getPasswordStringFromPasswordField(passwordField.getPassword());

                StaffMember staffMember = dataPersister.findUserByCredentials(userNameTextField.getText(), password, StaffMember.class);

                if (staffMember == null) {

                    logger.info("Login failed for staff member " + userNameTextField.getText());

                    JOptionPane.showMessageDialog(loginMainPanel,
                            "Username or password is incorrect.",
                            "Wrong credentials",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                loginMainPanel.setVisible(false);

                System.setProperty("CURRENT_USER", "STAFF_MEMBER");
                System.setProperty("CURRENT_USER_ID", String.valueOf(staffMember.getNumber()));

                logger.info("Login successfully for staff member " + userNameTextField.getText());
            }
        });

        customerRegisterButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        customerLoginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        employeeLogin.setAlignmentX(Component.CENTER_ALIGNMENT);

        loginMainPanel.add(loginChildPanel);

        return loginMainPanel;
    }

    //TODO move to ViewModels
    protected static String getPasswordStringFromPasswordField(char[] password) {

        StringBuilder stringBuilder = new StringBuilder();

        for (char c : password)
            stringBuilder.append(c);

        return stringBuilder.toString();
    }
}
