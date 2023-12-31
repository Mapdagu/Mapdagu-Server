package com.project.mapdagu.domain.auth.controller;

import com.project.mapdagu.common.dto.ResponseDto;
import com.project.mapdagu.domain.auth.dto.request.RefreshTokenRequest;
import com.project.mapdagu.domain.auth.service.AuthService;
import com.project.mapdagu.error.dto.ErrorResponse;
import com.project.mapdagu.jwt.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth", description = "Auth API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    @Operation(summary = "JWT 테스트", description = "Token으로 JWT 테스트를 합니다.", security = { @SecurityRequirement(name = "bearer-key") })
    @GetMapping("/jwt-test")
    public String jwtTest() {
        return "jwtTest 요청 성공";
    }

    @Operation(summary = "로그아웃", description = "로그아웃 후 사용자의 토큰을 블랙리스트에 등록합니다.", security = { @SecurityRequirement(name = "bearer-key") },
            responses = {
                    @ApiResponse(responseCode = "204", description = "로그아웃 성공, 반환 값 x")
                    , @ApiResponse(responseCode = "401", description = "잘못된 토큰입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, @AuthenticationPrincipal UserDetails loginUser) {
        authService.logout(request, loginUser.getUsername());
        return ResponseDto.noContent();
    }

    @Operation(summary = "토큰 재발급", description = "Refresh Token으로 Access Token과 Refresh Token을 재발급합니다.", security = { @SecurityRequirement(name = "bearer-key") },
            responses = {
                    @ApiResponse(responseCode = "204", description = "토큰 재발급 성공")
                    , @ApiResponse(responseCode = "400", description = "Refresh Token은 필수 입력 값입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @PostMapping("/reIssue")
    public ResponseEntity<Void> reIssue(HttpServletResponse response, @Valid @RequestBody RefreshTokenRequest request) {
        jwtService.reIssueToken(response, request.refreshToken());
        return ResponseDto.noContent();
    }
}
