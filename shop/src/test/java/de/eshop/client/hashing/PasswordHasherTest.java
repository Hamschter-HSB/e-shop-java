package de.eshop.client.hashing;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class PasswordHasherTest {

    @Test
    public void hashPassword_samePasswordsHaveSameHashValue() {
        String password = "password";
        String expectedHash = "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8";
        assertEquals(expectedHash, PasswordHasher.hashPassword(password));
    }

    @Test
    void hashPassword_sameInputSameOutput() {
        String password = "password";
        String hash1 = PasswordHasher.hashPassword(password);
        String hash2 = PasswordHasher.hashPassword(password);
        assertEquals(hash1, hash2, "Hashing has to be deterministic!");
    }

    @Test
    void hashPassword_differentInputDeliversDifferentOutput() {
        String password1 = "password1";
        String password2 = "password2";
        String hash1 = PasswordHasher.hashPassword(password1);
        String hash2 = PasswordHasher.hashPassword(password2);
        assertNotEquals(hash1, hash2, "Different passwords must not provide the same hash.");
    }

    @Test
    void hashPassword_lengthIs64HexCharactersLong() {
        String hash = PasswordHasher.hashPassword("password");
        assertEquals(64, hash.length(), "SHA-256 hash has 64 hex characters.");
    }

    @Test
    void hashPassword_emptyStringHashIsEqual() {
        String hash = PasswordHasher.hashPassword("");
        assertEquals("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855", hash);
    }

    public static void main(String[] args) {
        System.out.println(PasswordHasher.hashPassword("password"));
    }

}
