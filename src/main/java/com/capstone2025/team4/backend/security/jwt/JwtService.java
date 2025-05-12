package com.capstone2025.team4.backend.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

import static org.springframework.util.StringUtils.*;

@Component
public class JwtService {

    private final String SECRET_KEY;
    // 토큰 만료시간
//    public final long TOKEN_TIME = 30 * 60 * 1000L; // 30 분
    public final long TOKEN_TIME = 1000L; // 30 분
    public final long REFRESH_TIME = 24 * 60 * 60 * 1000L; // 24시간 (밀리초 단위)
    private final UserRefreshTokenRepository userRefreshTokenRepository;

    public JwtService(@Value("${jwt.secret.key}") String SECRET_KEY, UserRefreshTokenRepository userRefreshTokenRepository) {
        this.SECRET_KEY = SECRET_KEY;
        this.userRefreshTokenRepository = userRefreshTokenRepository;
    }


    public String generateJwtToken(String email) {
        return Jwts.builder()
                .subject(email) // 사용자 식별값, PK든 id값이든 이름이든 상관 X
//                .claim() //사용자 권한 (key, value-권한값)형태
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + TOKEN_TIME))
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    public boolean isJwtTokenValid(String token, String emailToCheck) {
        final String userEmail = extractUserEmail(token);
        return (userEmail.equals(emailToCheck)) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        Date expireDate = extractClaim(token, Claims::getExpiration);
        return expireDate.before(new Date());
    }

    public String extractUserEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey()).build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateRefreshToken() {
        return Jwts.builder()
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + REFRESH_TIME))
                .compact();
    }

    @Transactional(readOnly = true)
    public void validateRefreshToken(String refreshToken, String email) {
        extractAllClaims(refreshToken);
//        userRefreshTokenRepository.findByUserEmailAndReissueCountLessThan(email, REISSUE_LIMIT)
        UserRefreshToken urt = userRefreshTokenRepository.findByUserEmail(email)
                .orElseThrow(() -> new ExpiredJwtException(null, null, "저장된 Refresh-Token이 없습니다."));

        if (!urt.validateRefreshToken(refreshToken)) {
            throw new JwtException("서버에[ 저장된 Refresh-Token과 일치하지 않습니다");
        }
    }

    @Transactional
    public String recreateJwtToken(String email, String curJwt) {
        userRefreshTokenRepository.findByUserEmail(email)
                .orElseThrow(() -> new ExpiredJwtException(null, null, "Refresh Token Expired"));

        return generateJwtToken(email);
    }
}