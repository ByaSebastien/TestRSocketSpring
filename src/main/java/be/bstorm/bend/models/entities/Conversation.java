package be.bstorm.bend.models.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@ToString @EqualsAndHashCode
@Entity
public class Conversation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String title;

    @ManyToMany
    private final Set<User> users;

    public Conversation() {
        users = new HashSet<>();
    }

    public Conversation(String title) {
        this();
        this.title = title;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void removeUser(User user) {
        users.remove(user);
    }
}
