package com.project.mapdagu.domain.auth.dto.response;

public record SignUpResponseDto(Long id, String userName) {
    public static SignUpResponseDto of(Long id, String userName){
        return new SignUpResponseDto(id, userName);
    }
}
