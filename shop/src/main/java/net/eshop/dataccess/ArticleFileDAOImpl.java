package net.eshop.dataccess;

import net.eshop.domain.Article;
import net.eshop.exceptions.ArticleNotFoundException;

import java.io.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

class ArticleFileDAOImpl implements DAO<Article> {

    private static final Logger logger = Logger.getLogger(ArticleFileDAOImpl.class.getName());

    public static final String DATA_PATH = "Data";
    public static final String ARTICLE = "Articles";

    private final File file = new File(ARTICLE);

    public ArticleFileDAOImpl() {

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Error creating file \"" + ARTICLE + "\"", e);
            }
        }
    }

    @Override
    public void create(Article article) throws IOException {

        int articleNumber = article.getArticleNumber();

        if (containsArticle(articleNumber)) {
            logger.info(MessageFormat.format("Article with articleNumber {0} does already exist!", articleNumber));
            return;
        }

        try (FileWriter fileWriter = new FileWriter(file, true);
             BufferedWriter bufferedReader = new BufferedWriter(fileWriter)) {

            bufferedReader.write("\n");
            bufferedReader.write(articleNumber + ".id=" + articleNumber + "\n");
            bufferedReader.write(articleNumber + ".name=" + article.getName() + "\n");
            bufferedReader.write(articleNumber + ".description=" + article.getDescription() + "\n");
            bufferedReader.write(articleNumber + ".stock=" + article.getStock() + "\n");
        }
    }

    @Override
    public Article read(int id) throws IOException {

        if (!containsArticle(id))
            throw new ArticleNotFoundException(MessageFormat.format("No article with articleNumber {0}", id));

        return null;
    }

    @Override
    public void update(Article article) {

    }

    @Override
    public void delete(int id) {

    }

    private boolean containsArticle(int id) throws IOException {

        try (FileReader fileReader = new FileReader(file)) {

            BufferedReader bufferedReader = new BufferedReader(fileReader);
            List<String> duplicateArticles = new ArrayList<>();

            bufferedReader.lines().forEach(line -> {

                if (line.startsWith(String.valueOf(id) + "."))
                    duplicateArticles.add(line);
            });

            //if the list is not empty. a article is a duplicate
            return !duplicateArticles.isEmpty();
        }
    }
}
