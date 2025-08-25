package de.eshop.shared.domain.events;

import de.eshop.shared.domain.UserType;

public class StockChange {

    private final int id;
    private final int dayOfYear;
    private final int articleNumber;
    private final int oldAmount;
    private final int newAmount;
    private final int userID;
    private final UserType userType;

    public StockChange(int id, int dayOfYear, int articleNumber, int oldAmount, int newAmount, int userID, UserType userType) {
        this.id = id;
        this.dayOfYear = dayOfYear;
        this.articleNumber = articleNumber;
        this.oldAmount = oldAmount;
        this.newAmount = newAmount;
        this.userID = userID;
        this.userType = userType;
    }

    public int getId() {
        return id;
    }

    public int getDayOfYear() {
        return dayOfYear;
    }

    public int getArticleNumber() {
        return articleNumber;
    }

    public int getOldAmount() {
        return oldAmount;
    }

    public int getNewAmount() {
        return newAmount;
    }

    public int getUserID() {
        return userID;
    }

    public UserType getUserType() {
        return userType;
    }
}
