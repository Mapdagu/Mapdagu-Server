package com.project.mapdagu.domain.auth.controller;

import com.project.mapdagu.common.dto.ResponseDto;
import com.project.mapdagu.domain.auth.dto.request.EmailRequestDto;
import com.project.mapdagu.domain.auth.dto.request.SignUpRequestDto;
import com.project.mapdagu.domain.auth.dto.request.SocialSignUpRequestDto;
import com.project.mapdagu.domain.auth.dto.response.EmailResponseDto;
import com.project.mapdagu.domain.auth.dto.response.SocialSignUpResponseDto;
import com.project.mapdagu.domain.auth.service.AuthService;
import com.project.mapdagu.domain.auth.service.EmailService;
import com.project.mapdagu.error.dto.ErrorResponse;
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

@Tag(name = "Sign-Up", description = "Sign-Up API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/sign-up")
public class SignUpController {

    private final AuthService authService;
    private final EmailService emailService;

    @Operation(summary = "자체 회원가입", description = "이메일을 사용해 회원가입을 합니다.",
            responses = {@ApiResponse(responseCode = "204", description = "자체 회원가입 성공")
                    , @ApiResponse(responseCode = "400", description = "1. 이미 존재하는 이메일입니다. \t\n 2. 이미 존재하는 사용자 이름입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<Void> signUp(@RequestBody SignUpRequestDto signUpRequestDto) {
        authService.signUp(signUpRequestDto);
        return ResponseDto.noContent();
    }

    @Operation(summary = "소셜 추가 회원가입", description = "소셜 로그인 유저 대상으로 추가 회원가입을 합니다.",
            security = { @SecurityRequirement(name = "bearer-key") },
            responses = {
                    @ApiResponse(responseCode = "201", description = "소셜 추가 회원가입 성공", content = @Content(schema = @Schema(implementation = SocialSignUpResponseDto.class)))
                    , @ApiResponse(responseCode = "400", description = "이미 존재하는 사용자 이름입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
                    , @ApiResponse(responseCode = "401", description = "잘못된 토큰입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
                    , @ApiResponse(responseCode = "404", description = "해당 회원을 찾을 수 없습니다", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @PatchMapping("/social")
    public ResponseEntity<SocialSignUpResponseDto> socialSignUp(@RequestBody SocialSignUpRequestDto signUpRequestDto, HttpServletRequest request, HttpServletResponse response) {
        SocialSignUpResponseDto socialSignUpResponseDto = authService.socialSignUp(signUpRequestDto, request, response);
        return ResponseDto.created(socialSignUpResponseDto);
    }

    @Operation(summary = "이메일 인증번호 전송", description = "입력한 이메일로 인증번호를 전송합니다.",
            responses = {
                @ApiResponse(responseCode = "200", description = "이메일 인증번호 전송 성공", content = @Content(schema = @Schema(implementation = EmailResponseDto.class)))
                , @ApiResponse(responseCode = "400", description = "1. 이미 존재하는 이메일입니다. \t\n 2. 이메일 인증 코드 전송을 실패했습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/email")
    public ResponseEntity<EmailResponseDto> mailConfirm(@RequestBody EmailRequestDto requestDto){
        EmailResponseDto emailResponseDto = emailService.sendEmail(requestDto);
        return ResponseDto.ok(emailResponseDto);
    }

}
