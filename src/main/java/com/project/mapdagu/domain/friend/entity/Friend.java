package com.project.mapdagu.domain.friend.entity;

import com.project.mapdagu.common.entity.BaseTimeEntity;
import com.project.mapdagu.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Friend extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friendId")
    private Member friend;

    @Builder
    public Friend(Member member, Member friend) {
        this.member = member;
        this.friend = friend;
    }
}
