package com.project.mapdagu.domain.member.dto.response;

public record MemberUserNameResponseDto(Boolean isDuplicated) {

    public static MemberUserNameResponseDto of(Boolean isDuplicated){
        return new MemberUserNameResponseDto(isDuplicated);
    }
}
