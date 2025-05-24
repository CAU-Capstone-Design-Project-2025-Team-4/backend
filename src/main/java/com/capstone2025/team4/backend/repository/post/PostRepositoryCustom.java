package com.capstone2025.team4.backend.repository.post;

import com.capstone2025.team4.backend.service.dto.PostDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {
    Page<PostDTO> searchPage(Pageable pageable, Long userId);
}
