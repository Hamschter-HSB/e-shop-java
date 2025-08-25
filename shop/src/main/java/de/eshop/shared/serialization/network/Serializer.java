package de.eshop.shared.serialization.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Simple interface for reading and writing between networks
 * @param <T> DomainObject to read/write
 */
public interface Serializer<T> {

    void write(PrintStream out, T obj);
    T read(BufferedReader in) throws IOException;

}
