package net.eshop.domain;

public class User {

    private final int number;
    private final String name;

    public User(int number, String name) {
        this.number = number;
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }
}