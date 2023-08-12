package com.project.mapdagu.domain.auth.dto.response;

import com.project.mapdagu.domain.member.entity.Role;

public record LoginResponse(Role role) {
    public static LoginResponse of(String key){
        return new LoginResponse(Role.valueOf(key));
    }
}
