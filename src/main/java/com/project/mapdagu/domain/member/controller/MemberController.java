package com.project.mapdagu.domain.member.controller;

import com.project.mapdagu.common.dto.ResponseDto;
import com.project.mapdagu.domain.member.dto.request.MemberUpdateInfoRequestDto;
import com.project.mapdagu.domain.member.dto.response.MemberReadInfoResponseDto;
import com.project.mapdagu.domain.member.dto.response.MemberReadMainResponseDto;
import com.project.mapdagu.domain.member.service.MemberService;
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

@Tag(name = "Member", description = "Member API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "회원 정보 수정", description = "회원 정보를 수정합니다.",
            security = { @SecurityRequirement(name = "bearer-key") },
            responses = {
                    @ApiResponse(responseCode = "204", description = "회원 정보 수정 성공")
                    , @ApiResponse(responseCode = "401", description = "인증에 실패했습니다.")
                    , @ApiResponse(responseCode = "404", description = "1. 해당 회원을 찾을 수 없습니다. /t/n 2. 현재 사용자 이름과 동일합니다. /t/n 3. 이미 존재하는 사용자 이름입니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @PatchMapping("/me/info")
    public ResponseEntity<Void> updateMemberInfo(@AuthenticationPrincipal UserDetails loginUser, @RequestBody MemberUpdateInfoRequestDto requestDto) {
        memberService.updateMemberInfo(loginUser.getUsername(), requestDto);
        return ResponseDto.noContent();
    }

    @Operation(summary = "프로필 설정 정보 조회", description = "프로필 설정 정보를 조회합니다.",
            security = { @SecurityRequirement(name = "bearer-key") },
            responses = {
                    @ApiResponse(responseCode = "200", description = "프로필 설정 정보 조회 성공")
                    , @ApiResponse(responseCode = "401", description = "인증에 실패했습니다.")
                    , @ApiResponse(responseCode = "404", description = "해당 회원을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @GetMapping("/me/info")
    public ResponseEntity<MemberReadInfoResponseDto> readMemberInfo(@AuthenticationPrincipal UserDetails loginUser) {
        MemberReadInfoResponseDto responseDto = memberService.readMemberInfo(loginUser.getUsername());
        return ResponseDto.ok(responseDto);
    }

    @Operation(summary = "메인 - 사용자 이름, 레벨 조회", description = "자신의 사용자 이름, 레벨을 조회합니다.",
            security = { @SecurityRequirement(name = "bearer-key") },
            responses = {
                    @ApiResponse(responseCode = "200", description = " 사용자 이름, 레벨 조회 성공")
                    , @ApiResponse(responseCode = "401", description = "인증에 실패했습니다.")
                    , @ApiResponse(responseCode = "404", description = "해당 회원을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @GetMapping("/me/main")
    public ResponseEntity<MemberReadMainResponseDto> readMainInfo(@AuthenticationPrincipal UserDetails loginUser) {
        MemberReadMainResponseDto responseDto = memberService.readMainInfo(loginUser.getUsername());
        return ResponseDto.ok(responseDto);
    }
}
