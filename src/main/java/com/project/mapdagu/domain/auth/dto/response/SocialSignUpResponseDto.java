package com.project.mapdagu.domain.auth.dto.response;

import com.project.mapdagu.domain.member.entity.Role;

public record SocialSignUpResponseDto(Role role) {

    public static SocialSignUpResponseDto of(Role role){
        return new SocialSignUpResponseDto(role);
    }
}
