package net.eshop.ui.viewmodel;

import net.eshop.domain.Customer;
import net.eshop.domain.ShoppingBasket;
import net.eshop.domain.StaffMember;
import net.eshop.domain.dataaccess.DataPersister;
import net.eshop.ui.DialogUtils;
import net.eshop.ui.events.LoginListener;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

public class LoginAndRegistrationViewModel {

    private static final Logger logger = Logger.getLogger(LoginAndRegistrationViewModel.class.getName());

    private final DataPersister dataPersister;
    private LoginListener loginListener;

    public LoginAndRegistrationViewModel(DataPersister dataPersister) {
        this.dataPersister = dataPersister;
    }

    public void registerButtonClickHandler(String userName, char[] charPassword, String address, JPanel registrationMainPanel) {

        if (userName.isBlank() || charPassword.length == 0 || address.isBlank()) {

            DialogUtils.emptyInputFields(registrationMainPanel);
            logger.info("Registration failed for customer " + userName + ". Empty input field(s)");

            return;
        }

        String password = new String(charPassword);

        Customer customer = dataPersister.findUserByCredentials(userName, password, Customer.class);

        if (customer != null) {

            logger.info("Register process failed for customer with chosen name: " + userName);

            JOptionPane.showMessageDialog(registrationMainPanel,
                    "Username is already taken.",
                    "Unavailable Username",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<Customer> customers = dataPersister.readAllCustomers();
        int customerId = 1;

        try {
            customerId = customers.getLast().getNumber() + 1;
        } catch (NoSuchElementException ignored) {
        }

        Customer newCustomer = new Customer(customerId, userName, password, address, new ShoppingBasket(new HashMap<>()));
        dataPersister.createCustomer(newCustomer);

        registrationMainPanel.setVisible(false);

        System.setProperty("CURRENT_USER", "CUSTOMER");
        System.setProperty("CURRENT_USER_ID", String.valueOf(newCustomer.getNumber()));

        logger.info("Registered customer " + userName + " successfully.");
    }

    public void customerLoginButtonClickHandler(String userName, char[] charPassword, JPanel loginMainPanel) {

        if (userName.isBlank() || charPassword.length == 0) {

            DialogUtils.emptyInputFields(loginMainPanel);
            logger.info("Login failed for customer " + userName + ". Empty input field(s)");

            return;
        }

        String password = new String(charPassword);

        Customer customer = dataPersister.findUserByCredentials(userName, password, Customer.class);

        if (customer == null) {

            logger.info("Login failed for customer " + userName);

            JOptionPane.showMessageDialog(loginMainPanel,
                    "Username or password is incorrect.",
                    "Wrong credentials",
                    JOptionPane.WARNING_MESSAGE);

            return;
        }

        loginMainPanel.setVisible(false);

        System.setProperty("CURRENT_USER", "CUSTOMER");
        System.setProperty("CURRENT_USER_ID", String.valueOf(customer.getNumber()));

        if (loginListener != null)
            loginListener.onLoginSuccess();

        logger.info("Login successfully for customer " + userName);
    }

    public void staffMemberLoginButtonClickHandler(JTextField userNameField, JPasswordField passwordField, JPanel loginMainPanel, JLabel employeeLogin) {
        employeeLogin.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                String userName = userNameField.getText();
                String password = new String(passwordField.getPassword());

                if (userName.isBlank() || password.isBlank()) {

                    DialogUtils.emptyInputFields(loginMainPanel);
                    logger.info("Login failed for customer " + userName + ". Empty input field(s)");

                    return;
                }

                StaffMember staffMember = dataPersister.findUserByCredentials(userName, password, StaffMember.class);

                if (staffMember == null) {

                    logger.info("Login failed for staff member " + userName);

                    JOptionPane.showMessageDialog(loginMainPanel,
                            "Username or password is incorrect.",
                            "Wrong credentials",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                loginMainPanel.setVisible(false);

                System.setProperty("CURRENT_USER", "STAFF_MEMBER");
                System.setProperty("CURRENT_USER_ID", String.valueOf(staffMember.getNumber()));

                if (loginListener != null)
                    loginListener.onLoginSuccess();

                logger.info("Login successfully for staff member " + userName);
            }
        });
    }

    public void setLoggedIn(LoginListener loginListener) {
        this.loginListener = loginListener;
    }
}
