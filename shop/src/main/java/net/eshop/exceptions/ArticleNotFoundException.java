package net.eshop.exceptions;

/**
 * Thrown, when a specific {@link net.eshop.domain.Article} was not found by a {@link net.eshop.domain.dataaccess.DAO}.
 */
public class ArticleNotFoundException extends RuntimeException {
    public ArticleNotFoundException(String message) {
        super(message);
    }
}
