package com.project.mapdagu.domain.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    // 첫 로그인 구분
    GUEST("ROLE_GUEST"), USER("ROLE_USER");

    private final String key;   // 스프링 시큐리티 권한 코드에 ROLE_ 접두사가 붙어야 함
}
