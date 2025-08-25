package de.eshop.shared.domain.exceptions;

import de.eshop.shared.dataaccess.DAO;
import de.eshop.shared.domain.User;

/**
 * Thrown, when a specific {@link User} was not found by a {@link DAO}.
 */
public class UserNotFoundException extends Exception {

    private final int userID;

    public UserNotFoundException(String message, int userID) {
        super(message);
        this.userID = userID;
    }

    public int getUserID() {
        return userID;
    }
}
