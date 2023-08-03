package com.project.mapdagu.domain.auth.controller;

import com.project.mapdagu.common.dto.ResponseDto;
import com.project.mapdagu.domain.auth.service.AuthService;
import com.project.mapdagu.domain.member.dto.request.SignUpRequestDto;
import com.project.mapdagu.domain.member.dto.response.SignUpResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<SignUpResponseDto> signUp(@RequestBody SignUpRequestDto signUpRequestDto) {
        SignUpResponseDto signUpResponseDto = authService.signUp(signUpRequestDto);
        return ResponseDto.created(signUpResponseDto);
    }

    @GetMapping("/jwt-test")
    public String jwtTest() {
        return "jwtTest 요청 성공";
    }
}
