package com.capstone2025.team4.backend.controller;

import com.capstone2025.team4.backend.controller.api.design.NewSlideRequest;
import com.capstone2025.team4.backend.domain.User;
import com.capstone2025.team4.backend.domain.Workspace;
import com.capstone2025.team4.backend.domain.design.Design;
import com.capstone2025.team4.backend.domain.design.Slide;
import com.capstone2025.team4.backend.security.CustomUserDetailService;
import com.capstone2025.team4.backend.config.SecurityConfig;
import com.capstone2025.team4.backend.security.jwt.JwtService;
import com.capstone2025.team4.backend.mock.WithCustomMockUser;
import com.capstone2025.team4.backend.service.UserService;
import com.capstone2025.team4.backend.service.WorkspaceService;
import com.capstone2025.team4.backend.service.design.DesignService;
import com.capstone2025.team4.backend.service.design.SlideService;
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
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SlideController.class)
@Import(SecurityConfig.class)
@WithCustomMockUser
class SlideControllerTest {

    @MockitoBean
    DesignService designService;

    @MockitoBean
    SlideService slideService;

    @MockitoBean
    UserService userService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    JwtService jwtService;
    @MockitoBean
    CustomUserDetailService customUserDetailService;

    @MockitoBean
    private WorkspaceService workspaceService;

    @Test
    void newSlideSuccess() throws Exception {
        //given
        NewSlideRequest request = new NewSlideRequest(1L, 2L, 0);
        User user = User.builder().build();
        given(userService.getUser(1L)).willReturn(user);
        given(workspaceService.getWorkspace(any(User.class))).willReturn(new Workspace(user));
        given(designService.getDesign(2L)).willReturn(Design.builder().build());
        given(slideService.newSlide(any(User.class), any(Design.class), any(Workspace.class), anyInt())).willAnswer(invocation -> createSlide());

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/slide")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L));
    }

    @Test
    void newSlideBadArgs() throws Exception{
        //given
        NewSlideRequest nullUserIdRequest= new NewSlideRequest(null, 2L, 0);
        NewSlideRequest nullDesignIdRequest= new NewSlideRequest(1L, null, 0);
        NewSlideRequest nullOrderRequest= new NewSlideRequest(1L, 2L, null);

        //when
        ResultActions nullUserIdResultActions = mockMvc.perform(
                post("/slide")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nullUserIdRequest))
        );

        ResultActions nullDesignIdResultActions = mockMvc.perform(
                post("/slide")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nullDesignIdRequest))
        );

        ResultActions nullOrderResultActions = mockMvc.perform(
                post("/slide")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nullOrderRequest))
        );

        //then
        nullUserIdResultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data.userId").value("유저 정보는 필수입니다"));

        nullDesignIdResultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data.designId").value("디자인 정보는 필수입니다"));

        nullOrderResultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data.order").value("순서 정보는 필수입니다"));
    }

    private Slide createSlide() throws Exception {
        Slide slide = Slide.builder().build();
        Field id = Slide.class.getDeclaredField("id");
        id.setAccessible(true);
        id.set(slide, 1L);
        return slide;
    }

    @Test
    void allInDesign() throws Exception {
        //given
        given(slideService.findAllInDesign(1L, 1L)).willReturn(new ArrayList<>());

        //when
        ResultActions resultActions = mockMvc.perform(
                get("/slide").param("designId", "1").param("userId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions
                .andExpect(status().isOk());
    }
}