package de.eshop.shared.domain;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ShoppingBasket {

    private final Map<Integer, Integer> article;

    public ShoppingBasket() {
        this.article = new HashMap<>();
    }

    public void clear() {
        article.clear();
    }

    public Map<Integer, Integer> getArticleMap() {
        return Collections.unmodifiableMap(article);
    }

    public void addToArticleMap(int article, int amount) {
        this.article.put(article, amount);
    }

    public void removeFromArticleMap(int article) {
        this.article.remove(article);
    }
}