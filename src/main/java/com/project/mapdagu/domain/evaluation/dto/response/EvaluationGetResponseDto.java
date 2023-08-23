package com.project.mapdagu.domain.evaluation.dto.response;

import com.project.mapdagu.domain.evaluation.entity.Evaluation;

public record EvaluationGetResponseDto(String name, Integer score) {

    public static EvaluationGetResponseDto from(Evaluation evaluation) {
        return new EvaluationGetResponseDto(evaluation.getFood().getName(), evaluation.getScore());
    }
}