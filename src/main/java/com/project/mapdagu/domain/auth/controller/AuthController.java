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
public class AuthController {

    private final AuthService authService;
    private final EmailService emailService;

    @Operation(summary = "자체 회원가입", description = "이메일을 사용해 회원가입을 합니다.")
    @PostMapping("/sign-up")
    public ResponseEntity<SignUpResponseDto> signUp(@RequestBody SignUpRequestDto signUpRequestDto) {
        SignUpResponseDto signUpResponseDto = authService.signUp(signUpRequestDto);
        return ResponseDto.created(signUpResponseDto);
    }

    @Operation(summary = "JWT 테스트", description = "Token으로 JWT 테스트를 합니다.", security = { @SecurityRequirement(name = "bearer-key") })
    @GetMapping("/jwt-test")
    public String jwtTest() {
        return "jwtTest 요청 성공";
    }

    @Operation(summary = "소셜 추가 회원가입", description = "소셜 로그인 유저 대상으로 추가 회원가입을 합니다.",
            security = { @SecurityRequirement(name = "bearer-key") },
            responses = {
                    @ApiResponse(responseCode = "201", description = "소셜 추가 회원가입 성공", content = @Content(schema = @Schema(implementation = SocialSignUpResponseDto.class)))
            })
    @PatchMapping("/sign-up/social")
    public ResponseEntity<SocialSignUpResponseDto> socialSignUp(@RequestBody SocialSignUpRequestDto signUpRequestDto, HttpServletRequest request, HttpServletResponse response) {
        SocialSignUpResponseDto socialSignUpResponseDto = authService.socialSignUp(signUpRequestDto, request, response);
        return ResponseDto.created(socialSignUpResponseDto);
    }

    @Operation(summary = "이메일 인증번호 전송", description = "입력한 이메일로 인증번호를 전송합니다.")
    @PostMapping("/sign-up/email")
    public ResponseEntity<EmailResponseDto> mailConfirm(@RequestBody EmailRequestDto requestDto){
        EmailResponseDto emailResponseDto = emailService.sendEmail(requestDto);
        return ResponseDto.ok(emailResponseDto);
    }

    @Operation(summary = "로그아웃", description = "로그아웃 후 사용자의 토큰을 블랙리스트에 등록합니다.", security = { @SecurityRequirement(name = "bearer-key") },
            responses = {
                    @ApiResponse(responseCode = "204", description = "로그아웃 성공, 반환 값 x")
            })
    @PostMapping("/auth/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        authService.logout(request);
        return ResponseDto.noContent();
    }
}
