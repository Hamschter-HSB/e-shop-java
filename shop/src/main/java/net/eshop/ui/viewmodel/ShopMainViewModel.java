package net.eshop.ui.viewmodel;

import net.eshop.domain.Customer;
import net.eshop.domain.StaffMember;
import net.eshop.domain.dataaccess.DataPersister;
import net.eshop.ui.DialogUtils;
import net.eshop.ui.events.LogoutListener;
import net.eshop.ui.events.RegistrationListener;

import javax.swing.*;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

public class ShopMainViewModel {

    private static final Logger logger = Logger.getLogger(ShopMainViewModel.class.getName());

    private final DataPersister dataPersister;
    private RegistrationListener registrationListener;
    private LogoutListener logoutListener;

    public ShopMainViewModel(DataPersister dataPersister) {
        this.dataPersister = dataPersister;
    }

    public void registerButtonClickHandler(String userName, char[] charPassword, JPanel registrationMainPanel) {

        if (userName.isBlank() || charPassword.length == 0) {
            DialogUtils.emptyInputFields(registrationMainPanel);
            logger.info("Registration failed for staff member " + userName + ". Empty input field(s)");
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

        List<StaffMember> staffMembers = dataPersister.readAllStaffMembers();
        int staffMemberID = 1;

        try {
            staffMemberID = staffMembers.getLast().getNumber() + 1;
        } catch (NoSuchElementException ignored) {
        }

        StaffMember newStaffMember = new StaffMember(staffMemberID, userName, password);
        dataPersister.createStaffMember(newStaffMember);

        registrationMainPanel.setVisible(false);

        if (registrationListener != null)
            registrationListener.onRegistrationSuccess();

        logger.info("Registered customer " + userName + " successfully.");
    }

    public void logOutCurrentUser() {
        if (logoutListener != null) {
            logoutListener.onLogoutSuccess();

            System.clearProperty("CURRENT_USER");
            System.clearProperty("CURRENT_USER_ID");

            logger.info("Successfully logged out user");
        }
    }

    public void setRegisteredStaffMember(RegistrationListener registrationListener) {
        this.registrationListener = registrationListener;
    }

    public void setLogoutListener(LogoutListener logoutListener) {
        this.logoutListener = logoutListener;
    }
}
