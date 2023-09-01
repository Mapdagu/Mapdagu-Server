package com.project.mapdagu.domain.food.dto.response;

import com.project.mapdagu.domain.food.entity.Food;

public record FoodSearchResponseDto(String name, Integer imageNum, Integer scoville) {
    public static FoodSearchResponseDto from(Food food) {
        return new FoodSearchResponseDto(food.getName(), food.getImageNum(), food.getScoville());
    }
}
