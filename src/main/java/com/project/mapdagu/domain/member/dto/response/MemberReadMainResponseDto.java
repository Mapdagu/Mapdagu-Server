package com.project.mapdagu.domain.member.dto.response;

public record MemberReadMainResponseDto(String userName, Integer level, Integer imageNum) {

    public static MemberReadMainResponseDto of(String userName, Integer level, Integer imageNum){
        return new MemberReadMainResponseDto(userName, level, imageNum);
    }
}
