package com.project.mapdagu.domain.evaluation.dto.request;

import com.project.mapdagu.domain.evaluation.entity.Evaluation;
import com.project.mapdagu.domain.food.entity.Food;
import com.project.mapdagu.domain.member.entity.Member;

public record EvaluationSaveRequestDto(String name, Integer score) {
    public Evaluation toEntity(Member member, Food food, Integer score) {
        return Evaluation.builder()
                .food(food)
                .member(member)
                .score(score)
                .build();
    }
}
