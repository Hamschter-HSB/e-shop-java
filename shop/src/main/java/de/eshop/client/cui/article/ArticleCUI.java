package de.eshop.client.cui.article;

import de.eshop.client.cui.CUIManager;
import de.eshop.client.dataaccess.ClientDataPersisterImpl;
import de.eshop.shared.domain.Article;
import de.eshop.shared.domain.BulkArticle;
import de.eshop.shared.domain.UserType;
import de.eshop.shared.domain.events.StockChange;
import de.eshop.shared.domain.exceptions.ArticleNotFoundException;
import de.eshop.shared.domain.exceptions.NoMultipleOfBulkException;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ArticleCUI {

    private static final Logger logger = Logger.getLogger(ArticleCUI.class.getName());

    private final Scanner scanner = new Scanner(System.in);

    private final CUIManager cuiManager;
    private final ClientDataPersisterImpl clientDataPersisterImpl;

    public ArticleCUI(CUIManager cuiManager, ClientDataPersisterImpl clientDataPersisterImpl) {
        this.cuiManager = cuiManager;
        this.clientDataPersisterImpl = clientDataPersisterImpl;
    }

    public void printSubMenu() {

        System.out.println("-----E-Shop/Article-----");
        System.out.println("1. Show all articles");
        System.out.println("2. Manage articles");
        System.out.println("3. Back");

        if (scanner.hasNextInt()) {
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    readAllArticles();
                    printSubMenu();
                case 2:
                    printArticleManagementMenu();
                    break;
                case 3:
                    cuiManager.printMainMenu();
            }
        }

    }

    private void readAllArticles() {

        System.out.println("-----E-Shop/Article/Management/Manage articles-----");

        List<BulkArticle> bulkArticles = clientDataPersisterImpl.readAllBulkArticles();
        bulkArticles.forEach(article -> {
            printArticle(article);
            System.out.println();
        });

        System.out.println("-----------------------");
        System.out.println();

    }

    public void printArticleManagementMenu() {

        System.out.println("-----E-Shop/Articles/Management-----");
        System.out.println("1. Add article");
        System.out.println("2. Show all articles");
        System.out.println("3. Find article");
        System.out.println("4. Remove article");
        System.out.println("5. Edit stock");
        System.out.println("6. Back");

        if (scanner.hasNextInt()) {
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    addArticle();
                case 2:
                    readAllArticles();
                    printArticleManagementMenu();
                case 3:
                    readArticle();
                case 4:
                    try {
                        removeArticle();
                    } catch (ArticleNotFoundException exception) {
                        logger.log(Level.SEVERE, "An Error has occurred: Article " + exception.getArticleNumber() + " was not found!");
                        System.out.println("Could not find article with number: " + exception.getArticleNumber() + ".");
                    }
                case 5:
                    editArticle();
                case 6:
                    printSubMenu();
                    break;
            }
        }
    }

    public void browseArticlesMenu() {

        System.out.println("-----E-Shop/Articles/Browse-----");
        System.out.println("1. Add article");
        System.out.println("2. Back");

        if (scanner.hasNextInt()) {
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    try {
                        addArticleToShoppingBasket();
                    } catch (NoMultipleOfBulkException exception) {
                        logger.log(Level.SEVERE, "An Error has occurred: Bulk size for article " + exception.getArticleNumber() + " is " + exception.getActualBulkSize() + ", which is not a multiple of " + exception.getStockSize() + "!");
                        System.out.println("Bulk size for article " + exception.getArticleNumber() + " is " + exception.getActualBulkSize() + ", which is not a multiple of " + exception.getStockSize() + "!");
                    }
                case 2:
                    cuiManager.printMainMenu();
                    break;
            }
        }
    }

    private void addArticle() {

        System.out.println("-----E-Shop/Article/Management/Add article-----");
        System.out.println("Write: [\"articleNumber\" \"name\" \"description\" \"stock\" \"price\" \"bulk size\"] to add an article.");

        int articleNumber = scanner.nextInt();
        String name = scanner.next();
        String description = scanner.next();
        int stock = scanner.nextInt();
        double price = scanner.nextDouble();

        int bulkSize = scanner.nextInt();

        if (bulkSize > 0) {
            while (stock % bulkSize != 0) {
                System.out.println(name + " has a stock of " + stock + ". This is not a multiple of " + bulkSize);
                System.out.println("Enter the new stock of the bulk article.");
                stock = scanner.nextInt();
            }
        }

        BulkArticle bulkArticle = new BulkArticle(articleNumber, name, description, stock, price, bulkSize);
        clientDataPersisterImpl.createBulkArticle(bulkArticle);

        Random random = new Random();
        int id = random.nextInt(10000 - 1) + 1;
        UserType userType = UserType.valueOf(System.getProperty("CURRENT_USER"));
        StockChange stockChange = new StockChange(id, LocalDateTime.now().getDayOfYear(), articleNumber, 0, bulkArticle.getStock(), Integer.parseInt(System.getProperty("CURRENT_USER_ID")), userType);
        clientDataPersisterImpl.createStockChange(stockChange);

        printArticleManagementMenu();
    }

    private void removeArticle() throws ArticleNotFoundException {

        System.out.println("-----E-Shop/Article/Management/Remove article by ID-----");
        System.out.println("Write: [\"articleNumber\"] to remove an article.");

        int articleNumber = scanner.nextInt();

        BulkArticle bulkArticle = clientDataPersisterImpl.readBulkArticle(articleNumber);
        if (bulkArticle == null)
            throw new ArticleNotFoundException("Error! No article with id " + articleNumber + " found.", articleNumber);

        clientDataPersisterImpl.deleteBulkArticle(articleNumber);

        System.out.println("Removed article with ID " + articleNumber + ".");
    }

    private void editArticle() {

        System.out.println("-----E-Shop/Article/Management/Edit article by ID-----");
        System.out.println("Write: [\"articleNumber\"] to edit an article.");

        int articleNumber = scanner.nextInt();

        System.out.println("-----------------------------");
        System.out.println("-----E-Shop/Article/Management/Edit article with ID: " + articleNumber);
        System.out.println("1. Change Name");
        System.out.println("2. Change Description");
        System.out.println("3. Change Stock");
        System.out.println("4. Back");

        if (scanner.hasNextInt()) {
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    logger.info("Changing article namess is currently not implemented.");
                    //changeName();
                    editArticle();
                case 2:
                    //changeDesc();
                    logger.info("Changing article descriptions is currently not implemented.");
                    editArticle();
                case 3:
                    try {
                        changeStock();
                    } catch (NoMultipleOfBulkException exception) {
                        logger.log(Level.SEVERE, "An Error has occurred: Bulk size for article " + exception.getArticleNumber() + " is " + exception.getActualBulkSize() + ", which is not a multiple of " + exception.getStockSize() + "!");
                        System.out.println("Bulk size for article " + exception.getArticleNumber() + " is " + exception.getActualBulkSize() + ", which is not a multiple of " + exception.getStockSize() + "!");
                    }
                case 4:
                    printArticleManagementMenu();
            }
        }
    }

    private void changeStock() throws NoMultipleOfBulkException {

        System.out.println("-----E-Shop/Article/Management/Edit stock-----");

        System.out.println("Write the article number you want to edit the article stock for.");
        int id = scanner.nextInt();

        System.out.println("Write the new stock amount.");
        int stock = scanner.nextInt();

        BulkArticle bulkArticle = clientDataPersisterImpl.readBulkArticle(id);
        int oldStock = bulkArticle.getStock();

        boolean isMultiple = true;

        if (bulkArticle.getBulkSize() > 0)
            isMultiple = stock % bulkArticle.getBulkSize() == 0;

        if (isMultiple)
            bulkArticle.setStock(stock);
        else {
            int actualBulkSize = bulkArticle.getBulkSize();
            throw new NoMultipleOfBulkException(MessageFormat.format("Error: Article amount must be a multiple of {0}", actualBulkSize), bulkArticle.getArticleNumber(), actualBulkSize, stock);
        }

        clientDataPersisterImpl.updateBulkArticle(bulkArticle);

        Random random = new Random();
        int stockChangeID = random.nextInt(10000 - 1) + 1;
        UserType userType = UserType.valueOf(System.getProperty("CURRENT_USER"));
        StockChange stockChange = new StockChange(stockChangeID, LocalDateTime.now().getDayOfYear(), id, oldStock, bulkArticle.getStock(), Integer.parseInt(System.getProperty("CURRENT_USER_ID")), userType);
        clientDataPersisterImpl.createStockChange(stockChange);
    }

    private void readArticle() {

        System.out.println("-----E-Shop/Article/Management/Find article-----");
        System.out.println("Write: [\"articleNumber\"] to find the article.");

        int articleNumber = scanner.nextInt();
        BulkArticle read = clientDataPersisterImpl.readBulkArticle(articleNumber);
        printArticle(read);

        printArticleManagementMenu();
    }

    private void printArticle(Article article) {
        System.out.println("id: " + article.getArticleNumber());
        System.out.println("name: " + article.getName());
        System.out.println("description: " + article.getDescription());
        System.out.println("stock: " + article.getStock());

        if (article instanceof BulkArticle bulkArticle && bulkArticle.getBulkSize() > 0)
            System.out.println("bulk size: " + bulkArticle.getBulkSize());
    }

    private void addArticleToShoppingBasket() throws NoMultipleOfBulkException {

        System.out.println("Please type the number of the article that you wish to add to your Basket.");
        int articleNumber = scanner.nextInt();

        System.out.println("Please choose how many Items of this number you wish to add to your Basket.");
        int articleAmount = scanner.nextInt();

        BulkArticle bulkArticle = clientDataPersisterImpl.readBulkArticle(articleNumber);

        while (articleAmount > bulkArticle.getStock()) {
            System.out.println(bulkArticle.getName() + " has " + bulkArticle.getStock() + " items stored.");
            System.out.println("Enter the amount of the article.");
            articleAmount = scanner.nextInt();
        }

        boolean isMultiple = true;

        if (bulkArticle.getBulkSize() > 0)
            isMultiple = articleAmount % bulkArticle.getBulkSize() == 0;

        if (isMultiple)
            clientDataPersisterImpl.getCustomer().getShoppingBasket().addToArticleMap(bulkArticle.getArticleNumber(), articleAmount);
        else {
            int actualBulkSize = bulkArticle.getBulkSize();
            throw new NoMultipleOfBulkException(MessageFormat.format("Error: Article amount must be a multiple of {0}", actualBulkSize), bulkArticle.getArticleNumber(), actualBulkSize, bulkArticle.getStock());
        }
    }
}
