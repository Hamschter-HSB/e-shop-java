package net.eshop.dataccess;

import net.eshop.domain.Article;

public class DataPersister {

    private final DAO<Article> articleDAO = new ArticleDAOImpl();

    public void create(Article article) {
        articleDAO.create(article);
    }
}
