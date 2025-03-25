package com.capstone2025.team4.backend.service;

import com.capstone2025.team4.backend.domain.User;
import com.capstone2025.team4.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.capstone2025.team4.backend.utils.StringChecker.stringsAreEmpty;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final WorkspaceService workspaceService;

    public User login(String email, String password) {
        if (stringsAreEmpty(email, password)) {
            log.debug("[LOGIN FAILED] Empty Email and Pass");
            return null;
        }

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            log.debug("[LOGIN FAILED] No user found! email = {}", email);
            return null;
        }

        User user = optionalUser.get();
        if (user.getPassword().equals(password)) {
            log.debug("[LOGIN SUCCESS] email = {}", email);
            return user;
        }

        log.debug("[LOGIN FAILED] Password Doesnt Match! email = {}, pass = {}", email, password);
        return null;
    }

    public User register(String name, String email, String password) {
        if (stringsAreEmpty(name, email, password)) {
            log.debug("[REGISTRATION FAILED] Empty Email and Pass");
            return null;
        }

        Optional<User> byEmail = userRepository.findByEmail(email);
        if (byEmail.isPresent()) {
            log.debug("[REGISTRATION FAILED] Email = {} Already Exists!", email);
            return null;
        }

        User newUser = User.builder()
                .name(name)
                .email(email)
                .password(password).build();
        workspaceService.newWorkspace(newUser);

        log.debug("[REGISTRATION SUCCESS] Email = {}", email);
        return newUser;
    }


}
