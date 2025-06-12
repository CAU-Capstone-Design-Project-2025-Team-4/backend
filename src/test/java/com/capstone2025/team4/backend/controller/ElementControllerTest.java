package com.capstone2025.team4.backend.controller;

import com.capstone2025.team4.backend.controller.api.ApiResponse;
import com.capstone2025.team4.backend.controller.api.element.request.UpdateElementRequest;
import com.capstone2025.team4.backend.controller.api.element.request.UpdateSpatialRequest;
import com.capstone2025.team4.backend.domain.element.Element;
import com.capstone2025.team4.backend.domain.element.Image;
import com.capstone2025.team4.backend.domain.element.TextBox;
import com.capstone2025.team4.backend.domain.element.border.BorderRef;
import com.capstone2025.team4.backend.domain.element.border.BorderType;
import com.capstone2025.team4.backend.domain.element.spatial.CameraTransform;
import com.capstone2025.team4.backend.domain.element.spatial.Spatial;
import com.capstone2025.team4.backend.domain.element.spatial.CameraMode;
import com.capstone2025.team4.backend.infra.aws.S3Service;
import com.capstone2025.team4.backend.security.CustomUserDetailService;
import com.capstone2025.team4.backend.config.SecurityConfig;
import com.capstone2025.team4.backend.security.jwt.JwtService;
import com.capstone2025.team4.backend.mock.WithCustomMockUser;
import com.capstone2025.team4.backend.repository.element.ElementRepository;
import com.capstone2025.team4.backend.service.design.ElementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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


    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    JwtService jwtService;

    @MockitoBean
    CustomUserDetailService customUserDetailService;

    @Test
    void addNewFileElementSuccess() throws Exception {
        //given
        MockMultipartFile multipart = new MockMultipartFile("image", "test.png", "image/png", new FileInputStream(("/Users/vladkim/Pictures/test.png")));
        given(s3Service.upload(multipart)).willReturn("tempUrl");

        Element slideElement = createFileSlideElement(true);
        given(elementService.addImageElementToSlide(
                anyLong(), anyLong(),
                any(BorderRef.class),
                anyLong(), anyLong(), anyLong(),
                anyDouble(), anyLong(), anyLong(),
                eq("tempUrl")          // 문자열은 eq(...)로
        )).willReturn((Image) slideElement);

        //when
        ResultActions resultActions = mockMvc.perform(
                multipart("/element/image") // 여기를 post()가 아니라 multipart()로 변경
                        .file(multipart)
                        .param("userId", "1")
                        .param("slideId", "1")
                        .param("x", "0")
                        .param("y", "0")
                        .param("z", "0")
                        .param("angle", "3.14")
                        .param("width", "1920")
                        .param("height", "1080")
                        .param("borderType", "NONE")
                        .param("borderColor", "black")
                        .param("borderThickness", "1")
        );
        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L));

    }

    @Test
    void addNewFileElementBadArg() throws Exception {
        MockMultipartFile multipart = new MockMultipartFile("image", "test.png", "image/png", new FileInputStream(("/Users/vladkim/Pictures/test.png")));
        given(s3Service.upload(multipart)).willReturn("tempUrl");

        Element slideElement = createFileSlideElement(true);
        given(elementService.addImageElementToSlide(
                anyLong(), anyLong(),
                any(BorderRef.class),
                anyLong(), anyLong(), anyLong(),
                anyDouble(), anyLong(), anyLong(),
                eq("tempUrl")          // 문자열은 eq(...)로
        )).willReturn((Image) slideElement);

        //when
        ResultActions resultActions = mockMvc.perform(
                multipart("/element/image") // 여기를 post()가 아니라 multipart()로 변경
                        .file(multipart)
                        .param("userId", "")
                        .param("slideId", "1")
                        .param("x", "0")
                        .param("y", "0")
                        .param("z", "0")
                        .param("angle", "3.14")
                        .param("width", "1920")
                        .param("height", "1080")
                        .param("borderType", "NONE")
                        .param("borderColor", "black")
                        .param("borderThickness", "1")
        );
        //then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data.userId").value("유저 정보는 필수입니다"));
    }

    @Test
    void updateCommonFieldsSuccess() throws Exception {
        //given
        Element element = createFileElement(true);
        BorderRef borderRef = BorderRef.builder()
                .borderType(BorderType.NONE)
                .color("black")
                .thickness(1L)
                .build();
        given(elementService.updateCommonFields(1L, 1L, borderRef, 0L, 0L, 0L, 1920L, 1080L, 3.14)).willReturn(element);
        UpdateElementRequest request = new UpdateElementRequest(1L, 1L, borderRef.getBorderType(), borderRef.getColor(), borderRef.getThickness(), 0L, 0L, 0L, 3.14, 1920L, 1080L);
        //when
        ResultActions resultActions = mockMvc.perform(
                patch("/element")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").value("tempUrl"));

    }

    @Test
    void updateCommonFieldsBadArgs() throws Exception {
        //given
        Element element = createFileElement(true);
        BorderRef borderRef = BorderRef.builder()
                .borderType(BorderType.NONE)
                .color("black")
                .thickness(1L)
                .build();
        given(elementService.updateCommonFields(1L, 1L, borderRef, 0L, 0L, 0L, 1920L, 1080L, 3.14)).willReturn(element);
        UpdateElementRequest request = new UpdateElementRequest(null, 1L, borderRef.getBorderType(), borderRef.getColor(), borderRef.getThickness(), 0L, 0L, 0L, 3.14, 1920L, 1080L);

        //when
        ResultActions resultActions = mockMvc.perform(
                patch("/element")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        //then
        resultActions.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status").value(ApiResponse.VALIDATION_ERROR_STATUS));

    }


    @Test
    void getAllElementsInSlideSuccess() throws Exception {
        //given
        given(elementService.findAllElementsInSlide(1L, 1L)).willReturn(Collections.singletonList(createFileSlideElement(true)));

        //when
        ResultActions resultActions = mockMvc.perform(
                get("/element")
                        .param("userId", "1")
                        .param("slideId", "1")
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value("1"));

    }


    // Test methods
    @Test
    void updateSpatialSuccess() throws Exception {
        // given
        CameraTransform cameraTransform = CameraTransform.builder()
                .positionX(0.0)
                .positionY(0.0)
                .positionZ(0.0)
                .rotationX(0.0)
                .rotationY(0.0)
                .rotationZ(0.0)
                .build();

        Spatial spatial = mock(Spatial.class);
        given(spatial.getCameraMode()).willReturn(CameraMode.FREE);

        given(elementService.updateSpatial(
                any(),
                any(),
                any(),
                any(),
                any()
        )).willReturn(spatial);

        UpdateSpatialRequest request = new UpdateSpatialRequest();
        request.setUserId(1L);
        request.setElementId(1L);
        request.setCameraMode(CameraMode.FREE);
        request.setCameraTransform(cameraTransform);
        request.setBackgroundColor("#ffffff");

        // when
        ResultActions resultActions = mockMvc.perform(
                patch("/element/spatial")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.cameraMode").value("FREE"));
    }

    @Test
    void updateSpatialBadArgs() throws Exception {
        // given
        CameraTransform cameraTransform = CameraTransform.builder()
                .positionX(0.0)
                .positionY(0.0)
                .positionZ(0.0)
                .rotationX(0.0)
                .rotationY(0.0)
                .rotationZ(0.0)
                .build();

        UpdateSpatialRequest request = new UpdateSpatialRequest();
        request.setUserId(null); // userId missing
        request.setElementId(1L);
        request.setCameraMode(CameraMode.FREE);
        request.setCameraTransform(cameraTransform);
        request.setBackgroundColor("#ffffff");

        // when
        ResultActions resultActions = mockMvc.perform(
                patch("/element/spatial")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        // then
        resultActions.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status").value(ApiResponse.VALIDATION_ERROR_STATUS));
    }

    private Element createFileSlideElement(boolean isFile) throws Exception {
        Element element = createFileElement(isFile);
        Field id = Element.class.getDeclaredField("id");
        id.setAccessible(true);
        id.set(element, 1L);
        return element;
    }

    private Element createFileElement(boolean isFIle) {
        if (isFIle) {
            return Image.builder()
                    .url("tempUrl")
                    .build();
        } else {
            return TextBox.builder()
                    .text("tempContent")
                    .build();
        }
    }
}