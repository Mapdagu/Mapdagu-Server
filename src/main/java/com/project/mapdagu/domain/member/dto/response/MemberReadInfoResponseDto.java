package com.project.mapdagu.domain.member.dto.response;

public record MemberReadInfoResponseDto(Integer imageNum, String userName, String intro) {

    public static MemberReadInfoResponseDto of(Integer imageNum, String userName, String intro){
        return new MemberReadInfoResponseDto(imageNum, userName, intro);
    }
}
