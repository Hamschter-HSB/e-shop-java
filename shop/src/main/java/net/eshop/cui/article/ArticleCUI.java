package net.eshop.cui.article;

import net.eshop.cui.CUIManager;
import net.eshop.dataccess.DataPersister;
import net.eshop.domain.Article;

import java.util.List;
import java.util.Scanner;

public class ArticleCUI {

    private final Scanner scanner = new Scanner(System.in);

    private final CUIManager cuiManager;
    private final DataPersister dataPersister = new DataPersister();

    public ArticleCUI(CUIManager cuiManager) {
        this.cuiManager = cuiManager;
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
                    managementMenu();
                    break;
                case 3:
                    cuiManager.printMainMenu();
            }
        }

    }

    private void readAllArticles() {

        System.out.println("-----E-Shop/Article/Management/Manage articles-----");

        List<Article> articles = dataPersister.readAll();
        articles.forEach(article -> {
            printArticle(article);
            System.out.println();
        });

    }

    private void managementMenu() {

        System.out.println("-----E-Shop/Article/Management-----");
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
                    //editArticle();
                case 5:
                    printSubMenu();
                    break;
            }
        }
    }

    private void addArticle() {

        System.out.println("-----E-Shop/Article/Management/Add article-----");
        System.out.println("Write: [\"articleNumber\" \"name\" \"description\" \"stock\"] to add an article.");

        int articleNumber = scanner.nextInt();
        String name = scanner.next();
        String description = scanner.next();
        int stock = scanner.nextInt();

        Article article = new Article(articleNumber, name, description, stock);
        dataPersister.create(article);
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
        System.out.println("-----E-Shop/Article/Management/Edit article with ID: "+articleNumber);
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
                    //changeStock(); // TODO
                    editArticle();
                case 4:
                    managementMenu();
            }
        }
    }

    private void readArticle() {

        System.out.println("-----E-Shop/Article/Management/Read article-----");
        System.out.println("Write: [\"articleNumber\"] to find the article.");

        int articleNumber = scanner.nextInt();
        Article read = dataPersister.read(articleNumber);
        printArticle(read);
    }

    private void printArticle(Article article) {
        System.out.println("id: " + article.getArticleNumber());
        System.out.println("name: " + article.getName());
        System.out.println("description: " + article.getDescription());
        System.out.println("stock: " + article.getStock());
    }
}
