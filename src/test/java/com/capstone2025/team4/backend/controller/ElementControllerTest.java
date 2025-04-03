package com.capstone2025.team4.backend.controller;

import com.capstone2025.team4.backend.domain.design.SlideElement;
import com.capstone2025.team4.backend.domain.design.Type;
import com.capstone2025.team4.backend.domain.design.element.FileElement;
import com.capstone2025.team4.backend.infra.aws.S3Entity;
import com.capstone2025.team4.backend.infra.aws.S3Service;
import com.capstone2025.team4.backend.infra.security.CustomUserDetailService;
import com.capstone2025.team4.backend.infra.security.config.SecurityConfig;
import com.capstone2025.team4.backend.infra.security.jwt.JwtService;
import com.capstone2025.team4.backend.mock.WithCustomMockUser;
import com.capstone2025.team4.backend.repository.ElementRepository;
import com.capstone2025.team4.backend.repository.FileElementRepository;
import com.capstone2025.team4.backend.service.design.ElementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ElementController.class)
@Import(SecurityConfig.class)
@WithCustomMockUser
class ElementControllerTest {

    @MockitoBean
    DesignController designController;

    @MockitoBean
    ElementService elementService;
    
    @MockitoBean
    S3Service s3Service;

    @MockitoBean
    ElementRepository elementRepository;

    @MockitoBean
    FileElementRepository fileElementRepository;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    JwtService jwtService;

    @MockitoBean
    CustomUserDetailService customUserDetailService;

    @Test
    void addNewElementSuccess() throws Exception {
        //given
        MockMultipartFile multipart = new MockMultipartFile("file", "test.png", "image/png", new FileInputStream(("/Users/vladkim/Pictures/test.png")));
        given(s3Service.upload(multipart)).willReturn("tempUrl");

        SlideElement slideElement = createFileSlideElement();
        given(elementService.addUserFileElementToSlide(1L, 1L, "tempUrl", Type.IMAGE, 0L, 0L, 3.14, 1920L, 1080L)).willReturn(slideElement);

        //when
        ResultActions resultActions = mockMvc.perform(
                multipart("/element/add/file") // 여기를 post()가 아니라 multipart()로 변경
                        .file(multipart)
                        .param("userId", "1")
                        .param("slideId", "1")
                        .param("elementId", "1")
                        .param("type", "IMAGE")
                        .param("x", "0")
                        .param("y", "0")
                        .param("angle", "3.14")
                        .param("width", "1920")
                        .param("height", "1080")
        );
        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L));

    }

    @Test
    void addNewElementBadArg() throws Exception {
        //given
        MockMultipartFile multipart = new MockMultipartFile("file", "test.png", "image/png", new FileInputStream(("/Users/vladkim/Pictures/test.png")));
        given(s3Service.upload(multipart)).willReturn("tempUrl");

        SlideElement slideElement = createFileSlideElement();
        given(elementService.addUserFileElementToSlide(1L, 1L, "tempUrl", Type.IMAGE, 0L, 0L, 3.14, 1920L, 1080L)).willReturn(slideElement);

        //when
        ResultActions resultActions = mockMvc.perform(
                multipart("/element/add/file") // 여기를 post()가 아니라 multipart()로 변경
                        .file(multipart)
                        .param("userId", "")
                        .param("slideId", "1")
                        .param("elementId", "1")
                        .param("type", "IMAGE")
                        .param("x", "0")
                        .param("y", "0")
                        .param("angle", "3.14")
                        .param("width", "1920")
                        .param("height", "1080")
        );
        //then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data.userId").value("유저 정보는 필수입니다"));
    }

    @Test
    void getFileSuccess() throws Exception {
        //given
        given(fileElementRepository.findById(1L)).willReturn(Optional.of(createFileElement()));
        given(s3Service.findByUrl("tempUrl")).willReturn(S3Entity.builder().url("tempUrl").s3Key("s3Key").originalFileName("fileName").build());

        //when
        ResultActions resultActions = mockMvc.perform(
                get("/element/file/1")
        );

        //then
        resultActions
                .andExpect(status().isOk());

    }

    @Test
    void getFileBadArg() throws Exception {
        //given
        given(fileElementRepository.findById(1L)).willReturn(Optional.of(createFileElement()));
        given(s3Service.findByUrl("tempUrl")).willReturn(S3Entity.builder().url("tempUrl").s3Key("s3Key").originalFileName("fileName").build());

        //when
        ResultActions resultActions = mockMvc.perform(
                get("/element/")
        );

        //then
        resultActions
                .andExpect(status().is4xxClientError());

    }

    private SlideElement createFileSlideElement() throws Exception {
        SlideElement slideElement = SlideElement.builder()
                .element(createFileElement())
                .build();
        Field id = SlideElement.class.getDeclaredField("id");
        id.setAccessible(true);
        id.set(slideElement, 1L);
        return slideElement;
    }

    private FileElement createFileElement() {
        return FileElement.builder()
                .s3Url("tempUrl")
                .build();
    }
}