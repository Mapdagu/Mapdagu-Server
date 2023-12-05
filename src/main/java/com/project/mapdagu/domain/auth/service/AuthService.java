package com.project.mapdagu.domain.auth.service;

import com.project.mapdagu.domain.auth.dto.request.RefreshTokenRequest;
import com.project.mapdagu.domain.auth.dto.request.SignUpRequestDto;
import com.project.mapdagu.domain.auth.dto.request.SocialSignUpRequestDto;
import com.project.mapdagu.domain.auth.dto.response.SocialSignUpResponseDto;
import com.project.mapdagu.domain.member.entity.Member;
import com.project.mapdagu.domain.member.entity.Role;
import com.project.mapdagu.domain.member.repository.MemberRepository;
import com.project.mapdagu.error.exception.custom.BusinessException;
import com.project.mapdagu.error.exception.custom.TokenException;
import com.project.mapdagu.jwt.service.JwtService;
import com.project.mapdagu.util.RedisUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.project.mapdagu.error.ErrorCode.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RedisUtil redisUtil;

    public void signUp(SignUpRequestDto signUpRequestDto) {

        if (memberRepository.existsByEmail(signUpRequestDto.email())) {
            throw new BusinessException(ALREADY_EXIST_EMAIL);
        }
        if (memberRepository.existsByUserName(signUpRequestDto.userName())) {
            throw new BusinessException(ALREADY_EXIST_USERNAME);
        }

        Member member = signUpRequestDto.toEntity();
        member.passwordEncode(passwordEncoder);
        memberRepository.save(member);
    }

    public SocialSignUpResponseDto socialSignUp(SocialSignUpRequestDto socialSignUpRequestDto, HttpServletRequest request, HttpServletResponse response) {

        log.info("socialSignUp 호출");
        // 회원 이름 중복 확인
        if (memberRepository.existsByUserName(socialSignUpRequestDto.userName())) {
            throw new BusinessException(ALREADY_EXIST_USERNAME);
        }
        // AccessToken 으로 회원 찾기
        String accessToken = jwtService.extractAccessToken(request).orElseThrow(() -> new TokenException(INVALID_TOKEN));
        String email = jwtService.extractEmail(accessToken).orElseThrow(() -> new TokenException(INVALID_TOKEN));
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));

        // 추가 정보 업데이트
        member.updateSocialMember(socialSignUpRequestDto.userName(), socialSignUpRequestDto.imageNum(), socialSignUpRequestDto.intro(), Role.NOT_TEST_USER);

        // RefreshToken 생성 후 헤더에 보내기
        String refreshToken = jwtService.createRefreshToken(email);
        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
        jwtService.updateRefreshToken(email, refreshToken);

        return SocialSignUpResponseDto.of(member.getRole());
    }

    public void logout(HttpServletRequest request, String email) {
        log.info("logout 로직 호출");
        String accessToken = jwtService.extractAccessToken(request).orElseThrow(() -> new TokenException(INVALID_TOKEN));

        redisUtil.delete(email);
        redisUtil.setBlackList(accessToken, "accessToken", jwtService.getAccessTokenExpirationPeriod());
    }
}
