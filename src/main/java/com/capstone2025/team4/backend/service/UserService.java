package com.capstone2025.team4.backend.service;

import com.capstone2025.team4.backend.domain.User;
import com.capstone2025.team4.backend.exception.user.EmailAlreadyExistsException;
import com.capstone2025.team4.backend.exception.user.PasswordDoesntMatchException;
import com.capstone2025.team4.backend.exception.user.UserNotFoundException;
import com.capstone2025.team4.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final WorkspaceService workspaceService;
    private final PasswordEncoder passwordEncoder;

    public User login(String email, String password) {

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            log.debug("[LOGIN FAILED] No user found! email = {}", email);
            throw new UserNotFoundException();
        }

        User user = optionalUser.get();
        if (passwordEncoder.matches(password, user.getPassword())) {
            log.debug("[LOGIN SUCCESS] email = {}", email);
            return user;
        }

        throw new PasswordDoesntMatchException();
    }

    public User register(String name, String email, String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new PasswordDoesntMatchException();
        }

        Optional<User> byEmail = userRepository.findByEmail(email);
        if (byEmail.isPresent()) {
            log.debug("[REGISTRATION FAILED] Email = {} Already Exists!", email);
            throw new EmailAlreadyExistsException();
        }

        String encodedPassword = passwordEncoder.encode(password);

        User newUser = User.builder()
                .name(name)
                .email(email)
                .password(encodedPassword).build();
        userRepository.save(newUser);
        workspaceService.newWorkspace(newUser);

        log.debug("[REGISTRATION SUCCESS] Email = {}", email);
        return newUser;
    }

    public User getUser(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException();
        }
        return optionalUser.get();
    }
}
