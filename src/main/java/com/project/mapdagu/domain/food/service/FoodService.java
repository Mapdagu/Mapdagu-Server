package com.project.mapdagu.domain.food.service;

import com.project.mapdagu.domain.food.dto.response.FoodSearchResponseDto;
import com.project.mapdagu.domain.food.entity.Food;
import com.project.mapdagu.domain.food.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FoodService {

    private final FoodRepository foodRepository;

    public Slice<FoodSearchResponseDto> searchFood(String email, String search, Pageable pageable) {
        Slice<Food> result = foodRepository.findByNameContaining(search);
        Slice<FoodSearchResponseDto> response = result.map(f -> FoodSearchResponseDto.from(f));
        return response;
    }
}
