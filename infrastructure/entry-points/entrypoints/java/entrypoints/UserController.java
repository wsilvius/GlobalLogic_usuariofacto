package entrypoints;

import model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import usercase.UsuarioUseCase;

@RestController
@RequestMapping("/sign_up")
public class UserController {
    @Autowired
    private UsuarioUseCase usuarioUseCase;

    @PostMapping
    public Usuario signUp(@RequestBody Usuario usuario) {
        return usuarioUseCase.signUp(usuario);
    }
}
