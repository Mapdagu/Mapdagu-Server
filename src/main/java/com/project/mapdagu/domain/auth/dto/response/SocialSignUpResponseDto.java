package com.project.mapdagu.domain.auth.dto.response;

import com.project.mapdagu.domain.member.entity.Role;

public record SocialSignUpResponseDto(String userName, Role role) {

    public static SocialSignUpResponseDto of(String userName, Role role){
        return new SocialSignUpResponseDto(userName, role);
    }
}
