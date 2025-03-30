package com.capstone2025.team4.backend.service.design;

import com.capstone2025.team4.backend.domain.User;
import com.capstone2025.team4.backend.domain.Workspace;
import com.capstone2025.team4.backend.domain.design.Design;
import com.capstone2025.team4.backend.domain.design.Slide;
import com.capstone2025.team4.backend.exception.user.UserNotAllowedDesign;
import com.capstone2025.team4.backend.exception.user.UserNotAllowedWorkspace;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class DesignUtil {
    public static void checkUWDS(User user, Workspace workspace, Design design, Slide slide) {
        if (workspace != null && workspace.getUser() != user) {
            log.debug("[checkUWDS] This user is not the owner of that workspace. userEmail = {}, workspaceId = {}", user.getEmail(), workspace.getId());
            throw new UserNotAllowedWorkspace();
        }

        if (design != null && design.getWorkspace().getUser() != user) {
            log.debug("[checkUWDS] This user cannot modify the design. userEmail = {}, workspaceId = {}", user.getEmail(), workspace.getId());
            throw new UserNotAllowedDesign();
        }

        // 해당 슬라이드가 이 디자인의 슬라이드인지 확인
        if (slide != null && slide.getDesign() != design) {
            log.error("[checkUWDS] Slides that do not exist in that design. SlideId = {}, DesignId = {}", slide.getId(), design.getId());
            throw new RuntimeException("해당 디자인에 존재하지 않는 슬라이드.");
        }
    }
}
