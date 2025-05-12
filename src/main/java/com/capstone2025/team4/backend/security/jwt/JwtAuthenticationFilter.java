package com.capstone2025.team4.backend.security.jwt;
import io.jsonwebtoken.ExpiredJwtException;

import com.capstone2025.team4.backend.security.CustomUserDetailService;
import com.capstone2025.team4.backend.security.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String NEW_JWT_TOKEN = "New-Jwt-Token";
    private final JwtService jwtService;
    private final CustomUserDetailService userDetailsService;
    private final String BEARER = "Bearer ";

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        // 헤더 확인
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith(BEARER)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(BEARER.length());
        try {
            String email = jwtService.extractUserEmail(jwt);
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                CustomUserDetails userDetails = userDetailsService.loadUserByUsername(email);
                if (jwtService.isJwtTokenValid(jwt, userDetails.getEmail())) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (ExpiredJwtException e) {
            try {
                String email = e.getClaims().getSubject();
                reissueAccessToken(request, response, email, jwt);
            } catch (Exception ex) {
                throw ex;
            }
        } catch (Exception ex) {
            throw ex;
        }
        filterChain.doFilter(request, response);
    }

    private void reissueAccessToken(HttpServletRequest request, HttpServletResponse response, String email, String curJwt) {
        String refreshToken = getRefreshToken(request);

        if (refreshToken == null) return;
        jwtService.validateRefreshToken(refreshToken, email);

        String newJwt = jwtService.recreateJwtToken(email, curJwt);
        response.setHeader(NEW_JWT_TOKEN, BEARER + newJwt);
        CustomUserDetails userDetails =
            userDetailsService.loadUserByUsername(jwtService.extractUserEmail(newJwt));
        UsernamePasswordAuthenticationToken authToken =
            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    private String getRefreshToken(HttpServletRequest request) {
        String refreshToken = request.getHeader("Refresh-Token");

        if (refreshToken == null) {
            return null;
        }
        return refreshToken;
    }
}