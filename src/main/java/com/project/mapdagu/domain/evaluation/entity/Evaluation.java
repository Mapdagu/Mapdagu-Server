package com.project.mapdagu.domain.evaluation.entity;

import com.project.mapdagu.common.entity.BaseTimeEntity;
import com.project.mapdagu.domain.food.entity.Food;
import com.project.mapdagu.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Evaluation extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "foodId")
    private Food food;

    private Integer score;

    @Builder
    public Evaluation(Member member, Food food, Integer score) {
        this.member = member;
        this.food = food;
        this.score = score;
    }

    public void updateTestScore(Integer score) {
        this.score = score;
    }

    public void updateEvaluation(Integer score) { this.score = score;}
}
