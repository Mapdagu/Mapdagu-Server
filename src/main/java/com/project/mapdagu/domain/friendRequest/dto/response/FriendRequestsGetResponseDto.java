package com.project.mapdagu.domain.friendRequest.dto.response;

import com.project.mapdagu.domain.friendRequest.entity.FriendRequest;

public record FriendRequestsGetResponseDto(Long id, String userName, Integer imageNum, Integer level) {

    public static FriendRequestsGetResponseDto from(FriendRequest friendRequest){
        return new FriendRequestsGetResponseDto(friendRequest.getFromMember().getId(), friendRequest.getFromMember().getUserName(),
                friendRequest.getFromMember().getImageNum(), friendRequest.getFromMember().getLevel());
    }
}
