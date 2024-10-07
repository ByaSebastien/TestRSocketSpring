package be.bstorm.bend.models.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor @AllArgsConstructor
@ToString @EqualsAndHashCode
@Entity
public class Message {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String content;

    @Setter
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @Setter
    @ManyToOne
    private Conversation conversation;

    public Message(String content) {
        this.content = content;
    }
}
