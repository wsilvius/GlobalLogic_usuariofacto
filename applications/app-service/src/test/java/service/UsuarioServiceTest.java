package service;

import data.UsuarioRepository;
import helpers.JwtUtil;
import model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import usercase.UsuarioUseCase;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UsuarioServiceTest {

    private UsuarioRepository usuarioRepository;
    private JwtUtil jwtUtil;
    private UsuarioUseCase usuarioService;

    @BeforeEach
    void setUp() {
        usuarioRepository = mock(UsuarioRepository.class);
        jwtUtil = mock(JwtUtil.class);
        usuarioService = new UsuarioService(usuarioRepository, jwtUtil);
    }

    @Test
    void testSignUpOk() {
        var usuario = Usuario.builder()
                .email("test@dominio.com")
                .password("Passwo12")
                .build();

        when(usuarioRepository.existsByEmail("test@dominio.com")).thenReturn(false);
        when(jwtUtil.generateToken("test@dominio.com")).thenReturn("fake-token");
        when(usuarioRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var result = usuarioService.signUp(usuario);

        assertNotNull(result.getId());
        assertEquals("fake-token", result.getToken());
        assertTrue(result.isActive());
        assertNotNull(result.getCreated());
        assertNotNull(result.getLastLogin());

        verify(usuarioRepository).save(any());
    }

    @Test
    void testSignUp_EmailNulo_Exception() {
        var usuario = Usuario.builder().email(null).password("Password12").build();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> usuarioService.signUp(usuario));
        assertEquals("El email es obligatorio", exception.getMessage());
    }

    @Test
    void testSignUp_EmailVacio_Exception() {
        var usuario = Usuario.builder().email("").password("Password12").build();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> usuarioService.signUp(usuario));
        assertEquals("El email es obligatorio", exception.getMessage());
    }

    @Test
    void testSignUp_PasswordNull_Exception() {
        var usuario = Usuario.builder().email("test@dominio.com").password(null).build();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> usuarioService.signUp(usuario));
        assertEquals("El password es obligatorio", exception.getMessage());
    }

    @Test
    void testSignUp_PasswordVacio_Exception() {
        var usuario = Usuario.builder().email("test@dominio.com").password("").build();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> usuarioService.signUp(usuario));
        assertEquals("El password es obligatorio", exception.getMessage());
    }

    @Test
    void testSignUp_EmailInvalido_Exception() {
        var usuario = Usuario.builder().email("invalido").password("Password12").build();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> usuarioService.signUp(usuario));
        assertTrue(exception.getMessage().contains("Formato de correo inválido"));
    }

    @Test
    void testSignUp_EmailYaExiste_Exception() {
        var usuario = Usuario.builder().email("test@dominio.com").password("Password12").build();

        when(usuarioRepository.existsByEmail("test@dominio.com")).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> usuarioService.signUp(usuario));
        assertEquals("Ya existe un usuario con ese correo electrónico.", exception.getMessage());
    }

    @Test
    void testSignUp_PasswordNoCumpleAbajo_Exception() {
        var usuario = Usuario.builder().email("test@dominio.com").password("pass123").build();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> usuarioService.signUp(usuario));
        assertTrue(exception.getMessage().contains("El password debe tener entre 8 y 12 caracteres"));
    }

    @Test
    void testSignUp_PasswordNoCumpleArriba_Exception() {
        var usuario = Usuario.builder().email("test@dominio.com").password("pass123pass123").build();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> usuarioService.signUp(usuario));
        assertTrue(exception.getMessage().contains("El password debe tener entre 8 y 12 caracteres"));
    }

    @Test
    void testSignUp_PasswordNoCumpleMayuscula_Exception() {
        var usuario = Usuario.builder().email("test@dominio.com").password("passwo12").build();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> usuarioService.signUp(usuario));
        assertTrue(exception.getMessage().contains("El password debe contener exactamente una letra mayúscula"));
    }

    @Test
    void testSignUp_PasswordNoCumpleNumeros_Exception() {
        var usuario = Usuario.builder().email("test@dominio.com").password("passwoIi").build();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> usuarioService.signUp(usuario));
        assertTrue(exception.getMessage().contains("El password debe contener exactamente dos números"));
    }

    @Test
    void testSignUp_PasswordNoCumpleEspeciales_Exception() {
        var usuario = Usuario.builder().email("test@dominio.com").password("Passwo12#").build();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> usuarioService.signUp(usuario));
        assertTrue(exception.getMessage().contains("El password solo puede contener letras y números, sin caracteres especiales"));
    }

    @Test
    void testLogin_Ok() {
        var token = "fake-token";
        var email = "test@dominio.com";

        var usuario = Usuario.builder()
                .email(email)
                .password("Password12")
                .build();

        when(jwtUtil.extractEmail(token)).thenReturn(email);
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));
        when(jwtUtil.generateToken(email)).thenReturn("new-token");
        when(usuarioRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var result = usuarioService.login(token);

        assertEquals("new-token", result.getToken());
        assertNotNull(result.getLastLogin());
    }

    @Test
    void testLogin_UsuarioNoExiste_Exception() {
        var token = "fake-token";
        var email = "noexiste@correo.com";

        when(jwtUtil.extractEmail(token)).thenReturn(email);
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> usuarioService.login(token));
        assertEquals("Usuario no encontrado con el token proporcionado", exception.getMessage());
    }
}
