package be.bstorm.bend.models.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor @AllArgsConstructor
@ToString @EqualsAndHashCode
@Entity
@Table(name = "user_")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(unique = true, nullable = false)
    private String username;

    @Setter
    private String password;

    public User(String username, String password) {
        this.password = password;
        this.username = username;
    }
}
