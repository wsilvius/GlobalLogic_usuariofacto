package entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.Phone;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "phones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhoneEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long number;
    private Integer citycode;
    private String contrycode;

    public static PhoneEntity fromDomain(Phone phone) {
        return PhoneEntity.builder()
                .number(phone.getNumber())
                .citycode(phone.getCitycode())
                .contrycode(phone.getContrycode())
                .build();
    }

    public Phone toDomain() {
        return Phone.builder()
                .number(number)
                .citycode(citycode)
                .contrycode(contrycode)
                .build();
    }
}
