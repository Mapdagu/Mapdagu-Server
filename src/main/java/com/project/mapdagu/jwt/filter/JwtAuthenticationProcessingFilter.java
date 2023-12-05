package com.project.mapdagu.jwt.filter;

import com.project.mapdagu.domain.member.entity.Member;
import com.project.mapdagu.domain.member.repository.MemberRepository;
import com.project.mapdagu.error.ErrorCode;
import com.project.mapdagu.error.exception.custom.TokenException;
import com.project.mapdagu.jwt.service.JwtService;
import com.project.mapdagu.jwt.util.PasswordUtil;
import com.project.mapdagu.util.RedisUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.project.mapdagu.error.ErrorCode.ALREADY_LOGOUT_MEMBER;

/**
 * Jwt 인증 필터
 * "/login" 이외의 URI 요청이 왔을 때 처리하는 필터
 */
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

    private static final Set<String> NO_CHECK_URLS = new HashSet<>(Arrays.asList("/auth/login", "/auth/reIssue")); // "/login"으로 들어오는 요청은 Filter 작동 X
    private final JwtService jwtService;

    /**
     * JWT 인증 필터
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (NO_CHECK_URLS.stream().anyMatch(url -> url.equals(request.getRequestURI()))) {
            filterChain.doFilter(request, response); // NO_CHECK_URLS 요청이 들어오면, 다음 필터 호출
            return;
        }
        log.info("uri = {}, query = {}", request.getRequestURI(), request.getQueryString());
        log.info("JwtAuthenticationProcessingFilter 호출");

        String accessToken = jwtService.extractAccessToken(request).orElse(null);
        if (jwtService.isTokenValid(accessToken)) {
            jwtService.getAuthentication(accessToken);
            filterChain.doFilter(request, response);
            return;
        }

        log.info("유효한 JWT 토큰이 없습니다. uri: {}, {}", request.getRequestURI(), accessToken);
        filterChain.doFilter(request, response);
    }

}