package be.bstorm.bend.models;

public record PrivateMessage(
        String fromUser,
        String toUser,
        String message
) {
}