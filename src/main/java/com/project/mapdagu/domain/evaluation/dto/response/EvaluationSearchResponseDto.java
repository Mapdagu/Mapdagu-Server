package com.project.mapdagu.domain.evaluation.dto.response;

import com.project.mapdagu.domain.evaluation.entity.Evaluation;

public record EvaluationSearchResponseDto(String name, String image, Integer score) {
    public static EvaluationSearchResponseDto from(Evaluation evaluation) {
        return new EvaluationSearchResponseDto(evaluation.getFood().getName(), evaluation.getFood().getImage(), evaluation.getScore());
    }

}
