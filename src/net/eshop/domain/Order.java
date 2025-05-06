package net.eshop.domain;

import java.time.LocalDateTime;
import java.util.HashMap;

public class Order {

    private final int orderId;
    private final int userId;
    private final HashMap<Integer, Integer> articles;
    private final LocalDateTime localDateTime = LocalDateTime.now();

    public Order(int orderId, int userId, HashMap<Integer, Integer> articles) {
        this.orderId = orderId;
        this.userId = userId;
        this.articles = articles;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getUserId() {
        return userId;
    }

    public HashMap<Integer, Integer> getArticles() {
        return articles;
    }

}