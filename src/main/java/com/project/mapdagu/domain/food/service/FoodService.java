package com.project.mapdagu.domain.food.service;

import com.project.mapdagu.domain.evaluation.entity.Evaluation;
import com.project.mapdagu.domain.evaluation.repository.EvaluationRepository;
import com.project.mapdagu.domain.food.dto.response.FoodGetResponseDto;
import com.project.mapdagu.domain.food.dto.response.FoodScovilleSearchResponseDto;
import com.project.mapdagu.domain.food.dto.response.FoodSearchResponseDto;
import com.project.mapdagu.domain.food.entity.Food;
import com.project.mapdagu.domain.food.repository.FoodRepository;
import com.project.mapdagu.domain.member.entity.Member;
import com.project.mapdagu.domain.member.repository.MemberRepository;
import com.project.mapdagu.error.ErrorCode;
import com.project.mapdagu.error.exception.custom.BusinessException;
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
    private final EvaluationRepository evaluationRepository;
    private final MemberRepository memberRepository;

    public Slice<FoodSearchResponseDto> searchFood(String search, Pageable pageable) {
        Slice<Food> result = foodRepository.findByNameContaining(search);
        Slice<FoodSearchResponseDto> response = result.map(f -> FoodSearchResponseDto.from(f));
        return response;
    }

    public FoodScovilleSearchResponseDto searchFoodScoville(String search) {
        Food food = foodRepository.findByName(search).orElseThrow(() -> new BusinessException(ErrorCode.FOOD_NOT_FOUND));
        FoodScovilleSearchResponseDto response = FoodScovilleSearchResponseDto.from(food);
        return response;
    }

    public FoodGetResponseDto getOneFood(String email, Long foodId) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        Food food = foodRepository.findById(foodId).orElseThrow(() -> new BusinessException(ErrorCode.FOOD_NOT_FOUND));
        Evaluation evaluation = evaluationRepository.findByMemberIdAndFoodId(member.getId(), foodId).orElse(null);
        Boolean isEvaluated = false;
        Integer score = null;
        if (evaluation != null) {
            isEvaluated = true;
            score = evaluation.getScore();
        }
        FoodGetResponseDto response = FoodGetResponseDto.of(food, isEvaluated, score);
        return response;
    }
}
