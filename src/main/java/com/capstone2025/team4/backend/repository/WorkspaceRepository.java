package cau.capstone2025.team4.repository;

import cau.capstone2025.team4.domain.User;
import cau.capstone2025.team4.domain.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {
    Optional<Workspace> findByUser(User user);
}
