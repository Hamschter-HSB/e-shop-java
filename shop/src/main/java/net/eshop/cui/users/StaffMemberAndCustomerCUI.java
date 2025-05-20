package net.eshop.cui.users;

import net.eshop.cui.CUIManager;
import net.eshop.dataccess.DataPersister;
import net.eshop.domain.Customer;
import net.eshop.domain.ShoppingBasket;
import net.eshop.domain.StaffMember;

import java.util.Collections;
import java.util.Scanner;

public class StaffMemberAndCustomerCUI {

    private final Scanner scanner = new Scanner(System.in);

    private final CUIManager cuiManager;
    private final DataPersister dataPersister = new DataPersister();

    public StaffMemberAndCustomerCUI(CUIManager cuiManager) {
        this.cuiManager = cuiManager;
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

            printStaffMenu();
        } else {
            System.out.println("Wrong number or password!");
        }

    }

    public void printStaffMenu() {

        System.out.println("-----E-Shop/StaffMember-----");
        System.out.println("1. register new staff member");
        System.out.println("2. main menu");
        System.out.println("3. close");

        if (scanner.hasNextInt()) {
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    registerNewStaffMember();
                case 2:
                    cuiManager.printMainMenu();
                    break;
                case 3:
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

        Customer customer = new Customer(number, username, password, address, new ShoppingBasket(Collections.emptyList()));
        dataPersister.createCustomer(customer);

        System.out.println("You've successfully registered as a new customer with the username" + username + " !");
    }
}
