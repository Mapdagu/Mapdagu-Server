package com.project.mapdagu.domain.food.dto.response;

import com.project.mapdagu.domain.food.entity.Food;

public record FoodSearchResponseDto(Long id, String name, String image, Integer scoville) {
    public static FoodSearchResponseDto from(Food food) {
        return new FoodSearchResponseDto(food.getId(), food.getName(), food.getImage(), food.getScoville());
    }
}
