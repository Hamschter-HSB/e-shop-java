package net.eshop.domain;

public class User {

    private final int number;
    private final String name;
    private final String password;

    public User(int number, String name, String password) {
        this.number = number;
        this.name = name;
        this.password = password;
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}