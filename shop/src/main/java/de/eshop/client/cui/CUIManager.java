package de.eshop.client.cui;

import de.eshop.client.cui.article.ArticleCUI;
import de.eshop.client.cui.users.StaffMemberAndCustomerCUI;
import de.eshop.client.dataaccess.ClientDataPersisterImpl;

import java.util.Scanner;

public class CUIManager {

    private final Scanner scanner = new Scanner(System.in);

    private final ArticleCUI articleCUI;
    private final StaffMemberAndCustomerCUI staffMemberAndCustomerCUI;

    public CUIManager(ClientDataPersisterImpl clientDataPersisterImpl) {
        articleCUI = new ArticleCUI(this, clientDataPersisterImpl);
        staffMemberAndCustomerCUI = new StaffMemberAndCustomerCUI(this, clientDataPersisterImpl);
    }

    public void printLoginOption() {
        staffMemberAndCustomerCUI.loginOption();
    }

    public void printMainMenu() {

        System.out.println("-----E-Shop-----");

        if (currentUserIsStaffMember())
            System.out.println("0. Staff menu");

        System.out.println("1. Browse Articles");
        System.out.println("2. Check Shopping Basket");
        System.out.println("3. Exit");


        if (scanner.hasNextInt()) {
            int choice = scanner.nextInt();
            switch (choice) {
                case 0:
                    if (currentUserIsStaffMember())
                        staffMemberAndCustomerCUI.printStaffMenu();
                    break;
                case 1:
                    articleCUI.browseArticlesMenu();
                    break;
                case 2:
                    staffMemberAndCustomerCUI.printShoppingBasketManagementMenu();
                    break;
                case 3:
                    break;
                default:
                    System.out.println("Invalid choice. Program terminated");
            }
        }
    }

    public ArticleCUI getArticleCUI() {
        return articleCUI;
    }

    private boolean currentUserIsStaffMember() {
        return "STAFF_MEMBER".equals(System.getProperty("CURRENT_USER"));
    }
}