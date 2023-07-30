package com.project.mapdagu.domain.member.entity;

import com.project.mapdagu.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private String name;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Enumerated(EnumType.STRING)
    private SocialType socialType;  // KAKAO, NAVER, GOOGLE
    private String socialId;    // 로그인한 소셜 타입 식별자 값 (일반 로그인의 경우 null)
    private String nickname;
    private String imageNum;

    @Builder
    public Member(String email, String password, String name, Role role, SocialType socialType, String socialId, String nickname, String imageNum) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
        this.socialType = socialType;
        this.socialId = socialId;
        this.nickname = nickname;
        this.imageNum = imageNum;
    }
}
