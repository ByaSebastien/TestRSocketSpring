package be.bstorm.bend.models.dtos;

import be.bstorm.bend.models.entities.Message;

public record MessageDTO(
        Long id,
        String username,
        String content
) {
    public static MessageDTO fromEntity(Message message){
        return new MessageDTO(message.getId(), message.getUser().getUsername(), message.getContent());
    }
}
