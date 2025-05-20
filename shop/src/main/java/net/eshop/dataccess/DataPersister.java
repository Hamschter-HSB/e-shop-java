package net.eshop.dataccess;

import net.eshop.domain.Article;

import java.io.IOException;
import java.util.List;

public class DataPersister {

    private final DAO<Article> articleDAO = new ArticleFileDAOImpl();

    public void create(Article article) {

        try {
            articleDAO.create(article);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public Article read(int id) {
        try {
            return articleDAO.read(id);
        } catch (IOException ioException) {
            throw new RuntimeException(ioException.getMessage());
        }
    }

    public List<Article> readAll() {
        try {
            return articleDAO.readAll();
        } catch (IOException ioException) {
            throw new RuntimeException(ioException.getMessage());
        }
    }
}