package com.project.mapdagu.domain.friendRequest.service;

import com.project.mapdagu.domain.friendRequest.dto.response.FriendRequestsGetResponseDto;
import com.project.mapdagu.domain.friendRequest.entity.FriendRequest;
import com.project.mapdagu.domain.friendRequest.repository.FriendRequestRepository;
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
public class FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;
    private final MemberRepository memberRepository;

    public void saveFriendRequest(String email, Long friendId) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        Member friend = memberRepository.findById(friendId).orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        if (friendRequestRepository.findByFromMemberAndToMember(member, friend).isPresent())
            throw new BusinessException(ErrorCode.ALREADY_EXIST_FRIEND_REQUEST);
        friendRequestRepository.save(new FriendRequest(member, friend));
    }

    public void deleteFriendRequest(String email, Long friendId) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        Member friend = memberRepository.findById(friendId).orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        FriendRequest friendRequest = friendRequestRepository.findByFromMemberAndToMember(member, friend).orElseThrow(() -> new BusinessException(ErrorCode.FRIEND_REQUEST_NOT_FOUND));
        friendRequestRepository.delete(friendRequest);
    }

    public Slice<FriendRequestsGetResponseDto> getAllFriendRequest(String email, Pageable pageable) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        Slice<FriendRequest> friendRequests = friendRequestRepository.findAllByToMemberQuery(member.getId(), pageable);
        Slice<FriendRequestsGetResponseDto> response = friendRequests.map(f -> FriendRequestsGetResponseDto.from(f));
        return response;
    }
}
