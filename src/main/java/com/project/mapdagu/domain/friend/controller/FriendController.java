package com.project.mapdagu.domain.friend.controller;

import com.project.mapdagu.common.dto.ResponseDto;
import com.project.mapdagu.common.dto.SliceResponseDto;
import com.project.mapdagu.domain.friend.dto.response.FriendSearchResponseDto;
import com.project.mapdagu.domain.friend.service.FriendService;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

@Tag(name = "Friend", description = "Friend API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/friends")
public class FriendController {

    private final FriendService friendService;

    @Operation(summary = "회원 닉네임 검색", description = "회원 닉네임을 검색합니다.",
            security = { @SecurityRequirement(name = "bearer-key") },
            responses = {
                    @ApiResponse(responseCode = "200", description = "회원 닉네임 검색 성공")
                    , @ApiResponse(responseCode = "401", description = "인증에 실패했습니다.")
                    , @ApiResponse(responseCode = "400", description = "검색어를 입력해야 합니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
                    , @ApiResponse(responseCode = "404", description = "해당 회원을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @GetMapping
    public ResponseEntity<SliceResponseDto> searchMember(@AuthenticationPrincipal UserDetails loginUser, @RequestParam String search, Pageable pageable) {
        if (StringUtils.isEmpty(search)) {
            throw new BusinessException(ErrorCode.WRONG_SEARCH);
        }
        Slice<FriendSearchResponseDto> response = friendService.searchMember(loginUser.getUsername(), search, pageable);
        return SliceResponseDto.ok(response);
    }

    @Operation(summary = "친구 추가", description = "친구를 추가합니다.",
            security = { @SecurityRequirement(name = "bearer-key") },
            responses = {
                    @ApiResponse(responseCode = "204", description = "친구 추가 성공")
                    , @ApiResponse(responseCode = "401", description = "인증에 실패했습니다.")
                    , @ApiResponse(responseCode = "404", description = "해당 회원을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @PostMapping("/{memberId}")
    public ResponseEntity<Void> saveFriend(@AuthenticationPrincipal UserDetails loginUser, @PathVariable Long memberId) {
        friendService.saveFriend(loginUser.getUsername(), memberId);
        return ResponseDto.noContent();
    }

    @Operation(summary = "친구 삭제", description = "친구를 삭제합니다.",
            security = { @SecurityRequirement(name = "bearer-key") },
            responses = {
                    @ApiResponse(responseCode = "204", description = "친구 삭제 성공")
                    , @ApiResponse(responseCode = "401", description = "인증에 실패했습니다.")
                    , @ApiResponse(responseCode = "404", description = "1. 해당 회원을 찾을 수 없습니다. \t\n 2. 해당 회원과 친구가 아닙니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> deleteFriend(@AuthenticationPrincipal UserDetails loginUser, @PathVariable Long memberId) {
        friendService.deleteFriend(loginUser.getUsername(), memberId);
        return ResponseDto.noContent();
    }
}
