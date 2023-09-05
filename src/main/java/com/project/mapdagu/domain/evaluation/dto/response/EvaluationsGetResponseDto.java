package com.project.mapdagu.domain.evaluation.dto.response;

import com.project.mapdagu.domain.evaluation.entity.Evaluation;

public record EvaluationsGetResponseDto(Long id, String name, Integer imageNum, Integer score) {

    public static EvaluationsGetResponseDto from(Evaluation evaluation) {
        return new EvaluationsGetResponseDto(evaluation.getId(), evaluation.getFood().getName(), evaluation.getFood().getImageNum(), evaluation.getScore());
    }
}
