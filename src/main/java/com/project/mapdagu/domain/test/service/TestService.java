package com.project.mapdagu.domain.test.service;

import com.project.mapdagu.domain.member.entity.Member;
import com.project.mapdagu.domain.member.entity.Role;
import com.project.mapdagu.domain.member.repository.MemberRepository;
import com.project.mapdagu.domain.test.dto.request.TestInfoRequestDto;
import com.project.mapdagu.error.exception.custom.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.project.mapdagu.error.ErrorCode.MEMBER_NOT_FOUND;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TestService {

    private final MemberRepository memberRepository;

    public void saveTestInfo(String email, TestInfoRequestDto testInfoRequestDto) {
        log.info("save TestInfo");
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));
        member.updateTestInfo(testInfoRequestDto.scoville(), testInfoRequestDto.level(), Role.USER);
    }
}
