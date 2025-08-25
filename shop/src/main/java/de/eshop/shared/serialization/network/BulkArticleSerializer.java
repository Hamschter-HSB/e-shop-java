package de.eshop.shared.serialization.network;

import de.eshop.shared.domain.BulkArticle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;

public class BulkArticleSerializer implements Serializer<BulkArticle> {

    @Override
    public void write(PrintStream out, BulkArticle bulkArticle) {
        out.println(bulkArticle.getArticleNumber());
        out.println(bulkArticle.getName());
        out.println(bulkArticle.getDescription());
        out.println(bulkArticle.getStock());
        out.println(bulkArticle.getPrice());
        out.println(bulkArticle.getBulkSize());
    }

    @Override
    public BulkArticle read(BufferedReader in) throws IOException {
        int articleNumber = Integer.parseInt(in.readLine());
        String name = in.readLine();
        String description = in.readLine();
        int stock = Integer.parseInt(in.readLine());
        double price = Double.parseDouble(in.readLine());
        int bulkSize = Integer.parseInt(in.readLine());
        return new BulkArticle(articleNumber, name, description, stock, price, bulkSize);
    }
}
