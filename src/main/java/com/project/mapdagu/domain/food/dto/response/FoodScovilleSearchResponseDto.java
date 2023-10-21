package com.project.mapdagu.domain.food.dto.response;

import com.project.mapdagu.domain.food.entity.Food;

public record FoodScovilleSearchResponseDto(String name, String image, Integer scoville) {

    public static FoodScovilleSearchResponseDto from(Food food) {
        return new FoodScovilleSearchResponseDto(food.getName(), food.getImage(), food.getScoville());
    }
}
