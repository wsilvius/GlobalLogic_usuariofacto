package entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.Usuario;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserEntity {
    @Id
    private UUID id;
    private String name;
    private String email;
    private String password;
    private LocalDateTime created;
    private LocalDateTime lastLogin;
    private String token;
    private boolean isActive;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private List<PhoneEntity> phones;

    public static UserEntity fromDomain(Usuario usuario) {
        return UserEntity.builder()
                .id(usuario.getId())
                .name(usuario.getName())
                .email(usuario.getEmail())
                .password(usuario.getPassword())
                .created(usuario.getCreated())
                .lastLogin(usuario.getLastLogin())
                .token(usuario.getToken())
                .isActive(usuario.isActive())
                .phones(usuario.getPhones().stream()
                        .map(PhoneEntity::fromDomain)
                        .collect(Collectors.toList()))
                .build();
    }

    public Usuario toDomain() {
        return Usuario.builder()
                .id(id)
                .name(name)
                .email(email)
                .password(password)
                .created(created)
                .lastLogin(lastLogin)
                .token(token)
                .isActive(isActive)
                .phones(phones.stream()
                        .map(PhoneEntity::toDomain)
                        .collect(Collectors.toList()))
                .build();
    }
}
