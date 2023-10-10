package com.project.mapdagu.domain.food.dto.response;

import com.project.mapdagu.domain.food.entity.Food;

public record FoodGetResponseDto(Long id, String name, Integer imageNum, Boolean isEvaluated, Integer score) {

    public static FoodGetResponseDto of(Food food, Boolean isEvaluated, Integer score) {
        return new FoodGetResponseDto(food.getId(), food.getName(), food.getImageNum(), isEvaluated, score);
    }
}
