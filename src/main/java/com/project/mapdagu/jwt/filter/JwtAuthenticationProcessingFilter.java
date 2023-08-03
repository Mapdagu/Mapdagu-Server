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
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Jwt 인증 필터
 * "/login" 이외의 URI 요청이 왔을 때 처리하는 필터
 */
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

    private static final String NO_CHECK_URL = "/login"; // "/login"으로 들어오는 요청은 Filter 작동 X

    private final JwtService jwtService;
    private final MemberRepository memberRepository;
    private final RedisUtil redisUtil;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, ServletException, IOException {
        if (request.getRequestURI().equals(NO_CHECK_URL)) {
            filterChain.doFilter(request, response); // "/login" 요청이 들어오면, 다음 필터 호출
            return;
        }

        // 사용자 요청 헤더에서 RefreshToken 추출-> RefreshToken이 없거나 유효하지 않다면 null
        String refreshToken = jwtService.extractRefreshToken(request)
                .filter(jwtService::isTokenValid)
                .orElse(null);

        // 리프레시 토큰이 요청 헤더에 존재하고 유효하다면, AccessToken이 만료된 것 -> AccessToken 재발급
        if (refreshToken != null) {
            String email = jwtService.extractEmail(refreshToken).orElseThrow(() -> new TokenException(ErrorCode.INVALID_TOKEN));
            if (isRefreshTokenMatch(email, refreshToken)) {
                String newAccessToken = jwtService.createAccessToken(email);
                String newRefreshToken = jwtService.createRefreshToken(email);
                jwtService.updateRefreshToken(email, newRefreshToken);
                jwtService.sendAccessAndRefreshToken(response, newAccessToken, refreshToken);
            }
            return;
        }

        // AccessToken을 검사하고 인증 처리
        // AccessToken이 없거나 유효하지 않다면, 인증 객체가 담기지 않은 상태로 다음 필터로 넘어가기 때문에 403 에러 발생
        // AccessToken이 유효하다면, 인증 객체가 담긴 상태로 다음 필터로 넘어가기 때문에 인증 성공
        else {
            checkAccessTokenAndAuthentication(request, response, filterChain);
        }
    }

    public boolean isRefreshTokenMatch(String email, String refreshToken) {
        if (redisUtil.get(email).equals(refreshToken)) {
            return true;
        }
        throw new TokenException(ErrorCode.INVALID_TOKEN);
    }

    /**
     * [액세스 토큰 체크 & 인증 처리 메소드]
     * 인증 허가 처리된 객체를 SecurityContextHolder에 담기
     */
    public void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response,
                                                  FilterChain filterChain) throws ServletException, IOException {
        log.info("checkAccessTokenAndAuthentication() 호출");
        jwtService.extractAccessToken(request)
                .filter(jwtService::isTokenValid)
                .ifPresent(accessToken -> jwtService.extractEmail(accessToken)
                        .ifPresent(email -> memberRepository.findByEmail(email)
                                .ifPresent(this::saveAuthentication)));

        filterChain.doFilter(request, response);
    }

    /**
     * [인증 허가 메소드]
     * 파라미터의 유저 : 우리가 만든 회원 객체 / 빌더의 유저 : UserDetails의 User 객체
     */
    public void saveAuthentication(Member member) {
        log.info("saveAuthentication() 호출");
        String password = member.getPassword();
        if (password == null) { // 소셜 로그인 유저의 비밀번호 임의로 설정 하여 소셜 로그인 유저도 인증 되도록 설정
            password = PasswordUtil.generateRandomPassword();
        }

        UserDetails userDetailsUser = org.springframework.security.core.userdetails.User.builder()
                .username(member.getEmail())
                .password(password)
                .roles(member.getRole().name())
                .build();

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetailsUser, null,
                        authoritiesMapper.mapAuthorities(userDetailsUser.getAuthorities()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}