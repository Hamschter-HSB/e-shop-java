package net.eshop.domain;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ShoppingBasket {

    private Map<Integer, Integer> article;

    public ShoppingBasket(HashMap<Integer, Integer> article) {
        this.article = article;
    }

    public void clear() {
        article.clear();
    }

    public Map<Integer, Integer> getArticleMap() {
        return Collections.unmodifiableMap(article);
    }

    public void addToArticleMap(Integer article, int amount) {
        this.article.put(article, amount);
    }

    public void removeFromArticleMap(Integer article) {
        this.article.remove(article);
    }
}