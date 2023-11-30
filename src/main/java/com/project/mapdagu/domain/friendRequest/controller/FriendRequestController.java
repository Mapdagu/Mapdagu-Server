package com.project.mapdagu.domain.friendRequest.controller;

import com.project.mapdagu.common.dto.ResponseDto;
import com.project.mapdagu.common.dto.SliceResponseDto;
import com.project.mapdagu.domain.friendRequest.dto.response.FriendRequestsGetResponseDto;
import com.project.mapdagu.domain.friendRequest.service.FriendRequestService;
import com.project.mapdagu.error.dto.ErrorResponse;
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

@Tag(name = "Friend Request", description = "Friend Request API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/friends/request")
public class FriendRequestController {

    private final FriendRequestService friendRequestService;

    @Operation(summary = "친구 요청", description = "친구를 요청합니다.",
            security = { @SecurityRequirement(name = "bearer-key") },
            responses = {
                    @ApiResponse(responseCode = "204", description = "친구 요청 성공")
                    , @ApiResponse(responseCode = "401", description = "인증에 실패했습니다.")
                    , @ApiResponse(responseCode = "400", description = "이미 보낸 친구 요청입니다.")
                    , @ApiResponse(responseCode = "404", description = "해당 회원을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @PostMapping("/{memberId}")
    public ResponseEntity<Void> saveFriendRequest(@AuthenticationPrincipal UserDetails loginUser, @PathVariable Long memberId) {
        friendRequestService.saveFriendRequest(loginUser.getUsername(), memberId);
        return ResponseDto.noContent();
    }

    @Operation(summary = "친구 요청 삭제", description = "친구 요청을 삭제합니다.",
            security = { @SecurityRequirement(name = "bearer-key") },
            responses = {
                    @ApiResponse(responseCode = "204", description = "친구 요청 삭제 성공")
                    , @ApiResponse(responseCode = "401", description = "인증에 실패했습니다.")
                    , @ApiResponse(responseCode = "404", description = "1. 해당 회원을 찾을 수 없습니다. \t\n 2. 해당 친구 요청을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> deleteFriendRequest(@AuthenticationPrincipal UserDetails loginUser, @PathVariable Long memberId) {
        friendRequestService.deleteFriendRequest(loginUser.getUsername(), memberId);
        return ResponseDto.noContent();
    }

    @Operation(summary = "친구 요청 목록 조회", description = "친구 요청 목록을 조회합니다.",
            security = { @SecurityRequirement(name = "bearer-key") },
            responses = {
                    @ApiResponse(responseCode = "200", description = "친구 요청 목록 조회 성공")
                    , @ApiResponse(responseCode = "401", description = "인증에 실패했습니다.")
                    , @ApiResponse(responseCode = "404", description = "해당 회원을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @GetMapping
    public ResponseEntity<SliceResponseDto> getAllFriendRequest(@AuthenticationPrincipal UserDetails loginUser, Pageable pageable) {
        Slice<FriendRequestsGetResponseDto> response = friendRequestService.getAllFriendRequest(loginUser.getUsername(), pageable);
        return SliceResponseDto.ok(response);
    }
}
