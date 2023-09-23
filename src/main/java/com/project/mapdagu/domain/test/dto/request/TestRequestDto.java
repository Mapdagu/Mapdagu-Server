package com.project.mapdagu.domain.test.dto.request;

import com.project.mapdagu.domain.evaluation.entity.Evaluation;
import com.project.mapdagu.domain.food.entity.Food;
import com.project.mapdagu.domain.member.entity.Member;

import java.util.List;

public record TestRequestDto(String name, Integer score, List<TestRequestDto> dtoList) {

    public Evaluation toEntity(Member member, Food food) {
        return Evaluation.builder()
                .food(food)
                .member(member)
                .score(score)
                .build();
    }
}
