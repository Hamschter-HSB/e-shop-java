package net.eshop.dataccess;

import net.eshop.dataccess.database.ArticleDBManager;
import net.eshop.domain.Article;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

class ArticleDBDAOImpl implements DAO<Article> {

    private static final String URL = "jdbc:sqlite:articles.db";
    private static final Connection CONNECTION;

    static {
        try {
            CONNECTION = DriverManager.getConnection(URL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private final ArticleDBManager articleDatabaseManager = new ArticleDBManager(CONNECTION);

    @Override
    public void create(Article article) throws IOException {
        articleDatabaseManager.createArticle(article);
    }

    @Override
    public Article read(int id) {
        return articleDatabaseManager.readArticle(id);
    }

    @Override
    public List<Article> readAll() {
        return List.of();
    }

    @Override
    public void update(Article article) {
        articleDatabaseManager.updateArticle(article);
    }

    @Override
    public void delete(int id) {

        try {
            articleDatabaseManager.deleteArticle(id);
        } catch (SQLException e) {
            Logger.getLogger("ArticleDBDAOImpl").log(Level.WARNING, "Failed to delete article with id {0}", id);
        }
    }
}
