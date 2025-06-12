package net.eshop.domain;

public class LoginSession {

    private final User user;

    public LoginSession(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
