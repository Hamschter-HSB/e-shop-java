package net.eshop.cui.users;

import net.eshop.cui.CUIManager;
import net.eshop.dataccess.DataPersister;
import net.eshop.domain.Article;
import net.eshop.domain.Customer;
import net.eshop.domain.ShoppingBasket;
import net.eshop.domain.StaffMember;
import net.eshop.domain.events.StockChange;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;

public class StaffMemberAndCustomerCUI {

    private final Scanner scanner = new Scanner(System.in);

    private final CUIManager cuiManager;
    private final DataPersister dataPersister;

    public StaffMemberAndCustomerCUI(CUIManager cuiManager, DataPersister dataPersister) {
        this.cuiManager = cuiManager;
        this.dataPersister = dataPersister;
    }

    public void loginOption() {

        System.out.println("-----E-Shop/Login-----");
        System.out.println("Write 1. to login as Customer");
        System.out.println("Write 2. to login as StaffMember");
        System.out.println("Write 3. to register as Customer");

        int input = scanner.nextInt();

        switch (input) {
            case 1:
                loginAsCustomer();
                break;
            case 2:
                loginAsStaffMember();
                break;
            case 3:
                registerAsCustomer();
                break;
        }
    }

    public void loginAsStaffMember() {

        System.out.println("-----E-Shop/Login/StaffMember-----");

        System.out.println("Please input your number.");
        int number = scanner.nextInt();

        System.out.println("Please input your password.");
        String password = scanner.next();

        StaffMember staffMember = dataPersister.readStaffMember(number);

        if (staffMember.getPassword().equals(password)) {
            System.out.println("You've successfully logged in!");

            System.setProperty("CURRENT_USER", "STAFF_MEMBER");
            System.setProperty("CURRENT_USER_ID", String.valueOf(number));

            printStaffMenu();
        } else {
            System.out.println("Wrong number or password!");
        }

    }

