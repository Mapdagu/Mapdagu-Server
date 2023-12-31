package com.project.mapdagu.domain.auth.dto.request;

import com.project.mapdagu.domain.member.entity.Member;
import com.project.mapdagu.domain.member.entity.Role;

public record SignUpRequestDto(String nickname, String email, String password,
                               String userName, Integer imageNum, String intro) {

    public Member toEntity() {
        return Member.builder()
                .nickname(nickname)
                .email(email)
                .password(password)
                .userName(userName)
                .imageNum(imageNum)
                .intro(intro)
                .role(Role.NOT_TEST_USER)
                .build();
    }
}
