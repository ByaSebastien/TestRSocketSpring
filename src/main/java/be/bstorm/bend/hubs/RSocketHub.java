package be.bstorm.bend.hubs;

import be.bstorm.bend.models.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
public class RSocketHub {

    // Le client envoie un message sur la route "request-response" et attend une réponse.
    @MessageMapping("request-response")
    public Mono<Message> requestResponse(String message) {
        // Affiche le message reçu
        log.info("Message reçu " + message);
        // Retourne une réponse en ajoutant un préfixe au message reçu.
        return Mono.just(new Message("Réponse : " + message));  // Envoie une chaîne formatée en JSON
    }
}
