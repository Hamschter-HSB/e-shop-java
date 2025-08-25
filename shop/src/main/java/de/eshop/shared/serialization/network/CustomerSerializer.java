package de.eshop.shared.serialization.network;

import de.eshop.shared.domain.Customer;
import de.eshop.shared.domain.ShoppingBasket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;

public class CustomerSerializer implements Serializer<Customer> {

    @Override
    public void write(PrintStream out, Customer customer) {
        out.println(customer.getNumber());
        out.println(customer.getName());
        out.println(customer.getPassword());
        out.println(customer.getAddress());
    }

    @Override
    public Customer read(BufferedReader in) throws IOException {
        int number = Integer.parseInt(in.readLine());
        String userName = in.readLine();
        String password = in.readLine();
        String address = in.readLine();
        return new Customer(number, userName, password, address, new ShoppingBasket());
    }
}
