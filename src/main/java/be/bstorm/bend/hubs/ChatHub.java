package be.bstorm.bend.hubs;

import be.bstorm.bend.models.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Duration;

@Slf4j
@Controller
public class ChatHub {

    // Un Sink est utilisé pour gérer les messages entrants et diffuser ces messages à tous les abonnés.
    private final Sinks.Many<Message> sink = Sinks.many().multicast().onBackpressureBuffer();

    // Méthode appelée lorsqu'un message est envoyé à la route "send-message"
    @MessageMapping("send-message")
    public void sendMessage(String message) {
        log.info("Message reçu : " + message);
        log.info("Nombre d'abonnés : " + sink.currentSubscriberCount());
        sink.tryEmitNext(new Message(message));  // Essaye d'émettre le message à tous les abonnés
    }

    // Méthode appelée lorsqu'un client s'abonne à la route "chat"
    @MessageMapping("chat")
    public Flux<Message> receiveMessages() {
        log.info("Client abonné à la route 'chat'");
        // Diffuse les messages à tous les clients connectés
        // delayElements est juste un exemple pour ralentir la diffusion de 100 ms
        return sink.asFlux();
    }
}

