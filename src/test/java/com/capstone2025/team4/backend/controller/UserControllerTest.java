package com.capstone2025.team4.backend.controller;

import com.capstone2025.team4.backend.controller.api.ApiResponse;
import com.capstone2025.team4.backend.controller.dto.user.UserLoginRequest;
import com.capstone2025.team4.backend.controller.dto.user.UserRegisterRequest;
import com.capstone2025.team4.backend.domain.User;
import com.capstone2025.team4.backend.security.CustomUserDetailService;
import com.capstone2025.team4.backend.config.SecurityConfig;
import com.capstone2025.team4.backend.security.jwt.JwtService;
import com.capstone2025.team4.backend.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.lang.reflect.Field;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@Import(SecurityConfig.class)
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    UserService userService;
    @MockitoBean
    JwtService jwtService;
    @MockitoBean
    CustomUserDetailService customUserDetailService;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    void registerSuccess() throws Exception {
        //given
        User mockUser = getUser();
        setUserId(mockUser, 1L);
        UserRegisterRequest request = new UserRegisterRequest("name", "email@email.com", "pass", "pass");
        given(userService.register(request.getName(), request.getEmail(), request.getPassword(), request.getConfirmPassword()))
                .willReturn(mockUser);

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(ApiResponse.OK_STATUS))
                .andExpect(jsonPath("$.data.id").value(1))
                .andDo(print());

    }

    private static void setUserId(User mockUser, long id) throws NoSuchFieldException, IllegalAccessException {
        Field idField = User.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(mockUser, id);
    }

    private static User getUser() {
        User mockUser = User.builder()
                .name("name")
                .email("email@email.com")
                .password("encodedPass")
                .build();
        return mockUser;
    }


    @Test
    void registerBadValues() throws Exception {
        //given
        UserRegisterRequest nullFieldRequest = new UserRegisterRequest(null, "email@email.com", "pass", "pass");
        UserRegisterRequest blankFieldRequest = new UserRegisterRequest("", "email@email.com", "pass", "pass");

        //when
        ResultActions nullResultActions = mockMvc.perform(
                post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nullFieldRequest)));

        ResultActions blankResultActions = mockMvc.perform(
                post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(blankFieldRequest)));
        //then
        nullResultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(ApiResponse.VALIDATION_ERROR_STATUS))
                .andExpect(jsonPath("$.data.name").value("이름을 입력해주세요"))
                .andDo(print());
        blankResultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(ApiResponse.VALIDATION_ERROR_STATUS))
                .andExpect(jsonPath("$.data.name").value("이름을 입력해주세요"))
                .andDo(print());
    }

    @Test
    void loginSuccess() throws Exception{
        //given
        UserLoginRequest request = new UserLoginRequest("email@email.com", "pass");
        User mockUser = getUser();
        setUserId(mockUser, 1L);
        given(userService.login(request.getEmail(), request.getPassword()))
                .willReturn(mockUser);
        given(jwtService.generateToken("email@email.com")).willReturn("jwtToken");

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(ApiResponse.OK_STATUS))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.jwtToken").value("jwtToken"))
                .andDo(print());

    }

    @Test
    void loginBadEmail() throws Exception{
        //given
        UserLoginRequest badEmailRequest = new UserLoginRequest("email", "pass");
        UserLoginRequest blankEmailRequest = new UserLoginRequest("", "pass");
        User mockUser = getUser();
        setUserId(mockUser, 1L);
        given(userService.login(badEmailRequest.getEmail(), badEmailRequest.getPassword()))
                .willReturn(mockUser);
        given(jwtService.generateToken("email@email.com")).willReturn("jwtToken");

        //when
        ResultActions badResultActions = mockMvc.perform(
                post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badEmailRequest))
        );
        ResultActions blankResultActions = mockMvc.perform(
                post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(blankEmailRequest))
        );

        //then
        badResultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(ApiResponse.VALIDATION_ERROR_STATUS))
                .andExpect(jsonPath("$.data.email").value("이메일 형식을 확인해주세요"))
                .andDo(print());

        blankResultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(ApiResponse.VALIDATION_ERROR_STATUS))
                .andExpect(jsonPath("$.data.email").value("이메일을 입력해주세요"))
                .andDo(print());
    }

    @Test
    void loginBadPass() throws Exception{
        //given
        UserLoginRequest badEmailRequest = new UserLoginRequest("email@email.com", "");
        User mockUser = getUser();
        setUserId(mockUser, 1L);
        given(userService.login(badEmailRequest.getEmail(), badEmailRequest.getPassword()))
                .willReturn(mockUser);
        given(jwtService.generateToken("email@email.com")).willReturn("jwtToken");

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badEmailRequest))
        );

        //then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(ApiResponse.VALIDATION_ERROR_STATUS))
                .andExpect(jsonPath("$.data.password").value("비밀번호를 입력해주세요"))
                .andDo(print());

    }
}