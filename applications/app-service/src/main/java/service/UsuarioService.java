package service;

import data.UsuarioRepository;
import helpers.JwtUtil;
import helpers.PasswordEncryptor;
import model.Usuario;
import org.springframework.stereotype.Service;
import usercase.UsuarioUseCase;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UsuarioService implements UsuarioUseCase {

    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;

    public UsuarioService(UsuarioRepository usuarioRepository, JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Usuario signUp(Usuario usuario) {
        if (usuario.getEmail() == null || usuario.getEmail().isBlank()) {  //isBlank propio de java 11
            throw new IllegalArgumentException("El email es obligatorio");
        }
        if (usuario.getPassword() == null || usuario.getPassword().isBlank()) {
            throw new IllegalArgumentException("El password es obligatorio");
        }
        validarEmail(usuario.getEmail());
        validarPassword(usuario.getPassword());
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("Ya existe un usuario con ese correo electrónico.");
        }
        usuario.setPassword(PasswordEncryptor.encrypt(usuario.getPassword()));
        usuario.setId(UUID.randomUUID());
        usuario.setCreated(LocalDateTime.now());
        usuario.setLastLogin(LocalDateTime.now());
        usuario.setToken(jwtUtil.generateToken(usuario.getEmail()));
        usuario.setActive(true);
        return usuarioRepository.save(usuario);
    }

    private void validarEmail(String email) {
        var regex = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
        if (!email.matches(regex)) {
            throw new IllegalArgumentException("Formato de correo inválido. Debe ser nombre@dominio.extesion");
        }
    }

    private void validarPassword(String password) {
        var mensaje = "*".repeat(12); //repeat propio de java 11
        if (password.length() < 8 || password.length() > 12) {
            throw new IllegalArgumentException("El password debe tener entre 8 y 12 caracteres: "+mensaje);
        }

        long countMayus = password.chars().filter(Character::isUpperCase).count();
        long countDigits = password.chars().filter(Character::isDigit).count();
        long countInvalid = password.chars().filter(c -> !Character.isLetterOrDigit(c)).count();

        if (countMayus != 1) {
            throw new IllegalArgumentException("El password debe contener exactamente una letra mayúscula.");
        }

        if (countDigits != 2) {
            throw new IllegalArgumentException("El password debe contener exactamente dos números.");
        }

        if (countInvalid > 0) {
            throw new IllegalArgumentException("El password solo puede contener letras y números, sin caracteres especiales.");
        }
    }

    @Override
    public Usuario login(String token) {
        var email = jwtUtil.extractEmail(token);
        var usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con el token proporcionado"));

        // Generar nuevo token y actualizar lastLogin
        usuario.setToken(jwtUtil.generateToken(email));
        usuario.setLastLogin(LocalDateTime.now());

        return usuarioRepository.save(usuario);
    }

}