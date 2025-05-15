package net.eshop.dataccess;

import net.eshop.dataccess.database.ArticleDBManager;
import net.eshop.domain.Article;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

class ArticleDBDAOImpl implements DAO<Article> {

    private ArticleDBManager articleDatabaseManager;

    @Override
    public void create(Article article) throws IOException {
        articleDatabaseManager.createArticle(article);
    }

    @Override
    public Article read(int id) {
        return articleDatabaseManager.readArticle(id);
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
