package be.bstorm.bend.hubs;

import be.bstorm.bend.models.SimpleMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

@Slf4j
@Controller
public class ChatChannelHub {

    @MessageMapping("chat-channel")
    public Flux<String> chatChannel(Flux<SimpleMessage> messages) {
        return messages
                .doOnNext(message -> System.out.println("Message reçu: " + message))
                .map(message -> "Réponse du serveur à: " + message);
    }
}


