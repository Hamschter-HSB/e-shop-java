package net.eshop.domain;

public class BulkArticle extends Article {

    private final int bulkSize;

    public BulkArticle(int articleNumber, String name, String description, int stock, double price, int bulkSize) {
        super(articleNumber, name, description, stock, price);
        this.bulkSize = bulkSize;
    }

    public int getBulkSize() {
        return bulkSize;
    }
}
