package de.eshop.shared.domain.exceptions;

import de.eshop.shared.domain.Article;

/**
 * Thrown, when a specific {@link Article} ran out of stock
 */
public class ArticleOutOfStockException extends Exception {

    private final String articleNameAndNumber;

    public ArticleOutOfStockException(String message, String articleNameAndNumber) {
        super(message);
        this.articleNameAndNumber = articleNameAndNumber;
    }

    public String getArticleNameAndNumber() {
        return articleNameAndNumber;
    }
}
