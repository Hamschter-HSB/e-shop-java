package net.eshop.cui;

import net.eshop.cui.article.ArticleCUI;

import java.util.Scanner;

public class CUIManager {

    private final Scanner scanner = new Scanner(System.in);

    private final ArticleCUI articleCUI = new ArticleCUI(this);

    public void printMainMenu() {

        System.out.println("-----E-Shop-----");
        System.out.println("1. Article");
        System.out.println("2. Exit");

        if (scanner.hasNextInt()) {
            int choice = scanner.nextInt();
            switch (choice) {
                case 1: articleCUI.printSubMenu(); break;
                case 2: break;
                default: System.out.println("Invalid choice. Program terminated");
            }
        }
    }
}