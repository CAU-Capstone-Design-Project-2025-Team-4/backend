package com.capstone2025.team4.backend.service;

import com.capstone2025.team4.backend.domain.User;
import com.capstone2025.team4.backend.domain.post.Category;
import com.capstone2025.team4.backend.domain.post.Post;
import com.capstone2025.team4.backend.exception.post.PostNotFound;
import com.capstone2025.team4.backend.repository.PostRepository;
import com.capstone2025.team4.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.capstone2025.team4.backend.utils.StringChecker.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public Post createNewPost(long creatorId, String title, String description, Category category) {
        Optional<User> optionalUser = userRepository.findById(creatorId);
        if (optionalUser.isEmpty()) {
            log.debug("[CREATING POST FAILED] Can't find User (id = {})", creatorId);
            return null;
        }
        User designer = optionalUser.get();

        if (stringsAreEmpty(title, description) || category == null) {
            log.debug("[CREATING POST FAILED] Null Or Empty Strings Found! title = {}, description = {}, category = {}", title, description, category);
            return null;
        }

        Post newPost = Post.builder()
                .designer(designer)
                .title(title)
                .description(description)
                .category(category)
                .build();

        log.debug("[CREATING POST SUCCESS] postId = {}, userEmail = {}, createdDate = {}", newPost.getId(), designer.getEmail(), newPost.getCreatedAt());
        return postRepository.save(newPost);
    }

    public Post getPost(Long postId) {
        Optional<Post> optionalPost = postRepository.findByIdWithDesigner(postId);
        if (optionalPost.isEmpty()) {
            throw new PostNotFound();
        }

        return optionalPost.get();
    }
}
