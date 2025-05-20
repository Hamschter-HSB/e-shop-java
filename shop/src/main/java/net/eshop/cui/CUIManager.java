package net.eshop.cui;

import net.eshop.cui.article.ArticleCUI;
import net.eshop.cui.users.StaffMemberAndCustomerCUI;

import java.util.Scanner;

public class CUIManager {

    private final Scanner scanner = new Scanner(System.in);

    private final ArticleCUI articleCUI = new ArticleCUI(this);
    private final StaffMemberAndCustomerCUI staffMemberAndCustomerCUI = new StaffMemberAndCustomerCUI(this);

    public void printLoginOption() {
        staffMemberAndCustomerCUI.loginOption();
    }

    public void printMainMenu() {

        System.out.println("-----E-Shop-----");

        if (currentUserIsStaffMember())
            System.out.println("0. Staff menu");

        System.out.println("1. Article");
        System.out.println("2. Exit");


        if (scanner.hasNextInt()) {
            int choice = scanner.nextInt();
            switch (choice) {
                case 0:
                    if (currentUserIsStaffMember())
                        staffMemberAndCustomerCUI.printStaffMenu();
                    break;
                case 1:
                    articleCUI.printSubMenu();
                    break;
                case 2:
                    break;
                default:
                    System.out.println("Invalid choice. Program terminated");
            }
        }
    }

    private boolean currentUserIsStaffMember() {
        return "STAFF_MEMBER".equals(System.getProperty("CURRENT_USER"));
    }

    private boolean currentUserIsCustomer() {
        return "CUSTOMER".equals(System.getProperty("CURRENT_USER"));
    }
}