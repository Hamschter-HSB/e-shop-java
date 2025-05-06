package net.eshop.domain;

import java.util.Collections;
import java.util.List;

public class ShoppingBasket {

    private final List<Article> articleList;

    public ShoppingBasket(List<Article> articleList) {
        this.articleList = articleList;
    }

    public void emptyBasket() {
        articleList.clear();
    }

    public List<Article> getArticleList() {
        return Collections.unmodifiableList(articleList);
    }
}