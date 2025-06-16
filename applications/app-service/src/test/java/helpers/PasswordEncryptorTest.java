package helpers;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PasswordEncryptorTest {

    @Test
    void testEncrypt_NotNullOrEmpty() {
        String password = "Password123";
        String encrypted = PasswordEncryptor.encrypt(password);

        assertNotNull(encrypted);
        assertFalse(encrypted.isBlank());
    }

    @Test
    void testEncrypt_SameInputSameOutput() {
        String password = "Password123";

        String hash1 = PasswordEncryptor.encrypt(password);
        String hash2 = PasswordEncryptor.encrypt(password);

        assertEquals(hash1, hash2);
    }

    @Test
    void testEncrypt_DifferentInputsProduceDifferentHashes() {
        String hash1 = PasswordEncryptor.encrypt("Password123");
        String hash2 = PasswordEncryptor.encrypt("Password124");

        assertNotEquals(hash1, hash2);
    }

    @Test
    void testEncrypt_HashLengthIsCorrect() {
        String password = "Password123";
        String encrypted = PasswordEncryptor.encrypt(password);

        assertEquals(64, encrypted.length()); // SHA-256 hash in hex has 64 characters
    }
}
