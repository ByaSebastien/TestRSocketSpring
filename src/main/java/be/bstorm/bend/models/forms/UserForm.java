package be.bstorm.bend.models.forms;

import be.bstorm.bend.models.entities.User;

public record UserForm(
        String username,
        String password
) {
    public User toEntity(){
        return new User(username,password);
    }
}
