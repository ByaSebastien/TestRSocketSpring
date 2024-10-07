package be.bstorm.bend.models.dtos;

import be.bstorm.bend.models.entities.User;

public record UserDTO(
        Long id,
        String username
) {
    public static UserDTO fromEntity(User user) {
        return new UserDTO(user.getId(), user.getUsername());
    }
}
