package helpers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
    }

    @Test
    void testGenerateTokenAndExtractEmail() {
        String email = "test@dominio.com";

        String token = jwtUtil.generateToken(email);
        assertNotNull(token);
        assertTrue(token.length() > 0);

        String extractedEmail = jwtUtil.extractEmail("Bearer " + token);
        assertEquals(email, extractedEmail);
    }

    @Test
    void testExtractEmailInvalidTokenException() {
        String invalidToken = "Bearer invalid.token.here";

        Exception exception = assertThrows(Exception.class, () -> jwtUtil.extractEmail(invalidToken));
        assertTrue(exception.getMessage().toLowerCase().contains("jwt"));
    }
}
