package com.capstone2025.team4.backend.service;

import com.capstone2025.team4.backend.domain.User;
import com.capstone2025.team4.backend.domain.matching.Matching;
import com.capstone2025.team4.backend.domain.post.Post;
import com.capstone2025.team4.backend.exception.matching.MatchingNotFound;
import com.capstone2025.team4.backend.exception.user.UserNotAllowed;
import com.capstone2025.team4.backend.repository.MatchingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MatchingService {

    private final MatchingRepository matchingRepository;
    private final UserService userService;
    private final PostService postService;

    public Matching requestMatching(Long senderId, Long receiverId, Long postId) {
        User sender = userService.getUser(senderId);
        User receiver = userService.getUser(receiverId);

        Post post = postService.getPost(postId);

        User designer = post.getDesigner();
        if (designer.getId() != receiverId) {
            throw new UserNotAllowed();
        }

        Matching matching = Matching.builder()
                .sender(sender)
                .receiver(receiver)
                .post(post)
                .build();
        matchingRepository.save(matching);

        return matching;
    }

    public Matching finishMatching() {
        // TODO : 매칭 종료 구현
        return null;
    }

    public Matching rejectMatching(Long receiverId, Long matchingId) {
        Matching matching = checkReceiverAndGetMatching(receiverId, matchingId);

        matching.reject();

        return matching;
    }

    private Matching checkReceiverAndGetMatching(Long receiverId, Long matchingId) {
        User receiver = userService.getUser(receiverId);

        Optional<Matching> optionalMatching = matchingRepository.findByIdWithReceiver(matchingId);
        if (optionalMatching.isEmpty()) {
            throw new MatchingNotFound();
        }
        Matching matching = optionalMatching.get();

        if (matching.getReceiver() != receiver) {
            throw new UserNotAllowed();
        }
        return matching;
    }

    public Matching acceptMatching(Long receiverId, Long matchingId) {
        Matching matching = checkReceiverAndGetMatching(receiverId, matchingId);

        matching.accept();

        return matching;
    }
}
