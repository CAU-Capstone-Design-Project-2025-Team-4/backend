package com.capstone2025.team4.backend.service;

import com.capstone2025.team4.backend.domain.User;
import com.capstone2025.team4.backend.domain.Workspace;
import com.capstone2025.team4.backend.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkspaceService {
    private final WorkspaceRepository workspaceRepository;

    public void newWorkspace(User user) {
        Workspace workspace = new Workspace(user);
        workspaceRepository.save(workspace);
    }

}
