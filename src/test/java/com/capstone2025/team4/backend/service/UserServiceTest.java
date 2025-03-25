package com.capstone2025.team4.backend.service;

import com.capstone2025.team4.backend.domain.User;
import com.capstone2025.team4.backend.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
    private UserRepository userRepository;

    @Test
    void login() {
        //given
        User user = User.builder().name("name").email("email").password("pass").build();
        em.persist(user);

        em.flush();
        em.clear();

        //when
        User badEmailAndPassLogin = userService.login("badEmail", "badPass");
        User nullLogin = userService.login(null, null);
        User emptyEmailAndPassLogin = userService.login("", "");
        User emptyEmailLogin = userService.login("", "pass");
        User badPassLogin = userService.login("email", "passsss");
        User successLogin = userService.login("email", "pass");

        //then
        assertThat(
                new User[]{
                        badEmailAndPassLogin, nullLogin, emptyEmailAndPassLogin, emptyEmailLogin, badPassLogin
                }).containsOnlyNulls();
        assertThat(successLogin).isNotNull();
        assertThat(successLogin.getEmail()).isEqualTo("email");
        assertThat(successLogin.getPassword()).isEqualTo("pass");
        assertThat(successLogin.getId()).isNotNull();
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
        User failedUser = userService.register("temp", "alreadyExistsEmail", "temp");
        User success = userService.register("temp", "temp", "temp");

        //then
        assertThat(failedUser).isNull();
        assertThat(success).isNotNull();
        assertThat(success.getId()).isNotEqualTo(alreadyExists.getId());
    }
}