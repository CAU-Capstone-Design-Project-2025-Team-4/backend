package com.capstone2025.team4.backend.controller;

import com.capstone2025.team4.backend.controller.api.ApiResponse;
import com.capstone2025.team4.backend.controller.api.post.AddPostRequest;
import com.capstone2025.team4.backend.controller.api.post.PostResponse;
import com.capstone2025.team4.backend.domain.Post;
import com.capstone2025.team4.backend.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;

    @PostMapping
    public ApiResponse<PostResponse> newPost(@Valid @RequestBody AddPostRequest request) {
        Post newPost = postService.createNewPost(request.getUserId(), request.getDesignId(), request.getTitle(), request.getContent());
        return ApiResponse.success(new PostResponse(newPost));
    }
}
