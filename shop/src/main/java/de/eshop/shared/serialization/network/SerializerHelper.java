package de.eshop.shared.serialization.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class SerializerHelper {

    public static <T> void writeList(Serializer<T> serializer, PrintStream out, List<T> list) {
        out.println(list.size());
        list.forEach(t -> serializer.write(out, t));
    }

    public static <T> List<T> readList(Serializer<T> serializer, BufferedReader in) throws IOException {
        int length = Integer.parseInt(in.readLine());
        List<T> objects = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            objects.add(serializer.read(in));
        }
        return objects;
    }

}
