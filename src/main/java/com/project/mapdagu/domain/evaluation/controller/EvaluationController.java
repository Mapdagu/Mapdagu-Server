package com.project.mapdagu.domain.evaluation.controller;

import com.project.mapdagu.common.dto.ResponseDto;
import com.project.mapdagu.domain.evaluation.dto.request.EvaluationInfoRequestDto;
import com.project.mapdagu.domain.evaluation.dto.request.EvaluationSaveRequestDto;
import com.project.mapdagu.domain.evaluation.service.EvaluationService;
import com.project.mapdagu.error.dto.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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
}
