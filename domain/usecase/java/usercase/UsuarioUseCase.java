package usercase;

import model.Usuario;

public interface UsuarioUseCase {
    Usuario signUp(Usuario usuario);
    Usuario login(String token);
}
