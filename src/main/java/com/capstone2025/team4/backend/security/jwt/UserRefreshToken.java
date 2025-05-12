package com.capstone2025.team4.backend.security.jwt;

import com.capstone2025.team4.backend.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class UserRefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_email", referencedColumnName = "email", unique = true)
    private User user;

    private String refreshToken;

    private String prevJwtToken;

    public UserRefreshToken(User user, String refreshToken) {
        this.user = user;
        this.refreshToken = refreshToken;
    }

    public UserRefreshToken(User user, String refreshToken, String prevJwtToken) {
        this.user = user;
        this.refreshToken = refreshToken;
        this.prevJwtToken = prevJwtToken;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public boolean validateRefreshToken(String refreshToken) {
        return this.refreshToken.equals(refreshToken);
    }

    public void updatePrevJwt(String prevJwtToken) {
        this.prevJwtToken = prevJwtToken;
    }
}
