package com.project.mapdagu.domain.friend.service;

import com.project.mapdagu.domain.friend.dto.response.FriendSearchResponseDto;
import com.project.mapdagu.domain.member.entity.Member;
import com.project.mapdagu.domain.member.repository.MemberRepository;
import com.project.mapdagu.error.ErrorCode;
import com.project.mapdagu.error.exception.custom.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class FriendService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public Slice<FriendSearchResponseDto> searchMember(String email, String search, Pageable pageable) {
        Slice<Member> members = memberRepository.findByUserNameContaining(search, pageable);
        Slice<FriendSearchResponseDto> response = members.map(m -> FriendSearchResponseDto.from(m));
        return response;
    }
}
