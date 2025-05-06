package net.eshop.domain;

public class Customer extends User {

    private final String address;
    private final ShoppingBasket shoppingBasket;

    public Customer(int number, String name, String address, ShoppingBasket shoppingBasket) {
        super(number, name);
        this.address = address;
        this.shoppingBasket = shoppingBasket;
    }

    public String getAddress() {
        return address;
    }

    public ShoppingBasket getShoppingBasket() {
        return shoppingBasket;
    }
}