package com.project.mapdagu.domain.member.dto.response;

public record SignUpResponseDto(Long id, String userName) {
    public static SignUpResponseDto of(Long id, String userName){
        return new SignUpResponseDto(id, userName);
    }
}
