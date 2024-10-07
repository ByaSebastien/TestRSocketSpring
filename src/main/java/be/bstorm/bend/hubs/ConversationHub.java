package be.bstorm.bend.hubs;

import be.bstorm.bend.models.dtos.ConversationDTO;
import be.bstorm.bend.models.dtos.MessageDTO;
import be.bstorm.bend.models.entities.Conversation;
import be.bstorm.bend.models.entities.Message;
import be.bstorm.bend.models.entities.User;
import be.bstorm.bend.models.forms.ConversationAsk;
import be.bstorm.bend.models.forms.ConversationForm;
import be.bstorm.bend.models.forms.MessageForm;
import be.bstorm.bend.repositories.ConversationRepository;
import be.bstorm.bend.repositories.MessageRepository;
import be.bstorm.bend.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Controller
public class ConversationHub {

    private final UserRepository userRepository;
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;

    private final Sinks.Many<ConversationDTO> conversationSink;
    private final ConcurrentHashMap<Long, Sinks.Many<MessageDTO>> messageMap;

    public ConversationHub(UserRepository userRepository, ConversationRepository conversationRepository, MessageRepository messageRepository) {
        this.userRepository = userRepository;
        log.error(userRepository.toString());
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.conversationSink = Sinks.many().replay().all();
        this.messageMap = new ConcurrentHashMap<>();
    }

    @PostConstruct
    public void init() {
        conversationRepository.findAll()
                .stream()
                .map(ConversationDTO::fromEntity)
                .forEach(c -> {
                    conversationSink.tryEmitNext(c);
                    messageMap.put(c.id(), Sinks.many().replay().all());
                    messageRepository.findByConversationId(c.id())
                            .stream()
                            .map(MessageDTO::fromEntity)
                            .forEach(m -> {
                                Sinks.Many<MessageDTO> messageSink = messageMap.get(c.id());
                                if (messageSink != null) {
                                    messageSink.tryEmitNext(m);
                                }
                            });
                });
    }

    @MessageMapping("send-conversation")
    public void sendConversation(ConversationForm form) {

        Conversation conversation = form.toEntity();
        conversationRepository.save(conversation);
        log.info("Conversation : {} ", conversation);
        messageMap.putIfAbsent(conversation.getId(), Sinks.many().replay().all());
        conversationSink.tryEmitNext(ConversationDTO.fromEntity(conversation));
    }

    @MessageMapping("conversation")
    public Flux<ConversationDTO> getConversations() {

        log.info("Get conversations");

        return conversationSink.asFlux().concatWith(Flux.never());
    }

    @MessageMapping("send-message")
    public void sendMessage(MessageForm form) {
        Message message = form.toEntity();
        User user = userRepository.findById(form.userId()).orElseThrow();
        Conversation conversation = conversationRepository.findById(form.conversationId()).orElseThrow();
        message.setUser(user);
        message.setConversation(conversation);
        Sinks.Many<MessageDTO> messageSink = messageMap.get(conversation.getId());
        if (messageSink == null) {
            log.error("No sink found for conversation {}", conversation);
            throw new RuntimeException();
        }
        messageRepository.save(message);
        messageSink.tryEmitNext(MessageDTO.fromEntity(message));
    }

    @MessageMapping("message-conversation")
    public Flux<MessageDTO> getConversationMessages(ConversationAsk conversation) {

        Sinks.Many<MessageDTO> messageSink = messageMap.get(conversation.conversationId());
        if (messageSink == null) {
            log.error("No sink found for conversation {}", conversation.conversationId());
            throw new RuntimeException();
        }
        return messageSink.asFlux().concatWith(Flux.never());
    }
}
