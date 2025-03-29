package com.capstone2025.team4.backend.service;

import com.capstone2025.team4.backend.domain.User;
import com.capstone2025.team4.backend.exception.user.EmailAlreadyExistsException;
import com.capstone2025.team4.backend.exception.user.PasswordDoesntMatchException;
import com.capstone2025.team4.backend.exception.user.UserNotFoundException;
import com.capstone2025.team4.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final WorkspaceService workspaceService;

    public User login(String email, String password) {

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            log.debug("[LOGIN FAILED] No user found! email = {}", email);
            throw new UserNotFoundException();
        }

        User user = optionalUser.get();
        if (user.getPassword().equals(password)) {
            log.debug("[LOGIN SUCCESS] email = {}", email);
            return user;
        }

        throw new PasswordDoesntMatchException();
    }

    public User register(String name, String email, String password, String confirmPassword) {

        Optional<User> byEmail = userRepository.findByEmail(email);
        if (byEmail.isPresent()) {
            log.debug("[REGISTRATION FAILED] Email = {} Already Exists!", email);
            throw new EmailAlreadyExistsException();
        }

        if (!password.equals(confirmPassword)) {
            log.debug("[REGISTRATION FAILED] 비밀번호 맞지 않음");
            throw new PasswordDoesntMatchException();
        }


        User newUser = User.builder()
                .name(name)
                .email(email)
                .password(password).build();
        userRepository.save(newUser);
        workspaceService.newWorkspace(newUser);

        log.debug("[REGISTRATION SUCCESS] Email = {}", email);
        return newUser;
    }


}
