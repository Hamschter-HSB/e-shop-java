package net.eshop.domain;

public class Article {

    private final int articleNumber;
    private final String name;
    private final String description;
    private int stock;
    private final double price;

    public Article(int articleNumber, String name, String description, int stock, double price) {
        this.articleNumber = articleNumber;
        this.name = name;
        this.description = description;
        this.stock = stock;
        this.price = price;
    }

    public int getArticleNumber() {
        return articleNumber;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getPrice() {
        return price;
    }
}