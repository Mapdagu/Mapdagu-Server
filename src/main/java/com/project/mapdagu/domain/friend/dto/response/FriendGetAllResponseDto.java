package com.project.mapdagu.domain.friend.dto.response;

import com.project.mapdagu.domain.member.entity.Member;

public record FriendGetAllResponseDto(Long id, Integer imageNum, String userName, Integer level) {

    public static FriendGetAllResponseDto from(Member member){
        return new FriendGetAllResponseDto(member.getId(), member.getImageNum(), member.getUserName(), member.getLevel());
    }
}
