package net.eshop.exceptions;

/**
 * Thrown, when a specific {@link net.eshop.domain.User} was not found by a {@link net.eshop.dataccess.DAO}.
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
