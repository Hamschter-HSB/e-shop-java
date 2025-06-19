package net.eshop.domain.events;

public class StockChange {

    private final int id;
    private final int dayOfYear;
    private final int articleNumber;
    private final int oldAmount;
    private final int newAmount;
    private final int userID;

    public StockChange(int id, int dayOfYear, int oldAmount, int articleNumber, int newAmount, int userID) {
        this.id = id;
        this.dayOfYear = dayOfYear;
        this.articleNumber = articleNumber;
        this.oldAmount = oldAmount;
        this.newAmount = newAmount;
        this.userID = userID;
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
}
