package com.project.mapdagu.domain.evaluation.controller;

import com.project.mapdagu.common.dto.PageResponseDto;
import com.project.mapdagu.common.dto.ResponseDto;
import com.project.mapdagu.common.dto.SliceResponseDto;
import com.project.mapdagu.domain.evaluation.dto.request.EvaluationInfoRequestDto;
import com.project.mapdagu.domain.evaluation.dto.request.EvaluationSaveRequestDto;
import com.project.mapdagu.domain.evaluation.dto.response.EvaluationGetResponseDto;
import com.project.mapdagu.domain.evaluation.dto.response.EvaluationSearchResponseDto;
import com.project.mapdagu.domain.evaluation.dto.response.EvaluationsGetResponseDto;
import com.project.mapdagu.domain.evaluation.service.EvaluationService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

@Tag(name = "Evaluation", description = "Evaluation API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/evaluations")
public class EvaluationController {

    private final EvaluationService evaluationService;

    @Operation(summary = "맵기 평가 저장", description = "맵기 평가를 저장합니다.",
            security = { @SecurityRequirement(name = "bearer-key") },
            responses = {
                    @ApiResponse(responseCode = "204", description = "맵기 평가 저장 성공")
                    , @ApiResponse(responseCode = "401", description = "인증에 실패했습니다.")
                    , @ApiResponse(responseCode = "400", description = "이미 존재하는 평가입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
                    , @ApiResponse(responseCode = "404", description = "1. 해당 회원을 찾을 수 없습니다. \t\n 2. 해당 음식을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @PostMapping
    public ResponseEntity<Void> saveEvaluation(@AuthenticationPrincipal UserDetails loginUser, @RequestBody EvaluationSaveRequestDto requestDto) {
        evaluationService.saveEvaluation(loginUser.getUsername(), requestDto);
        return ResponseDto.noContent();
    }

    @Operation(summary = "맵기 평가 후 스코빌지수, 레벨 저장", description = "맵기 평가 후 스코빌지수, 레벨을 저장합니다.",
            security = { @SecurityRequirement(name = "bearer-key") },
            responses = {
                    @ApiResponse(responseCode = "204", description = "스코빌지수, 레벨 저장 성공")
                    , @ApiResponse(responseCode = "401", description = "인증에 실패했습니다.")
                    , @ApiResponse(responseCode = "404", description = "해당 회원을 찾을 수 없습니다", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @PatchMapping("/info")
    public ResponseEntity<Void> saveTestInfo(@AuthenticationPrincipal UserDetails loginUser, @RequestBody EvaluationInfoRequestDto infoRequestDto) {
        evaluationService.saveEvaluationInfo(loginUser.getUsername(), infoRequestDto);
        return ResponseDto.noContent();
    }

    @Operation(summary = "맵기 평가 하나 조회", description = "맵기 평가 하나를 id로 조회합니다.",
            security = { @SecurityRequirement(name = "bearer-key") },
            responses = {
                    @ApiResponse(responseCode = "200", description = "맵기 평가 조회 성공")
                    , @ApiResponse(responseCode = "401", description = "인증에 실패했습니다.")
                    , @ApiResponse(responseCode = "404", description = "1. 해당 회원을 찾을 수 없습니다. \t\n 2. 해당 평가를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @GetMapping("/{evaluationId}")
    public ResponseEntity<EvaluationGetResponseDto> getOneEvaluation(@AuthenticationPrincipal UserDetails loginUser, @PathVariable Long evaluationId) {
        EvaluationGetResponseDto responseDto = evaluationService.getOneEvaluation(loginUser.getUsername(), evaluationId);
        return ResponseDto.ok(responseDto);
    }

    @Operation(summary = "맵기 평가 목록 조회", description = "맵기 평가 목록을 조회합니다.",
            security = { @SecurityRequirement(name = "bearer-key") },
            responses = {
                    @ApiResponse(responseCode = "200", description = "맵기 평가 목록 조회 성공")
                    , @ApiResponse(responseCode = "401", description = "인증에 실패했습니다.")
                    , @ApiResponse(responseCode = "404", description = "해당 회원을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @GetMapping("/me")
    public ResponseEntity<PageResponseDto> getEvaluations(@AuthenticationPrincipal UserDetails loginUser, @PageableDefault(size = 3) Pageable pageable) {
        Page<EvaluationsGetResponseDto> responseDto = evaluationService.getEvaluations(loginUser.getUsername(), pageable);
        return PageResponseDto.of(responseDto);
    }

    @Operation(summary = "내가 평가한 음식 검색", description = "내가 평가한 음식을 검색합니다.",
            security = { @SecurityRequirement(name = "bearer-key") },
            responses = {
                    @ApiResponse(responseCode = "200", description = "평가한 음식 검색 성공")
                    , @ApiResponse(responseCode = "401", description = "인증에 실패했습니다.")
                    , @ApiResponse(responseCode = "400", description = "검색어를 입력해야 합니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
                    , @ApiResponse(responseCode = "404", description = "해당 회원을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @GetMapping
    public ResponseEntity<SliceResponseDto> searchEvaluation(@AuthenticationPrincipal UserDetails loginUser, @RequestParam String search, Pageable pageable) {
        if (StringUtils.isEmpty(search)) {
            throw new BusinessException(ErrorCode.WRONG_SEARCH);
        }
        Slice<EvaluationSearchResponseDto> response = evaluationService.searchEvaluation(loginUser.getUsername(), search, pageable);
        return SliceResponseDto.ok(response);
    }
}
