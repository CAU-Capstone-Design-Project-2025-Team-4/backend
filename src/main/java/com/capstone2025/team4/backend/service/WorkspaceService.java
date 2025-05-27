package com.capstone2025.team4.backend.service;

import com.capstone2025.team4.backend.domain.User;
import com.capstone2025.team4.backend.domain.Workspace;
import com.capstone2025.team4.backend.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class WorkspaceService {
    private final WorkspaceRepository workspaceRepository;

    public void newWorkspace(User user) {
        Workspace workspace = new Workspace(user);
        workspaceRepository.save(workspace);
    }

    public Workspace getWorkspace(User creator) {
        Optional<Workspace> optionalWorkspace = workspaceRepository.findByUser(creator);
        if (optionalWorkspace.isEmpty()) {
            log.error("No workspace for user = {}, id = {} ", creator.getEmail(), creator.getId());
            throw new RuntimeException("워크스페이스 없는 사용자입니다.");
        }
        return optionalWorkspace.get();
    }

}
