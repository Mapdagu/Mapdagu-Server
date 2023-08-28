package com.project.mapdagu.domain.member.dto.response;

public record MemberReadMainResponseDto(String userName, Integer level) {

    public static MemberReadMainResponseDto of(String userName, Integer level){
        return new MemberReadMainResponseDto(userName, level);
    }
}
