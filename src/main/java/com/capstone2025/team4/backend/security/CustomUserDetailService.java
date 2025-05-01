package com.capstone2025.team4.backend.security;

import com.capstone2025.team4.backend.domain.User;
import com.capstone2025.team4.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> byEmail = userRepository.findByEmail(email);

        if (byEmail.isEmpty()) {
            throw new UsernameNotFoundException("해당 이메일로 회원 가입된 사람이 없습니다");
        }

        return new CustomUserDetails(byEmail.get());
    }
}
