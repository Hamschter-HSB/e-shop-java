package net.eshop.dataccess;

import net.eshop.domain.Article;

import java.io.IOException;

public class DataPersister {

    private final DAO<Article> articleDAO = new ArticleFileDAOImpl();

    public void create(Article article) {

        try {
            articleDAO.create(article);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
