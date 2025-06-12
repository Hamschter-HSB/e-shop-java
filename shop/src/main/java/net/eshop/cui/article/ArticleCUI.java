package net.eshop.cui.article;

import net.eshop.cui.CUIManager;
import net.eshop.domain.BulkArticle;
import net.eshop.domain.dataaccess.DataPersister;
import net.eshop.domain.Article;
import net.eshop.domain.events.StockChange;
import net.eshop.exceptions.NoMultipleOfBulkEcxeption;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class ArticleCUI {

    private final Scanner scanner = new Scanner(System.in);

    private final CUIManager cuiManager;
    private final DataPersister dataPersister;

    public ArticleCUI(CUIManager cuiManager, DataPersister dataPersister) {
        this.cuiManager = cuiManager;
        this.dataPersister = dataPersister;
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
                    System.out.println("-----------------------");
                    System.out.println("-----------------------");
                    System.out.println("-----------------------");
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

        List<BulkArticle> bulkArticles = dataPersister.readAllBulkArticles();
        bulkArticles.forEach(article -> {
            printArticle(article);
            System.out.println();
        });

    }

    public void printArticleManagementMenu() {

        System.out.println("-----E-Shop/Articles/Management-----");
        System.out.println("1. Add article");
        System.out.println("2. Find article");
        System.out.println("3. Remove article");
        System.out.println("4. Edit stock");
        System.out.println("5. Back");

        if (scanner.hasNextInt()) {
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    addArticle();
                case 2:
                    readArticle();
                case 3:
                    removeArticle();
                case 4:
                    editArticle();
                case 5:
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
                    addArticleToShoppingBasket();
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

        if(bulkSize > 0) {
            while (stock % bulkSize != 0) {
                System.out.println(name + " has a stock of " + stock + ". This is not a multiple of " + bulkSize);
                System.out.println("Enter the new stock of the bulk article.");
                stock = scanner.nextInt();
            }
        }

        BulkArticle bulkArticle = new BulkArticle(articleNumber, name, description, stock, price, bulkSize);
        dataPersister.createBulkArticle(bulkArticle);

        Random random = new Random();
        int id = random.nextInt(10000 - 1) + 1;
        StockChange stockChange = new StockChange(id, LocalDateTime.now().getDayOfYear(), articleNumber, 0, bulkArticle.getStock(), Integer.parseInt(System.getProperty("CURRENT_USER_ID")));
        dataPersister.createStockChange(stockChange);

        printArticleManagementMenu();
    }

    private void removeArticle() {

        System.out.println("-----E-Shop/Article/Management/Remove article by ID-----");
        System.out.println("Write: [\"articleNumber\"] to remove an article.");

        int articleNumber = scanner.nextInt();


        // TODO: Remove Obj by ID
    }

    private void editArticle() {

        System.out.println("-----E-Shop/Article/Management/Edit article by ID-----");
        System.out.println("Write: [\"articleNumber\"] to edit an article.");

        int articleNumber = scanner.nextInt();

        // TODO: get article obj by id

//        System.out.println("-----E-Shop/Article/Management/Edit article with ID: "+articleNumber);
//        System.out.println("id: " + article.getArticleNumber());
//        System.out.println("name: " + article.getName());
//        System.out.println("description: " + article.getDescription());
//        System.out.println("stock: " + article.getStock());

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
                    //changeName(); // TODO
                    editArticle();
                case 2:
                    //changeDesc(); // TODO
                    editArticle();
                case 3:
                    changeStock();
                case 4:
                    printArticleManagementMenu();
            }
        }
    }

    private void changeStock() {

        System.out.println("-----E-Shop/Article/Management/Edit stock-----");

        System.out.println("Write the article number you want to edit the article stock for.");
        int id = scanner.nextInt();

        System.out.println("Write the new stock amount.");
        int stock = scanner.nextInt();

        BulkArticle bulkArticle = dataPersister.readBulkArticle(id);
        int oldStock = bulkArticle.getStock();

        boolean isMultiple = true;

        if(bulkArticle.getBulkSize() > 0)
            isMultiple = stock % bulkArticle.getBulkSize() == 0;

        if(isMultiple)
            bulkArticle.setStock(stock);
        else
            throw new NoMultipleOfBulkEcxeption(MessageFormat.format("Error: Article amount must be a multiple of {0}", bulkArticle));

        dataPersister.updateBulkArticle(bulkArticle);

        Random random = new Random();
        int stockChangeID = random.nextInt(10000 - 1) + 1;
        StockChange stockChange = new StockChange(stockChangeID, LocalDateTime.now().getDayOfYear(), id, oldStock, bulkArticle.getStock(), Integer.parseInt(System.getProperty("CURRENT_USER_ID")));
        dataPersister.createStockChange(stockChange);
    }

    private void readArticle() {

        System.out.println("-----E-Shop/Article/Management/Find article-----");
        System.out.println("Write: [\"articleNumber\"] to find the article.");

        int articleNumber = scanner.nextInt();
        BulkArticle read = dataPersister.readBulkArticle(articleNumber);
        printArticle(read);

        printArticleManagementMenu();
    }

    private void printArticle(Article article) {
        System.out.println("id: " + article.getArticleNumber());
        System.out.println("name: " + article.getName());
        System.out.println("description: " + article.getDescription());
        System.out.println("stock: " + article.getStock());

        if(article instanceof BulkArticle bulkArticle && bulkArticle.getBulkSize() > 0)
            System.out.println("bulk size: " + bulkArticle.getBulkSize());
    }

    private void addArticleToShoppingBasket() {

        System.out.println("Please type the number of the article that you wish to add to your Basket.");
        int articleNumber = scanner.nextInt();

        System.out.println("Please choose how many Items of this number you wish to add to your Basket.");
        int articleAmount = scanner.nextInt();

        BulkArticle bulkArticle = dataPersister.readBulkArticle(articleNumber);

        while (articleAmount > bulkArticle.getStock()) {
            System.out.println(bulkArticle.getName() + " has " + bulkArticle.getStock() + " items stored.");
            System.out.println("Enter the amount of the article.");
            articleAmount = scanner.nextInt();
        }

        boolean isMultiple = true;

        if(bulkArticle.getBulkSize() > 0)
            isMultiple = articleAmount % bulkArticle.getBulkSize() == 0;

        if(isMultiple)
            dataPersister.getCustomer().getShoppingBasket().addToArticleMap(bulkArticle.getArticleNumber(), articleAmount);
        else
            throw new NoMultipleOfBulkEcxeption(MessageFormat.format("Error: Article amount must be a multiple of {0}", bulkArticle));
    }
}
