package net.eshop.domain.dataaccess;

import net.eshop.domain.BulkArticle;
import net.eshop.exceptions.ArticleNotFoundException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

class BulkArticleFileDAOImpl implements DAO<BulkArticle> {

    private static final Logger logger = Logger.getLogger(BulkArticleFileDAOImpl.class.getName());

    private final String REG_EX = ".*=";

    public static final String DATA_PATH = "Data";
    public static final String BULK_ARTICLE = "Articles";

    private final File file = new File(BULK_ARTICLE);

    public BulkArticleFileDAOImpl() {

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Error creating file \"" + BULK_ARTICLE + "\"", e);
            }
        }
    }

    @Override
    public void create(BulkArticle bulkArticle) throws IOException {

        List<BulkArticle> bulkArticles = readAll();
        int id = 1;

        try {
            id = bulkArticles.getLast().getArticleNumber() + 1;
        } catch (NoSuchElementException ignored) {
        }

        try (FileWriter fileWriter = new FileWriter(file, true);
             BufferedWriter bufferedReader = new BufferedWriter(fileWriter)) {

            bufferedReader.write(id + ".id=" + id + "\n");
            bufferedReader.write(id + ".name=" + bulkArticle.getName() + "\n");
            bufferedReader.write(id + ".description=" + bulkArticle.getDescription() + "\n");
            bufferedReader.write(id + ".stock=" + bulkArticle.getStock() + "\n");
            bufferedReader.write(id + ".price=" + bulkArticle.getPrice() + "\n");
            bufferedReader.write(id + ".bulkSize=" + bulkArticle.getBulkSize() + "\n");
        }
    }

    @Override
    public BulkArticle read(int id) throws IOException {

        if (!containsBulkArticle(id))
            throw new ArticleNotFoundException(MessageFormat.format("No bulk article with articleNumber {0}", id));

        try (FileReader fileReader = new FileReader(file)) {

            BufferedReader bufferedReader = new BufferedReader(fileReader);
            List<String> duplicateArticles = new ArrayList<>();

            bufferedReader.lines().forEach(line -> {

                if (line.startsWith(id + "."))
                    duplicateArticles.add(line);
            });

            final String regEx = ".*=";

            int articleNumber = Integer.parseInt(duplicateArticles.get(0).replaceAll(regEx, ""));
            String articleName = duplicateArticles.get(1).replaceAll(regEx, "");
            String articleDescription = duplicateArticles.get(2).replaceAll(regEx, "");
            int stock = Integer.parseInt(duplicateArticles.get(3).replaceAll(regEx, ""));
            double price = Double.parseDouble(duplicateArticles.get(4).replaceAll(regEx, ""));
            int bulkSize = Integer.parseInt(duplicateArticles.get(5).replaceAll(regEx, ""));

            return new BulkArticle(articleNumber, articleName, articleDescription, stock, price, bulkSize);
        }
    }

    @Override
    public List<BulkArticle> readAll() throws IOException {

        List<BulkArticle> bulkArticles = new ArrayList<>();
        Set<Integer> bulkArticleIDS = new HashSet<>();

        try (FileReader fileReader = new FileReader(file)) {

            BufferedReader bufferedReader = new BufferedReader(fileReader);
            AtomicInteger counter = new AtomicInteger();

            bufferedReader.lines().forEach(line -> {
                if (counter.getAndIncrement() % 6 == 0) {
                    bulkArticleIDS.add(Integer.parseInt(line.replaceAll(REG_EX, "")));
                }
            });

            bulkArticleIDS.forEach(bulkArticleID -> {
                try {
                    bulkArticles.add(read(bulkArticleID));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        return bulkArticles;
    }

    @Override
    public void update(BulkArticle bulkArticle) throws IOException {

        delete(bulkArticle.getArticleNumber());
        create(bulkArticle);

    }

    @Override
    public void delete(int id) throws IOException {

        if (!containsBulkArticle(id)) {
            logger.info(MessageFormat.format("Article with articleNumber {0} does not exist!", id));
            return;
        }

        Path path = Paths.get(BULK_ARTICLE);
        List<String> allLines = Files.readAllLines(path);

        allLines.removeIf(line -> line.equals(id + ".id=" + id) || line.startsWith(id + ".name=") || line.startsWith(id + ".description=") || line.startsWith(id + ".stock=") || line.startsWith(id + ".price=") || line.startsWith(id + ".bulkSize="));

        Files.write(path, allLines);
    }

    private boolean containsBulkArticle(int id) throws IOException {

        try (FileReader fileReader = new FileReader(file)) {

            BufferedReader bufferedReader = new BufferedReader(fileReader);
            List<String> duplicateBulkArticles = new ArrayList<>();

            bufferedReader.lines().forEach(line -> {

                if (line.startsWith(id + "."))
                    duplicateBulkArticles.add(line);
            });

            //if the list is not empty. a bulk article is a duplicate
            return !duplicateBulkArticles.isEmpty();
        }
    }
}