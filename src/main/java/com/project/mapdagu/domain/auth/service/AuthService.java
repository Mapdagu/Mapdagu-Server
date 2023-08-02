package com.project.mapdagu.domain.auth.service;

import com.project.mapdagu.domain.member.dto.request.SignUpRequestDto;
import com.project.mapdagu.domain.member.dto.response.SignUpResponseDto;
import com.project.mapdagu.domain.member.entity.Member;
import com.project.mapdagu.domain.member.repository.MemberRepository;
import com.project.mapdagu.error.exception.custom.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.project.mapdagu.error.ErrorCode.ALREADY_EXIST_EMAIL;
import static com.project.mapdagu.error.ErrorCode.ALREADY_EXIST_USERNAME;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public SignUpResponseDto signUp(SignUpRequestDto signUpRequestDto) {

        if (memberRepository.existsByEmail(signUpRequestDto.email())) {
            throw new BusinessException(ALREADY_EXIST_EMAIL);
        }
        if (memberRepository.existsByUserName(signUpRequestDto.userName())) {
            throw new BusinessException(ALREADY_EXIST_USERNAME);
        }

        Member member = signUpRequestDto.toEntity();
        member.passwordEncode(passwordEncoder);
        memberRepository.save(member);
        return SignUpResponseDto.of(member.getId(), member.getUserName());
    }
}
