package com.project.mapdagu.domain.member.entity;

import com.project.mapdagu.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nickname;
    private String email;
    private String password;
    private String userName;
    private int imageNum;
    private String intro;
    private int scoville;
    private int level;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Enumerated(EnumType.STRING)
    private SocialType socialType;  // KAKAO, NAVER, GOOGLE
    private String socialId;    // 로그인한 소셜 타입 식별자 값 (일반 로그인의 경우 null)

    @Builder
    public Member(String nickname, String email, String password, String userName, int imageNum, String intro, Role role, SocialType socialType, String socialId) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.imageNum = imageNum;
        this.intro = intro;
        this.role = role;
        this.socialType = socialType;
        this.socialId = socialId;
    }

    public void passwordEncode(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    public void updateSocialMember(String userName, int imageNum, String intro, Role role) {
        this.userName = userName;
        this.imageNum = imageNum;
        this.intro = intro;
        this.role = role;
    }
}
