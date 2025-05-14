package net.eshop.domain;

public class Article {

    private final int articleNumber;
    private final String name;
    private final String description;
    private int stock;

    public Article(int articleNumber, String name, String description, int stock) {
        this.articleNumber = articleNumber;
        this.name = name;
        this.description = description;
        this.stock = stock;
    }

    public int getArticleNumber() {
        return articleNumber;
    }

    public int getStock() {
        return stock;
    }

    public String getName() {
        return name;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }
}