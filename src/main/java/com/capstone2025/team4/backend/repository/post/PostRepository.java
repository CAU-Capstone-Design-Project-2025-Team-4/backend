package com.capstone2025.team4.backend.repository.post;

import com.capstone2025.team4.backend.domain.Post;
import com.capstone2025.team4.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom{
}
