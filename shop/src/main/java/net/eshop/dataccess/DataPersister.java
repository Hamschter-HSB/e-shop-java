package net.eshop.dataccess;

import net.eshop.domain.Article;

import java.io.IOException;

public class DataPersister {

    public static final String DATA_PATH = "Data";
    public static final String ARTICLE = "Article";

    public void create(Article article) {

        try {
            articleDAO.create(article);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
    private final DAO<Article> articleDAO = new ArticleFileDAOImpl();
}
