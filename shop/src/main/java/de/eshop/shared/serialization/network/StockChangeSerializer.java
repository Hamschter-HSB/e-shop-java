package de.eshop.shared.serialization.network;

import de.eshop.shared.domain.UserType;
import de.eshop.shared.domain.events.StockChange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;

public class StockChangeSerializer implements Serializer<StockChange> {

    @Override
    public void write(PrintStream out, StockChange stockChange) {
        out.println(stockChange.getId());
        out.println(stockChange.getDayOfYear());
        out.println(stockChange.getArticleNumber());
        out.println(stockChange.getOldAmount());
        out.println(stockChange.getNewAmount());
        out.println(stockChange.getUserID());
        out.println(stockChange.getUserType().name());
    }

    @Override
    public StockChange read(BufferedReader in) throws IOException {
        int id = Integer.parseInt(in.readLine());
        int dayOfYear = Integer.parseInt(in.readLine());
        int articleNumber = Integer.parseInt(in.readLine());
        int oldAmount = Integer.parseInt(in.readLine());
        int newAmount = Integer.parseInt(in.readLine());
        int userID = Integer.parseInt(in.readLine());
        UserType userType = UserType.valueOf(in.readLine());
        return new StockChange(id, dayOfYear, articleNumber, oldAmount, newAmount, userID, userType);
    }
}
