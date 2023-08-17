package com.project.mapdagu.domain.test.controller;

import com.project.mapdagu.common.dto.ResponseDto;
import com.project.mapdagu.domain.test.dto.request.TestInfoRequestDto;
import com.project.mapdagu.domain.test.dto.request.TestRequestDto;
import com.project.mapdagu.domain.test.service.TestService;
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

@Tag(name = "Test", description = "Test API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
public class TestController {

    private final TestService testService;

    @Operation(summary = "테스트 결과 정보 저장", description = "테스트 결과 정보(스코빌지수, 레벨)를 저장합니다.",
            security = { @SecurityRequirement(name = "bearer-key") },
            responses = {
                    @ApiResponse(responseCode = "204", description = "테스트 결과 정보 저장 성공")
                    , @ApiResponse(responseCode = "401", description = "인증에 실패했습니다.")
                    , @ApiResponse(responseCode = "404", description = "해당 회원을 찾을 수 없습니다", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @PatchMapping("/info")
    public ResponseEntity<Void> saveTestInfo(@AuthenticationPrincipal UserDetails loginUser, @RequestBody TestInfoRequestDto testInfoRequestDto) {
        testService.saveTestInfo(loginUser.getUsername(), testInfoRequestDto);
        return ResponseDto.noContent();
    }

    @Operation(summary = "테스트 평가 저장", description = "테스트 평가를 저장합니다.",
            security = { @SecurityRequirement(name = "bearer-key") },
            responses = {
                    @ApiResponse(responseCode = "204", description = "테스트 평가 저장 성공")
                    , @ApiResponse(responseCode = "401", description = "인증에 실패했습니다.")
                    , @ApiResponse(responseCode = "404", description = "해당 음식을 찾을 수 없습니다", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @PostMapping
    public ResponseEntity<Void> saveTest(@AuthenticationPrincipal UserDetails loginUser, @RequestBody TestRequestDto testRequestDto) {
        testService.saveTest(loginUser.getUsername(), testRequestDto);
        return ResponseDto.noContent();
    }

    @Operation(summary = "다시 진행한 테스트 평가 수정", description = "다시 진행한 테스트 평가를 수정합니다.",
            security = { @SecurityRequirement(name = "bearer-key") },
            responses = {
                    @ApiResponse(responseCode = "204", description = "테스트 평가 수정 성공")
                    , @ApiResponse(responseCode = "401", description = "인증에 실패했습니다.")
                    , @ApiResponse(responseCode = "404", description = "해당 평가를 찾을 수 없습니다", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @PatchMapping
    public ResponseEntity<Void> updateReTest(@AuthenticationPrincipal UserDetails loginUser, @RequestBody TestRequestDto testRequestDto) {
        testService.updateReTest(loginUser.getUsername(), testRequestDto);
        return ResponseDto.noContent();
    }
}
