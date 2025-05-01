package com.capstone2025.team4.backend.repository;

import com.capstone2025.team4.backend.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p JOIN FETCH p.designer WHERE p.id= :id")
    Optional<Post> findByIdWithDesigner(Long id);
}
