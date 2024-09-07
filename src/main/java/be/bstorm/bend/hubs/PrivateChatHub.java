package be.bstorm.bend.hubs;

import be.bstorm.bend.models.PrivateMessage;
import be.bstorm.bend.models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Controller
public class PrivateChatHub {

    // Une Map pour associer les usernames à leurs Sink, permettant d'envoyer des messages privés
    private final ConcurrentHashMap<String, Sinks.Many<PrivateMessage>> userSinks = new ConcurrentHashMap<>();

    // Méthode pour enregistrer un utilisateur et associer un Sink à son username
    @MessageMapping("register-private-chat")
    public void registerUser(User user) {
        // Crée un Sink pour cet utilisateur s'il n'existe pas déjà
        userSinks.putIfAbsent(user.username(), Sinks.many().replay().limit(10));
        System.out.println(user.username() + " est enregistré !");
    }

    // Méthode pour envoyer un message privé
    @MessageMapping("send-private-message")
    public void sendMessage(PrivateMessage privateMessage) {
        // Récupère le Sink du destinataire
        Sinks.Many<PrivateMessage> recipientSink = userSinks.get(privateMessage.toUser());
        if (recipientSink != null) {
            // Envoie le message au destinataire
            log.info("Sending message to " + privateMessage.toUser() + " " + privateMessage.message());
            recipientSink.tryEmitNext(privateMessage);
        } else {
            System.out.println("Utilisateur non trouvé : " + privateMessage.toUser());
        }
    }

    // Méthode pour recevoir les messages d'un utilisateur
    @MessageMapping("private-chat")
    public Flux<PrivateMessage> receiveMessages(User user) {
        // Retourne un flux de messages pour l'utilisateur spécifié
        Sinks.Many<PrivateMessage> userSink = userSinks.get(user.username());
        if (userSink != null) {
            return userSink.asFlux();
        } else {
            // Si l'utilisateur n'est pas trouvé, renvoie un flux vide
            return Flux.empty();
        }
    }
}

