package com.capstone2025.team4.backend.repository;

import com.capstone2025.team4.backend.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