    public void printStaffMenu() {

        System.out.println("-----E-Shop/StaffMember-----");
        System.out.println("1. register new staff member");
        System.out.println("2. manage articles");
        System.out.println("3. main menu");
        System.out.println("4. close");

        if (scanner.hasNextInt()) {
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    registerNewStaffMember();
                case 2:
                    cuiManager.getArticleCUI().printArticleManagementMenu();
                case 3:
                    cuiManager.printMainMenu();
                    break;
                case 4:
            }
        }

    }

    public void registerNewStaffMember() {

        System.out.println("-----E-Shop/Register/StaffMember-----");
        System.out.println("Please input a number.");
        int number = scanner.nextInt();

        System.out.println("Please input a username.");
        String username = scanner.next();

        System.out.println("Please input a password.");
        String password = scanner.next();

        StaffMember staffMember = new StaffMember(number, username, password);
        dataPersister.createStaffMember(staffMember);

        System.out.println("You've successfully registered the new staff member with the username" + username + " !");
    }

    public void loginAsCustomer() {

        System.out.println("-----E-Shop/Login/Customer-----");

        System.out.println("Please input your number.");
        int number = scanner.nextInt();

        System.out.println("Please input your password.");
        String password = scanner.next();

        Customer customer = dataPersister.readCustomer(number);

        if (customer.getPassword().equals(password)) {
            System.out.println("You've successfully logged in!");

            System.setProperty("CURRENT_USER", "CUSTOMER");
            System.setProperty("CURRENT_USER_ID", String.valueOf(number));

            cuiManager.printMainMenu();
        } else
            System.out.println("Wrong number or password!");
    }

    public void registerAsCustomer() {

        System.out.println("-----E-Shop/Register-----");
        System.out.println("Please input a number.");
        int number = scanner.nextInt();

        System.out.println("Please input a username.");
        String username = scanner.next();

        System.out.println("Please input a password.");
        String password = scanner.next();

        System.out.println("Please input an address");
        String address = scanner.next();

        Customer customer = new Customer(number, username, password, address, new ShoppingBasket(new HashMap<>()));
        dataPersister.createCustomer(customer);

        System.out.println("You've successfully registered as a new customer with the username" + username + " !");
    }

    public void printShoppingBasketManagementMenu() {

        System.out.println("-----E-Shop/Customer/Shopping_Basket-----");

        System.out.println("1. Edit amount of an article in the shopping Basket");
        System.out.println("2. Clear Shopping Basket");
        System.out.println("3. Buy all articles in the Shopping Basket");
        System.out.println("4. Back");

        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                printEditAmountOfAnArticleInShoppingBasketMenu();
                break;
            case 2:
                // TODO REMOVE and use own DAO. Temporary code
                dataPersister.getCustomer().getShoppingBasket().clear();
                System.out.println("You've successfully cleared your Shopping Basket");
            case 3:
                buyArticlesInShoppingCart();
            case 4:
                cuiManager.printMainMenu();
        }
    }

    public void printEditAmountOfAnArticleInShoppingBasketMenu() {

        printShoppingBasket();

        System.out.println("Enter the number of the Article for which you want to change the amount.");
        int articleNumber = scanner.nextInt();

        System.out.println("Enter the new amount of the article.");
        int newAmount = scanner.nextInt();

        Article article = dataPersister.readArticle(articleNumber);

        while (newAmount > article.getStock()) {
            System.out.println(article.getName() + " has " + article.getStock() + " items stored.");
            System.out.println("Enter the new amount of the article.");
            newAmount = scanner.nextInt();
        }

        // TODO REMOVE and use own DAO. Temporary code
        ShoppingBasket shoppingBasketOfCurrentUser = dataPersister.getCustomer().getShoppingBasket();
        shoppingBasketOfCurrentUser.addToArticleMap(articleNumber, newAmount);

        printShoppingBasketManagementMenu();
    }

    //TODO DEBUG why does this method not print the expected phrases?
    private void printShoppingBasket() {

        System.out.println("-----E-Shop/Customer/Shopping-Basket/Articles-----");

        ShoppingBasket shoppingBasketOfCurrentUser = dataPersister.getCustomer().getShoppingBasket();

        shoppingBasketOfCurrentUser.getArticleMap().forEach((key, value) -> {

            Article article = dataPersister.readArticle(key);
            System.out.println("id: " + article.getArticleNumber());
            System.out.println("name: " + article.getName());
            System.out.println("description: " + article.getDescription());
            System.out.println("amount: " + value + "\n");
        });
    }

    private void buyArticlesInShoppingCart() {

        Random random = new Random();

        // Remove articles from stock
        dataPersister.getCustomer().getShoppingBasket().getArticleMap().forEach((id, amount) -> {

            Article article = dataPersister.readArticle(id);
            int oldStock = article.getStock();
            article.setStock(oldStock - amount);
            dataPersister.updateArticle(article);

            int stockChangeID = random.nextInt(10000 - 1) + 1;
            StockChange stockChange = new StockChange(stockChangeID, LocalDateTime.now().getDayOfYear(), id, oldStock, article.getStock(), Integer.parseInt(System.getProperty("CURRENT_USER_ID")));
            dataPersister.createStockChange(stockChange);
        });

        // Calculate total price


        // Print invoice
        printInvoice();

        // Clear shopping cart

        // TODO REMOVE and use own DAO. Temporary code
        dataPersister.getCustomer().getShoppingBasket().clear();
    }

    private void printInvoice() {

        //Die Rechnung enthält u.a. Kunde,
        // Datum, die gekauften Artikel inkl.
        // Stückzahl und Preisinformation sowie den Gesamtpreis.
        Customer customer = dataPersister.getCustomer();
        LocalDate date = LocalDate.now();

        System.out.println("-----Your Invoice-----");

        System.out.println("Customer: " + customer.getName());
        System.out.println("Date: " + date+ "\n");

        AtomicReference<Double> totalPrice = new AtomicReference<>((double) 0);

        dataPersister.getCustomer().getShoppingBasket().getArticleMap().forEach((id, amount) -> {

            Article article = dataPersister.readArticle(id);
            totalPrice.updateAndGet(v -> v + article.getPrice() * amount);

            System.out.println("Article: " + article.getName());
            System.out.println("Amount: " + amount);
            System.out.println("Price: " + article.getPrice() + "\n");
        });
        System.out.println("\n Total Price: " + totalPrice.get() + "\n");
        System.out.println("\n-----Thank you for your purchase!-----");
    }
}
