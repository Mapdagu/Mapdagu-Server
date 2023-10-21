package com.project.mapdagu.domain.evaluation.dto.response;

import com.project.mapdagu.domain.evaluation.entity.Evaluation;

public record EvaluationGetResponseDto(String name, String image, Integer score) {

    public static EvaluationGetResponseDto from(Evaluation evaluation) {
        return new EvaluationGetResponseDto(evaluation.getFood().getName(), evaluation.getFood().getImage(), evaluation.getScore());
    }
}
