package com.project.mapdagu.domain.auth.controller;

import com.project.mapdagu.common.dto.ResponseDto;
import com.project.mapdagu.domain.auth.dto.request.EmailRequestDto;
import com.project.mapdagu.domain.auth.dto.request.SocialSignUpRequestDto;
import com.project.mapdagu.domain.auth.dto.response.EmailResponseDto;
import com.project.mapdagu.domain.auth.dto.response.SocialSignUpResponseDto;
import com.project.mapdagu.domain.auth.service.AuthService;
import com.project.mapdagu.domain.auth.dto.request.SignUpRequestDto;
import com.project.mapdagu.domain.auth.dto.response.SignUpResponseDto;
import com.project.mapdagu.domain.auth.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth", description = "Auth API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final EmailService emailService;

    @Operation(summary = "JWT 테스트", description = "Token으로 JWT 테스트를 합니다.", security = { @SecurityRequirement(name = "bearer-key") })
    @GetMapping("/jwt-test")
    public String jwtTest() {
        return "jwtTest 요청 성공";
    }

    @Operation(summary = "로그아웃", description = "로그아웃 후 사용자의 토큰을 블랙리스트에 등록합니다.", security = { @SecurityRequirement(name = "bearer-key") },
            responses = {
                    @ApiResponse(responseCode = "204", description = "로그아웃 성공, 반환 값 x")
            })
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        authService.logout(request);
        return ResponseDto.noContent();
    }
}
