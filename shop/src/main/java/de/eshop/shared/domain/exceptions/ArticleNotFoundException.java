package de.eshop.shared.domain.exceptions;

import de.eshop.shared.dataaccess.DAO;
import de.eshop.shared.domain.Article;

/**
 * Thrown, when a specific {@link Article} was not found by a {@link DAO}
 */
public class ArticleNotFoundException extends Exception {

    private final int articleNumber;

    public ArticleNotFoundException(String message, int articleNumber) {
        super(message);
        this.articleNumber = articleNumber;
    }

    public int getArticleNumber() {
        return articleNumber;
    }
}
