package net.eshop.dataccess;

import net.eshop.domain.Article;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

class ArticleFileDAOImpl implements DAO<Article> {

    @Override
    public void create(Article article) throws IOException {

        File file = new File(DataPersister.ARTICLE);

        try (FileWriter fileWriter = new FileWriter(file, true);
             BufferedWriter bufferedReader = new BufferedWriter(fileWriter)) {

            bufferedReader.write("\n");
            bufferedReader.write(article.getArticleNumber() + "\n");
            bufferedReader.write(article.getName() + "\n");
            bufferedReader.write(article.getDescription() + "\n");
            bufferedReader.write(article.getStock() + "\n");
        }
    }

    @Override
    public Article read(int id) {
        return null;
    }

    @Override
    public void update(Article article) {

    }

    @Override
    public void delete(int id) {

    }
}
