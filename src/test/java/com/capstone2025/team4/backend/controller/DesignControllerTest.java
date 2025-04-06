package com.capstone2025.team4.backend.controller;

import com.capstone2025.team4.backend.controller.api.ApiResponse;
import com.capstone2025.team4.backend.controller.dto.design.NewDesignRequest;
import com.capstone2025.team4.backend.domain.design.Design;
import com.capstone2025.team4.backend.domain.design.Slide;
import com.capstone2025.team4.backend.infra.security.CustomUserDetailService;
import com.capstone2025.team4.backend.infra.security.config.SecurityConfig;
import com.capstone2025.team4.backend.infra.security.jwt.JwtService;
import com.capstone2025.team4.backend.mock.WithCustomMockUser;
import com.capstone2025.team4.backend.service.design.DesignService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = DesignController.class)
@Import(SecurityConfig.class)
@WithCustomMockUser
class DesignControllerTest {
    @MockitoBean
    DesignService designService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    JwtService jwtService;
    @MockitoBean
    CustomUserDetailService customUserDetailService;

    @Test
    void newDesignBadArgs() throws Exception {
        //given
        NewDesignRequest noUserIdRequest = new NewDesignRequest(null, null, false);
        NewDesignRequest noShareFlagRequest = new NewDesignRequest(1L, null, null);

        //when
        ResultActions noUserIdResultActions = mockMvc.perform(
                post("/design")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(noUserIdRequest))
        );

        ResultActions noShareFlagResultActions = mockMvc.perform(
                post("/design")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(noShareFlagRequest))
        );

        //then
        noUserIdResultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(ApiResponse.VALIDATION_ERROR_STATUS))
                .andExpect(jsonPath("$.data.userId").value("유저 정보는 필수입니다"));

        noShareFlagResultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(ApiResponse.VALIDATION_ERROR_STATUS))
                .andExpect(jsonPath("$.data.isShared").value("공유인지 아닌지 선택해주세요"));
    }

    @Test
    void newDesignSuccess() throws Exception {
        //given
        NewDesignRequest request= new NewDesignRequest(1L, null, false);
        given(designService.createNewDesign(1L, null, false)).willReturn(createDesign());

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/design")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L));
    }

    private Design createDesign() throws NoSuchFieldException, IllegalAccessException {
        Design design = Design.builder()
                .build();

        Field id = Design.class.getDeclaredField("id");
        id.setAccessible(true);
        id.set(design, 1L);
        return design;
    }

    @Test
    void getAllSuccess() throws Exception {
        //given
        given(designService.findAll(1L)).willReturn(anyList());

        //when
        ResultActions resultActions = mockMvc.perform(
                get("/design")
                        .param("userId", "1")
        );

        //then
        resultActions
                .andExpect(status().isOk());
    }

    @Test
    void getAllBadArg() throws Exception {
        //given

        //when
        ResultActions emptyIdResult = mockMvc.perform(
                get("/design/")
        );
        ResultActions stringPathVarResult = mockMvc.perform(
                get("/design/string")
        );
        //then
        emptyIdResult
                .andExpect(status().is4xxClientError());

        stringPathVarResult
                .andExpect(status().is4xxClientError());
    }
}