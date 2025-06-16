package entrypoints;

import model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import usercase.UsuarioUseCase;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UsuarioUseCase usuarioUseCase;

    @GetMapping
    public Usuario login(@RequestHeader("Authorization") String token) {
        return usuarioUseCase.login(token);
    }
}