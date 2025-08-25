package de.eshop.shared.domain.exceptions;

public class StaffMemberNotFoundException extends UserNotFoundException {

    public StaffMemberNotFoundException(String message, int userID) {
        super(message, userID);
    }
}
