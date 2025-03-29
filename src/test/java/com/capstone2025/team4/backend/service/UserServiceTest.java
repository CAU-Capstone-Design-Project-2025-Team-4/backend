package com.capstone2025.team4.backend.service;

import com.capstone2025.team4.backend.domain.User;
import com.capstone2025.team4.backend.exception.user.EmailAlreadyExistsException;
import com.capstone2025.team4.backend.exception.user.PasswordDoesntMatchException;
import com.capstone2025.team4.backend.exception.user.UserNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    void login() {
        //given
        String password = passwordEncoder.encode("pass");
        User user = User.builder().name("name").email("email").password(password).build();
        em.persist(user);

        em.flush();
        em.clear();

        //when
        User successLogin = userService.login("email", "pass");

        //then
        assertThat(successLogin).isNotNull();
        assertThat(successLogin.getEmail()).isEqualTo("email");
        assertThat(passwordEncoder.matches("pass", successLogin.getPassword())).isTrue();
        assertThat(successLogin.getId()).isNotNull();

        assertThrowsExactly(PasswordDoesntMatchException.class, () -> userService.login("email", "passsss"), "틀린 비밀번호로 로그인 시 PasswordDoesntMatchException 이 발생하지 않았다");
        assertThrowsExactly(UserNotFoundException.class, () -> userService.login("badEmail", "badPass"), "틀린 이메일로 로그인 시 UserNotFoundException 이 발생하지 않았다.");

    }

    @Test
    void register() {
        //given
        User alreadyExists = User.builder()
                .email("alreadyExistsEmail")
                .password("alreadyExistsPass")
                .name("alreadyExistsName")
                .build();

        em.persist(alreadyExists);
        em.flush();
        em.clear();

        //when
        User success = userService.register("temp", "temp", "temp", "temp");

        //then
        assertThat(success).isNotNull();
        assertThat(success.getId()).isNotEqualTo(alreadyExists.getId());

        assertThrowsExactly(EmailAlreadyExistsException.class, () -> userService.register("temp", "alreadyExistsEmail", "temp", "temp"), "이메일 중복 시 예외가 발생하지 않았다.");
        assertThrowsExactly(PasswordDoesntMatchException.class, () -> userService.register("temp", "newEmail", "temp", "badConfirmPass"), "회원가입 시 두 비밀번호가 다르지만 예외가 발생하지 않았다.");
    }
}