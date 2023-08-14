package com.project.mapdagu.domain.auth.dto.response;

import com.project.mapdagu.domain.member.entity.Role;

public record LoginResponseDto(Role role) {
    public static LoginResponseDto of(String key){
        return new LoginResponseDto(Role.valueOf(key));
    }
}
