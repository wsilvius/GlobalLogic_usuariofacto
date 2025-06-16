package data;

import entities.UserEntity;
import model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaUserRepository implements UsuarioRepository {

    @Autowired
    private SpringDataUserRepository springDataUserRepository;

    @Override
    public Usuario save(Usuario usuario) {
        UserEntity entity = UserEntity.fromDomain(usuario);
        return springDataUserRepository.save(entity).toDomain();
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        return springDataUserRepository.findByEmail(email)
                .map(UserEntity::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return springDataUserRepository.findByEmail(email).isPresent();
    }
}
