package net.eshop.dataccess.database;

import net.eshop.domain.Article;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArticleDBManager {

    private final Connection connection;

    public ArticleDBManager(Connection connection) {
        this.connection = connection;
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        try {
            Statement statement = connection.createStatement();

            String sql = "CREATE TABLE IF NOT EXISTS articles ("
                + "articleNumber INTEGER PRIMARY KEY,"
                + "name TEXT NOT NULL,"
                + "description TEXT NOT NULL,"
                + "stock INTEGER NOT NULL)";

            statement.execute(sql);
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Article> readArticles() throws SQLException {

        List<Article> articles = new ArrayList<>();

        try (
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT articleNumber, name, description, stock FROM articles");
        ) {

            while (rs.next()) {
                // DEBUG ONLY
                System.out.println("---- DEBUG ------\n");
                // DEBUG ONLY
                int articleNumber = rs.getInt("articleNumber");
                String name = rs.getString("name");
                String description = rs.getString("description");
                int stock = rs.getInt("stock");

                Article article = new Article(articleNumber, name, description, stock);

                // DEBUG ONLY
                System.out.println("ID: " + articleNumber + "\nName: " + name + "\nDesc: " + description + "\nStock: " + stock);
                // DEBUG ONLY
                articles.add(article);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Loggen, Fehler anzeigen
        }

        return articles;
    }

    public Article readArticle(int articleNumber) {

        try (
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT articleNumber, name, description, stock FROM articles WHERE articleNumber = " + articleNumber);
        ) {

            while (rs.next()) {
                int articleNumberByDB = rs.getInt("articleNumber");
                int stock = rs.getInt("stock");
                String name = rs.getString("name");
                String description = rs.getString("description");

                if (articleNumber == articleNumberByDB) {
                    return new Article(articleNumber, name, description, stock);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Loggen, Fehler anzeigen
        }

        return null;
    }

    public boolean createArticle(Article article) {

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO articles (name, description, stock) " +
                    "VALUES (?, ?, ?)");

            preparedStatement.setString(1, article.getName());
            preparedStatement.setString(2, article.getDescription());
            preparedStatement.setInt(3, article.getStock());

            preparedStatement.executeUpdate();
            preparedStatement.close();

            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public void updateArticle(Article article) {

        try {

            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE articles SET name = ?, description = ?, stock = ? WHERE articleNumber = ?");
            preparedStatement.setString(1, article.getName());
            preparedStatement.setString(2, article.getDescription());
            preparedStatement.setInt(3, article.getStock());
            preparedStatement.setInt(4, article.getArticleNumber());

            preparedStatement.executeUpdate();
            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void deleteArticle(int articleNumber) throws SQLException {

        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM articles WHERE articleNumber = ?");
        preparedStatement.setInt(1, articleNumber);

        preparedStatement.executeUpdate();
        preparedStatement.close();

    }

}