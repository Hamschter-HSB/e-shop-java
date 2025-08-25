package de.eshop.shared.domain.exceptions;

public class CustomerNotFoundException extends UserNotFoundException {

    public CustomerNotFoundException(String message, int userID) {
        super(message, userID);
    }
}
