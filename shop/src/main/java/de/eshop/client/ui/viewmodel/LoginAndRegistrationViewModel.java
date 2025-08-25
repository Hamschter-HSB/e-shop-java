package de.eshop.client.ui.viewmodel;

import de.eshop.client.dataaccess.ClientDataPersisterImpl;
import de.eshop.client.ui.DialogUtils;
import de.eshop.shared.domain.Customer;
import de.eshop.shared.domain.ShoppingBasket;
import de.eshop.shared.domain.StaffMember;
import de.eshop.shared.domain.exceptions.CustomerNotFoundException;
import de.eshop.client.hashing.PasswordHasher;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

public class LoginAndRegistrationViewModel {

    private static final Logger logger = Logger.getLogger(LoginAndRegistrationViewModel.class.getName());

    private final ClientDataPersisterImpl clientDataPersisterImpl;
    private Runnable loginListener;

    public LoginAndRegistrationViewModel(ClientDataPersisterImpl clientDataPersisterImpl) {
        this.clientDataPersisterImpl = clientDataPersisterImpl;
    }

    public void registerButtonClickHandler(String userName, char[] charPassword, String address, JPanel registrationMainPanel) throws CustomerNotFoundException {

        if (userName.isBlank() || charPassword.length == 0 || address.isBlank()) {

            DialogUtils.emptyInputFields(registrationMainPanel);
            logger.info("Registration failed for customer " + userName + ". Empty input field(s)");

            return;
        }

        String password = PasswordHasher.hashPassword(new String(charPassword));

        Customer customer = clientDataPersisterImpl.findUserByCredentials(userName, password, Customer.class);

        if (customer != null) {

            logger.info("Register process failed for customer with chosen name: " + userName);

            JOptionPane.showMessageDialog(registrationMainPanel,
                    "Username is already taken.",
                    "Unavailable Username",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<Customer> customers = clientDataPersisterImpl.readAllCustomers();
        int customerId = 1;

        try {
            customerId = customers.getLast().getNumber() + 1;
        } catch (NoSuchElementException ignored) {
            throw new CustomerNotFoundException("No customer with id " + (customerId) + " was found!", customerId);
        }

        Customer newCustomer = new Customer(customerId, userName, password, address, new ShoppingBasket());
        clientDataPersisterImpl.createCustomer(newCustomer);

        System.setProperty("CURRENT_USER", "CUSTOMER");
        System.setProperty("CURRENT_USER_ID", String.valueOf(newCustomer.getNumber()));
        loginCustomer(customerId);

        if (loginListener != null)
            loginListener.run();

        logger.info("Registered customer " + userName + " successfully.");
    }

    public void customerLoginButtonClickHandler(JTextField userNameField, JPasswordField passwordField, JPanel loginMainPanel) throws CustomerNotFoundException {

        char[] charPassword = passwordField.getPassword();
        String userName = userNameField.getText();

        if (userName.isBlank() || charPassword.length == 0) {

            DialogUtils.emptyInputFields(loginMainPanel);
            logger.info("Login failed for customer " + userName + ". Empty input field(s)");

            return;
        }

        String password = PasswordHasher.hashPassword(new String(charPassword));

        Customer customer = clientDataPersisterImpl.findUserByCredentials(userName, password, Customer.class);

        if (customer == null) {

            logger.info("Login failed for customer " + userName);

            JOptionPane.showMessageDialog(loginMainPanel,
                    "Username or password is incorrect.",
                    "Wrong credentials",
                    JOptionPane.WARNING_MESSAGE);

            return;
        }

        System.setProperty("CURRENT_USER", "CUSTOMER");
        System.setProperty("CURRENT_USER_ID", String.valueOf(customer.getNumber()));
        loginCustomer(customer.getNumber());

        if (loginListener != null)
            loginListener.run();

        userNameField.setText("");
        passwordField.setText("");

        logger.info("Login successfully for customer " + userName);
    }

    public void staffMemberLoginButtonClickHandler(JTextField userNameField, JPasswordField passwordField, JPanel loginMainPanel, JLabel employeeLogin) {
        employeeLogin.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                String userName = userNameField.getText();
                String password = PasswordHasher.hashPassword(new String(passwordField.getPassword()));

                if (userName.isBlank() || password.isBlank()) {

                    DialogUtils.emptyInputFields(loginMainPanel);
                    logger.info("Login failed for customer " + userName + ". Empty input field(s)");

                    return;
                }

                StaffMember staffMember = clientDataPersisterImpl.findUserByCredentials(userName, password, StaffMember.class);

                if (staffMember == null) {

                    logger.info("Login failed for staff member " + userName);

                    JOptionPane.showMessageDialog(loginMainPanel,
                            "Username or password is incorrect.",
                            "Wrong credentials",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                System.setProperty("CURRENT_USER", "STAFF_MEMBER");
                System.setProperty("CURRENT_USER_ID", String.valueOf(staffMember.getNumber()));

                if (loginListener != null)
                    loginListener.run();

                userNameField.setText("");
                passwordField.setText("");

                logger.info("Login successfully for staff member " + userName);
            }
        });
    }

    public void loginCustomer(int customerID) {
        clientDataPersisterImpl.setCurrentCustomer(customerID);
    }

    public void setLoggedIn(Runnable loginListener) {
        this.loginListener = loginListener;
    }
}
