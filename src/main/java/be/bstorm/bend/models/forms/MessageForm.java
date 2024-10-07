package be.bstorm.bend.models.forms;

import be.bstorm.bend.models.entities.Message;

public record MessageForm(
        String content,
        Long userId,
        Long conversationId
) {
    public Message toEntity(){
        return new Message(this.content);
    }
}
