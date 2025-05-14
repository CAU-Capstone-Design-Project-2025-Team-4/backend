package com.capstone2025.team4.backend.controller;

import com.capstone2025.team4.backend.config.SecurityConfig;
import com.capstone2025.team4.backend.mock.WithCustomMockUser;
import com.capstone2025.team4.backend.security.CustomUserDetailService;
import com.capstone2025.team4.backend.security.jwt.JwtService;
import com.capstone2025.team4.backend.service.design.ModelService;
import com.capstone2025.team4.backend.service.dto.FileDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ModelController.class)
@Import(SecurityConfig.class)
@WithCustomMockUser
class ModelControllerTest {
    @MockitoBean
    ModelService modelService;

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    JwtService jwtService;

    @MockitoBean
    CustomUserDetailService customUserDetailService;

    @Test
    void getFileSuccess() throws Exception {
        //given
        given(modelService.getFile(1L)).willReturn(FileDTO.builder().fileBytes("tempFile".getBytes(StandardCharsets.UTF_8)).fileName("fileName").build());

        //when
        ResultActions resultActions = mockMvc.perform(
                get("/model/1")
        );

        //then
        resultActions
                .andExpect(status().isOk());

    }

    @Test
    void getFileBadArg() throws Exception {
        //given
        given(modelService.getFile(1L)).willReturn(FileDTO.builder().fileBytes("tempFile".getBytes(StandardCharsets.UTF_8)).fileName("fileName").build());

        //when
        ResultActions resultActions = mockMvc.perform(
                get("/element/S")
        );

        //then
        resultActions
                .andExpect(status().is4xxClientError());

    }
}