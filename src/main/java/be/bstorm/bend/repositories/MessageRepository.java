package be.bstorm.bend.repositories;

import be.bstorm.bend.models.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("select m from Message m join m.conversation c where c.id = :conversationId")
    List<Message> findByConversationId(Long conversationId);
}
