package be.bstorm.bend.models.dtos;

import be.bstorm.bend.models.entities.Conversation;

public record ConversationDTO(
        Long id,
        String title
) {

    public static ConversationDTO fromEntity(Conversation conversation) {
        return new ConversationDTO(conversation.getId(), conversation.getTitle());
    }
}
