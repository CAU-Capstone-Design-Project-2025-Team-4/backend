package cau.capstone2025.team4.repository;

import cau.capstone2025.team4.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
