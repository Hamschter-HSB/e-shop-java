package net.eshop.dataccess;

import net.eshop.domain.Article;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

class ArticleDAOImpl implements DAO<Article> {

    @Override
    public void create(Article article) throws IOException {

        File file = new File(DataPersister.ARTICLE);

        if (!file.exists())
            file.createNewFile();

        try (FileWriter fileWriter = new FileWriter(file);
             BufferedWriter bufferedReader = new BufferedWriter(fileWriter)) {

            bufferedReader.write(article.getArticleNumber() + "\n");
            bufferedReader.write(article.getName() + "\n");
            bufferedReader.write(article.getDescription() + "\n");
            bufferedReader.write(article.getStock() + "\n");
        }
    }

    @Override
    public void read(int id) {

    }

    @Override
    public void update(int id) {

    }

    @Override
    public void delete(int id) {

    }
}
