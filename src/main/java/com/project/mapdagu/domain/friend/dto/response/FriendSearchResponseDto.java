package com.project.mapdagu.domain.friend.dto.response;

import com.project.mapdagu.domain.member.entity.Member;

public record FriendSearchResponseDto(Long id, Integer imageNum, String userName, Integer level) {

    public static FriendSearchResponseDto from(Member member){
        return new FriendSearchResponseDto(member.getId(), member.getImageNum(), member.getUserName(), member.getLevel());
    }
}
