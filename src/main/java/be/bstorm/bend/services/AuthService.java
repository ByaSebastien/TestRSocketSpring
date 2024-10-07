package be.bstorm.bend.services;

import be.bstorm.bend.models.entities.User;
import be.bstorm.bend.models.forms.UserForm;
import be.bstorm.bend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;

    public User login(UserForm userForm) {
        return userRepository.findByUsername(userForm.username()).orElseThrow();
    }
}
