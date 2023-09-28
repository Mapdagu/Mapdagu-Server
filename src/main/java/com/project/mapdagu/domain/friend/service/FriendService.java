package com.project.mapdagu.domain.friend.service;

import com.project.mapdagu.domain.friend.dto.response.FriendGetAllResponseDto;
import com.project.mapdagu.domain.friend.dto.response.FriendSearchResponseDto;
import com.project.mapdagu.domain.friend.entity.Friend;
import com.project.mapdagu.domain.friend.repository.FriendRepository;
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
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class FriendService {

    private final MemberRepository memberRepository;
    private final FriendRepository friendRepository;
    private final FriendRequestRepository friendRequestRepository;

    @Transactional(readOnly = true)
    public Slice<FriendSearchResponseDto> searchMember(String email, String search, Pageable pageable) {
        Slice<Member> members = memberRepository.findByUserNameContaining(search, pageable);
        Slice<FriendSearchResponseDto> response = members.map(m -> FriendSearchResponseDto.from(m));
        return response;
    }

    public void saveFriend(String email, Long friendId) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        Member friend = memberRepository.findById(friendId).orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        if (friendRepository.findByMemberAndFriend(member, friend).isPresent() || friendRepository.findByMemberAndFriend(friend, member).isPresent())
            throw new BusinessException(ErrorCode.ALREADY_EXIST_FRIEND);
        FriendRequest friendRequest = friendRequestRepository.findByFromMemberAndToMember(friend, member).orElseThrow(() -> new BusinessException(ErrorCode.FRIEND_REQUEST_NOT_FOUND));
        friendRequestRepository.delete(friendRequest);
        friendRepository.save(new Friend(member, friend));
    }

    public void deleteFriend(String email, Long friendId) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        Member friend = memberRepository.findById(friendId).orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        Friend findFriend = friendRepository.findByMemberAndFriend(member, friend).orElse(null);
        if (findFriend == null) {
            Friend findFriend2 = friendRepository.findByMemberAndFriend(friend, member).orElseThrow(() -> new BusinessException(ErrorCode.FRIEND_NOT_FOUND));
            friendRepository.delete(findFriend2);
        }
        else {
            friendRepository.delete(findFriend);
        }
    }

    public Slice<FriendGetAllResponseDto> getFriends(String email, Pageable pageable) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        List<Member> friends1 = friendRepository.findAllFriendByMemberId(member.getId());
        List<Member> friends2 = friendRepository.findAllMemberByFriendId(member.getId());
        List<Member> friendList = new ArrayList<>();
        friendList.addAll(friends1);
        friendList.addAll(friends2);
        friendList.sort(Comparator.comparing(Member::getLevel).reversed());
        Slice<Member> response = new SliceImpl<>(friendList, pageable, false);
        return response.map(m -> FriendGetAllResponseDto.from(m));
    }
}
