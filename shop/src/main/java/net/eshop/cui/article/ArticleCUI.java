package net.eshop.cui.article;

import net.eshop.cui.CUIManager;
import net.eshop.dataccess.DataPersister;
import net.eshop.domain.Article;

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
                case 1: printSubMenu(); break;
                case 2: managementMenu(); break;
                case 3: cuiManager.printMainMenu();
            }
        }

    }

    private void managementMenu() {

        System.out.println("-----E-Shop/Article/Management-----");
        System.out.println("1. Add article");
        System.out.println("2. Remove article");
        System.out.println("3. Edit stock");
        System.out.println("4. Back");

        if (scanner.hasNextInt()) {
            int choice = scanner.nextInt();

            switch (choice) {
                case 1: addArticle();
                case 2: break;
                case 3: break;
                case 4: printSubMenu(); break;
            }
        }
        // end of scanner
    }

    private void addArticle() {

        System.out.println("-----E-Shop/Article/Management/Add Article-----");
        System.out.println("Write: [\"articleNumber\" \"name\" \"description\" \"stock\"] to add an article.");

        int articleNumber = scanner.nextInt();
        String name = scanner.next();
        String description = scanner.next();
        int stock = scanner.nextInt();

        Article article = new Article(articleNumber, name, description, stock);
        dataPersister.create(article);

    }
}
