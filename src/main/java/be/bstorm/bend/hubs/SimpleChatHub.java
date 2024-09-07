package be.bstorm.bend.hubs;

import be.bstorm.bend.models.SimpleMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Slf4j
@Controller
public class SimpleChatHub {

    // Un Sink est utilisé pour gérer les messages entrants et diffuser ces messages à tous les abonnés.
//    private Sinks.Many<Message> sink = Sinks.many().multicast().onBackpressureBuffer();
//    private final Sinks.Many<Message> sink = Sinks.many().replay().all();
    private final Sinks.Many<SimpleMessage> sink = Sinks.many().replay().limit(10); // Ne garde que les 10 derniers messages



    // Méthode appelée lorsqu'un message est envoyé à la route "send-message"
    @MessageMapping("send-simple-message")
    public void sendMessage(SimpleMessage message) {
        log.info("Message reçu : " + message);
        log.info("Nombre d'abonnés : " + sink.currentSubscriberCount());
//        sink.tryEmitNext(new Message(message));  // Essaye d'émettre le message à tous les abonnés
        Sinks.EmitResult result = sink.tryEmitNext(message);
        log.info("Resultat de l'émission : " + result);
    }

    // Méthode appelée lorsqu'un client s'abonne à la route "chat"
    @MessageMapping("simple-chat")
    public Flux<SimpleMessage> receiveMessages() {
        log.info("Client abonné à la route 'simple-chat'");
        // Si aucun abonné n'est présent, recrée le Sink
        if (sink.currentSubscriberCount() == 0) {
            resetSink();  // Implémenter la logique pour recréer ou nettoyer le sink
        }
        // Diffuse les messages à tous les clients connectés
        return sink.asFlux().startWith(new SimpleMessage("Welcome to Simple Chat")).concatWith(Flux.never());
    }

    private void resetSink() {
        // Pour gerer le sink dans le cas où il plante près deconnexion crutal du dernier user
        // uniquement pour les sink onBackpressure
//        this.sink = Sinks.many().multicast().onBackpressureBuffer();  // Réinitialiser le Sink
    }
}

