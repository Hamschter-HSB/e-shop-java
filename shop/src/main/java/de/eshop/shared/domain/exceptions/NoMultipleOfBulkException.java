package de.eshop.shared.domain.exceptions;

import de.eshop.shared.domain.BulkArticle;

/**
 * Thrown, when a specific {@link BulkArticle} was not selected as a valid bulk
 * <h6>Example:</h6> Article named "test" has a bulkSize of 6, but a customer tries to buy 7.
 */
public class NoMultipleOfBulkException extends Exception {

    private final int articleNumber;
    private final int actualBulkSize;
    private final int stockSize;

    public NoMultipleOfBulkException(String message, int articleNumber, int actualBulkSize, int stockSize) {
        super(message);
        this.articleNumber = articleNumber;
        this.actualBulkSize = actualBulkSize;
        this.stockSize = stockSize;
    }

    public int getArticleNumber() {
        return articleNumber;
    }

    public int getActualBulkSize() {
        return actualBulkSize;
    }

    public int getStockSize() {
        return stockSize;
    }
}
