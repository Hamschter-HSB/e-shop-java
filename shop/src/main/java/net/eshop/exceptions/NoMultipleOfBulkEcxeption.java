package net.eshop.exceptions;

/**
 * Thrown, when a specific {@link net.eshop.domain.BulkArticle} was not selected as a valid bulk
 * <h6>Example:</h6> Article named "test" has a bulkSize of 6, but a customer tries to buy 7.
 */
public class NoMultipleOfBulkEcxeption extends RuntimeException {
    public NoMultipleOfBulkEcxeption(String message) {
        super(message);
    }
}
