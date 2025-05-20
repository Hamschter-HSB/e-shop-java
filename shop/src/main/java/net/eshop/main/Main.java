package net.eshop.main;

import net.eshop.cui.CUIManager;

public class Main {

    public static void main(String[] args) {

        System.out.println("E-Shop started:");

        CUIManager manager = new CUIManager();
        manager.printMainMenu();
    }
}