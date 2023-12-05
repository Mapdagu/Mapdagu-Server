package com.project.mapdagu.jwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.project.mapdagu.domain.member.entity.Member;
import com.project.mapdagu.domain.member.repository.MemberRepository;
import com.project.mapdagu.error.exception.custom.TokenException;
import com.project.mapdagu.jwt.util.PasswordUtil;
import com.project.mapdagu.util.RedisUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

import static com.project.mapdagu.error.ErrorCode.ALREADY_LOGOUT_MEMBER;
import static com.project.mapdagu.error.ErrorCode.INVALID_TOKEN;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class JwtService {

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.access.expiration}")
    private Integer accessTokenExpirationPeriod;

    @Value("${jwt.refresh.expiration}")
    private Integer refreshTokenExpirationPeriod;

    @Value("${jwt.access.header}")
    private String accessHeader;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String EMAIL_CLAIM = "email";
    private static final String BEARER = "Bearer ";

    private final MemberRepository memberRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final RedisUtil redisUtil;
    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    /**
     * AccessToken 생성 메소드
     */
    public String createAccessToken(String email) {
        Date now = new Date();
        return JWT.create()
                .withSubject(ACCESS_TOKEN_SUBJECT)
                .withExpiresAt(new Date(now.getTime() + accessTokenExpirationPeriod)) // 토큰 만료 시간 설정
                .withClaim(EMAIL_CLAIM, email)
                .sign(Algorithm.HMAC512(secretKey));
    }

    /**
     * RefreshToken 생성 메소드
     */
    public String createRefreshToken(String email) {
        Date now = new Date();
        return JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)
                .withExpiresAt(new Date(now.getTime() + refreshTokenExpirationPeriod))
                .withClaim(EMAIL_CLAIM, email)
                .sign(Algorithm.HMAC512(secretKey));
    }

    /**
     * AccessToken 헤더에 실어서 보내기
     */
    public void sendAccessToken(HttpServletResponse response, String accessToken) {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader(accessHeader, accessToken);
        log.info("sendAccessToken 실행");
    }

    public void sendRefreshToken(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie(REFRESH_TOKEN_SUBJECT, refreshToken);
        cookie.setMaxAge(refreshTokenExpirationPeriod);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     * 로그인 시 AccessToken + RefreshToken 헤더에 실어서 보내기
     */
    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);
        setAccessTokenHeader(response, accessToken);
        setRefreshTokenHeader(response, refreshToken);
        log.info("Access Token, Refresh Token 헤더 설정 완료");
    }

    /**
     * 헤더에서 RefreshToken 추출
     */
    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(refreshHeader))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }

    /**
     * 헤더에서 AccessToken 추출
     */
    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(accessHeader))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }

    /**
     * AccessToken에서 Email 추출 -> 유효하지 않다면 빈 Optional 객체 반환
     */
    public Optional<String> extractEmail(String token) {
        try {
            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
                    .build()
                    .verify(token)
                    .getClaim(EMAIL_CLAIM)
                    .asString());
        } catch (Exception e) {
            log.error("토큰이 유효하지 않습니다.");
            return Optional.empty();
        }
    }

    /**
     * AccessToken 헤더 설정
     */
    public void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
        response.setHeader(accessHeader, "Bearer " + accessToken);
    }

    /**
     * RefreshToken 헤더 설정
     */
    public void setRefreshTokenHeader(HttpServletResponse response, String refreshToken) {
        response.setHeader(refreshHeader, "Bearer " + refreshToken);
    }

    /**
     * RefreshToken 저장(업데이트)
     */
    public void updateRefreshToken(String email, String refreshToken) {
        redisUtil.set(email, refreshToken, refreshTokenExpirationPeriod);
    }

    /**
     * 토큰 유효성 검사
     */
    public boolean isTokenValid(String token) {
        try {
            JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);

            if(redisUtil.hasKeyBlackList(token)) {
                throw new TokenException(ALREADY_LOGOUT_MEMBER);
            }
            return true;
        } catch (Exception e) {
            log.info("유효하지 않은 토큰입니다. {}", e.getMessage());
            return false;
        }
    }

    /**
     * AccessToken, RefreshToken 재발급 + 인증 + 응답 헤더에 보내기
     */
    private void reIssueRefreshAndAccessToken(HttpServletResponse response, String refreshToken, String email) {
        String newAccessToken = createAccessToken(email);
        String newRefreshToken = createRefreshToken(email);
        getAuthentication(newAccessToken);
        redisUtil.delete(email);
        updateRefreshToken(email, newRefreshToken);
        sendAccessAndRefreshToken(response, newAccessToken, refreshToken);
        log.info("AccessToken, RefreshToken 재발급 완료");
    }

    public void reIssueToken(HttpServletResponse response, String refreshToken) {
        String email = extractEmail(refreshToken).orElseThrow(() -> new TokenException(INVALID_TOKEN));
        isRefreshTokenMatch(email, refreshToken);
        String newAccessToken = createAccessToken(email);
        String newRefreshToken = createRefreshToken(email);
        getAuthentication(newAccessToken);
        redisUtil.delete(email);
        updateRefreshToken(email, newRefreshToken);
        sendAccessAndRefreshToken(response, newAccessToken, refreshToken);
        log.info("AccessToken, RefreshToken 재발급 완료");
    }


    /**
     * AccessToken 재발급 + 인증 메소드 + 응답 헤더에 보내기
     */
    private void reIssueAccessToken(HttpServletResponse response, String refreshToken, String email) {
        String newAccessToken = createAccessToken(email);
        sendAccessAndRefreshToken(response, newAccessToken, refreshToken);
        getAuthentication(newAccessToken);
        log.info("AccessToken 인증 완료");
    }

    /**
     * RefreshToken 검증 메소드
     */
    public boolean isRefreshTokenMatch(String email, String refreshToken) {
        log.info("RefreshToken 검증");
        if (redisUtil.get(email).equals(refreshToken)) {
            return true;
        }
        throw new TokenException(INVALID_TOKEN);
    }

    /**
     * [인증 처리 메소드]
     * 인증 허가 처리된 객체를 SecurityContextHolder에 담기
     */
    public void getAuthentication(String accessToken) {
        log.info("인증 처리 메소드 getAuthentication() 호출");
        extractEmail(accessToken)
                .ifPresent(email -> memberRepository.findByEmail(email)
                        .ifPresent(this::saveAuthentication));
    }

    /**
     * [인증 허가 메소드]
     * 파라미터의 유저 : 우리가 만든 회원 객체 / 빌더의 유저 : UserDetails의 User 객체
     */
    public void saveAuthentication(Member member) {
        log.info("인증 허가 메소드 saveAuthentication() 호출");
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