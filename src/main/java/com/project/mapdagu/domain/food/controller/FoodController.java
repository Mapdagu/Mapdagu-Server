package com.project.mapdagu.domain.food.controller;

import com.project.mapdagu.common.dto.ResponseDto;
import com.project.mapdagu.common.dto.SliceResponseDto;
import com.project.mapdagu.domain.evaluation.dto.response.EvaluationGetResponseDto;
import com.project.mapdagu.domain.food.dto.response.FoodGetResponseDto;
import com.project.mapdagu.domain.food.dto.response.FoodScovilleSearchResponseDto;
import com.project.mapdagu.domain.food.dto.response.FoodSearchResponseDto;
import com.project.mapdagu.domain.food.service.FoodService;
import com.project.mapdagu.error.ErrorCode;
import com.project.mapdagu.error.dto.ErrorResponse;
import com.project.mapdagu.error.exception.custom.BusinessException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

@Tag(name = "Food", description = "Food API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/foods")
public class FoodController {

    private final FoodService foodService;

    @Operation(summary = "음식 검색", description = "음식을 검색합니다.",
            security = { @SecurityRequirement(name = "bearer-key") },
            responses = {
                    @ApiResponse(responseCode = "200", description = "음식 검색 성공")
                    , @ApiResponse(responseCode = "401", description = "인증에 실패했습니다.")
                    , @ApiResponse(responseCode = "400", description = "검색어를 입력해야 합니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @GetMapping
    public ResponseEntity<SliceResponseDto> searchFood(@RequestParam String search, Pageable pageable) {
        if (StringUtils.isEmpty(search)) {
            throw new BusinessException(ErrorCode.WRONG_SEARCH);
        }
        Slice<FoodSearchResponseDto> response = foodService.searchFood(search, pageable);
        return SliceResponseDto.ok(response);
    }

    @Operation(summary = "음식 스코빌 지수 검색", description = "음식 스코빌 지수를 검색합니다.",
            security = { @SecurityRequirement(name = "bearer-key") },
            responses = {
                    @ApiResponse(responseCode = "200", description = "음식 스코빌 지수 검색 성공")
                    , @ApiResponse(responseCode = "401", description = "인증에 실패했습니다.")
                    , @ApiResponse(responseCode = "400", description = "검색어를 입력해야 합니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
                    , @ApiResponse(responseCode = "404", description = "해당 음식을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @GetMapping("/scoville")
    public ResponseEntity<FoodScovilleSearchResponseDto> searchFoodScoville(@RequestParam String search) {
        if (StringUtils.isEmpty(search)) {
            throw new BusinessException(ErrorCode.WRONG_SEARCH);
        }
        FoodScovilleSearchResponseDto response = foodService.searchFoodScoville(search);
        return ResponseDto.ok(response);
    }

    @Operation(summary = "음식 상세 정보 조회", description = "음식 상세 정보를 id로 조회합니다.",
            security = { @SecurityRequirement(name = "bearer-key") },
            responses = {
                    @ApiResponse(responseCode = "200", description = "음식 상세 정보 조회 성공")
                    , @ApiResponse(responseCode = "401", description = "인증에 실패했습니다.")
                    , @ApiResponse(responseCode = "404", description = "1. 해당 회원을 찾을 수 없습니다. \t\n 2. 해당 평가를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @GetMapping("/{foodId}")
    public ResponseEntity<FoodGetResponseDto> getOneFood(@AuthenticationPrincipal UserDetails loginUser, @PathVariable Long foodId) {
        FoodGetResponseDto response = foodService.getOneFood(loginUser.getUsername(), foodId);
        return ResponseDto.ok(response);
    }
}
