package be.bstorm.bend.hubs;

import be.bstorm.bend.models.dtos.UserDTO;
import be.bstorm.bend.models.entities.User;
import be.bstorm.bend.models.forms.UserForm;
import be.bstorm.bend.repositories.UserRepository;
import be.bstorm.bend.services.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserHub {

    private final UserRepository userRepository;
    private final AuthService authService;

    @MessageMapping("register")
    private void register(UserForm userForm) {
        User user = userForm.toEntity();
        userRepository.save(user);
    }

    @MessageMapping("login")
    private Mono<UserDTO> login(UserForm userForm) {
        User user = authService.login(userForm);
        if(!user.getPassword().equals(userForm.password())){
            throw new RuntimeException("Wrong password");
        }
        return Mono.just(UserDTO.fromEntity(user));
    }
}
