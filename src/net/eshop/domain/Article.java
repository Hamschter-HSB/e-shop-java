package net.eshop.domain;

public class Article {

    private final String name;
    private final int articleNumber;
    private final String description;
    private int stock;

    public Article(String name, int articleNumber, String description, int stock) {
        this.name = name;
        this.articleNumber = articleNumber;
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