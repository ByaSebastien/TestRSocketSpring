package be.bstorm.bend.models.forms;

import be.bstorm.bend.models.entities.Conversation;

public record ConversationForm(
        String title
) {
    public Conversation toEntity(){
        return new Conversation(this.title);
    }
}
