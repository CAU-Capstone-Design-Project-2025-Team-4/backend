package com.capstone2025.team4.backend.repository.post;

import com.capstone2025.team4.backend.service.dto.PostFullDTO;
import com.capstone2025.team4.backend.service.dto.PostSimpleDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {
    Page<PostSimpleDTO> searchPage(Pageable pageable, Long userId);

    PostFullDTO findFullPost(Long postId);
}
