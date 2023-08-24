package com.project.mapdagu.domain.evaluation.dto.response;

import com.project.mapdagu.domain.evaluation.entity.Evaluation;

public record EvaluationSearchResponseDto(String name, Integer imageNum, Integer score) {
    public static EvaluationSearchResponseDto from(Evaluation evaluation) {
        return new EvaluationSearchResponseDto(evaluation.getFood().getName(), evaluation.getFood().getImageNum(), evaluation.getScore());
    }

}
