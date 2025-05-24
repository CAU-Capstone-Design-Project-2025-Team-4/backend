package com.capstone2025.team4.backend.service;

import com.capstone2025.team4.backend.domain.User;
import com.capstone2025.team4.backend.domain.Post;
import com.capstone2025.team4.backend.domain.design.Design;
import com.capstone2025.team4.backend.exception.design.DesignNotFound;
import com.capstone2025.team4.backend.exception.post.EmptyPostTitle;
import com.capstone2025.team4.backend.exception.user.UserNotAllowed;
import com.capstone2025.team4.backend.repository.design.DesignRepository;
import com.capstone2025.team4.backend.repository.post.PostRepository;
import com.capstone2025.team4.backend.service.dto.PostDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.capstone2025.team4.backend.utils.StringChecker.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final DesignRepository designRepository;

    public Post createNewPost(Long userId, Long designId, String title, String content) {
        User user = userService.getUser(userId);

        if (stringsAreEmpty(title)) {
            log.error("[CREATING POST FAILED] Null Or Empty Title! title = {}", title);
            throw new EmptyPostTitle();
        }

        Optional<Design> optionalDesign = designRepository.findWithUserById(designId);
        if (optionalDesign.isEmpty()) {
            throw new DesignNotFound();
        }

        Design design = optionalDesign.get();
        if (design.getUser() != user) {
            throw new UserNotAllowed();
        }

        Post newPost = Post.builder()
                .user(user)
                .title(title)
                .content(content)
                .design(design)
                .build();

        return postRepository.save(newPost);
    }

    public Page<PostDTO> searchPage(Long userId, int pageNumber, int pageSize) {
        PageRequest pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        return postRepository.searchPage(pageable, userId);
    }
}
