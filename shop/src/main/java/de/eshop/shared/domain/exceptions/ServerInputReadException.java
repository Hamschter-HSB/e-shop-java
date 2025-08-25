package de.eshop.shared.domain.exceptions;

/**
 * Thrown, when a clients input wasn't from a server.
 */
public class ServerInputReadException extends RuntimeException {

    public ServerInputReadException(String message) {
        super(message);
    }
}
