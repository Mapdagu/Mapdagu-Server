package com.project.mapdagu.domain.auth.controller;

import com.project.mapdagu.common.dto.ResponseDto;
import com.project.mapdagu.domain.auth.service.AuthService;
import com.project.mapdagu.domain.auth.dto.request.SignUpRequestDto;
import com.project.mapdagu.domain.auth.dto.response.SignUpResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "Auth API")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "자체 회원가입", description = "이메일을 사용해 회원가입 합니다.")
    @PostMapping("/sign-up")
    public ResponseEntity<SignUpResponseDto> signUp(@RequestBody SignUpRequestDto signUpRequestDto) {
        SignUpResponseDto signUpResponseDto = authService.signUp(signUpRequestDto);
        return ResponseDto.created(signUpResponseDto);
    }

    @Operation(summary = "JWT 테스트", description = "Token으로 JWT 테스트를 합니다.")
    @GetMapping("/jwt-test")
    public String jwtTest() {
        return "jwtTest 요청 성공";
    }
}
