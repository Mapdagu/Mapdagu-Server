package com.project.mapdagu.domain.friendRequest.controller;

import com.project.mapdagu.common.dto.ResponseDto;
import com.project.mapdagu.domain.friendRequest.service.FriendRequestService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Friend Request", description = "Friend Request API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/friends/request")
public class FriendRequestController {

    private final FriendRequestService friendRequestService;

    @Operation(summary = "친구 요청", description = "친구를 요청합니다.",
            security = { @SecurityRequirement(name = "bearer-key") },
            responses = {
                    @ApiResponse(responseCode = "200", description = "친구 요청 성공")
                    , @ApiResponse(responseCode = "401", description = "인증에 실패했습니다.")
                    , @ApiResponse(responseCode = "404", description = "해당 회원을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            })
    @PostMapping("/{friendId}")
    public ResponseEntity<Void> saveFriendRequest(@AuthenticationPrincipal UserDetails loginUser, @PathVariable Long friendId) {
        friendRequestService.saveFriendRequest(loginUser.getUsername(), friendId);
        return ResponseDto.noContent();
    }
}
