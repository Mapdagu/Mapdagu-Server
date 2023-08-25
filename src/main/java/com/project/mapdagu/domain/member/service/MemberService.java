package com.project.mapdagu.domain.member.service;

import com.project.mapdagu.domain.member.dto.request.MemberUpdateInfoRequestDto;
import com.project.mapdagu.domain.member.entity.Member;
import com.project.mapdagu.domain.member.repository.MemberRepository;
import com.project.mapdagu.error.ErrorCode;
import com.project.mapdagu.error.exception.custom.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public void updateMemberInfo(String email, MemberUpdateInfoRequestDto requestDto) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        member.updateMemberInfo(requestDto.imageNum(), requestDto.userName(), requestDto.intro());
    }
}
