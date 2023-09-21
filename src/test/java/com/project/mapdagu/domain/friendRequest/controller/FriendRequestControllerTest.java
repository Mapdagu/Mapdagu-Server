package com.project.mapdagu.domain.friendRequest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.mapdagu.domain.friendRequest.dto.response.FriendRequestsGetResponseDto;
import com.project.mapdagu.domain.friendRequest.service.FriendRequestService;
import com.project.mapdagu.utils.TestUserArgumentResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class FriendRequestControllerTest {

    @InjectMocks
    private FriendRequestController friendRequestController;
    @Mock
    private FriendRequestService friendRequestService;
    private ObjectMapper objectMapper = new ObjectMapper();
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(friendRequestController)
                .setCustomArgumentResolvers(new TestUserArgumentResolver(), new PageableHandlerMethodArgumentResolver())
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    @Test
    void 친구_요청() throws Exception {
        //given
        Long friendId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                post("/api/friends/request/{friendId}", friendId)
        );

        //then
        result.andExpect(status().isNoContent());
        verify(friendRequestService, times(1)).saveFriendRequest(anyString(), anyLong());
    }

    @Test
    void 친구_요청_삭제() throws Exception {
        //given
        Long friendId = 1L;

        // when
        ResultActions result = mockMvc.perform(
                delete("/api/friends/request/{friendId}", friendId)
        );

        //then
        result.andExpect(status().isNoContent());
        verify(friendRequestService, times(1)).deleteFriendRequest(anyString(), anyLong());
    }

    @Test
    void 친구_요청_목록_조회() throws Exception {
        //given
        PageRequest pageable = PageRequest.of(0, 2);
        List<FriendRequestsGetResponseDto> dtos = new ArrayList<>();
        dtos.add(new FriendRequestsGetResponseDto(1L, "테스트1", 3, 1));
        dtos.add(new FriendRequestsGetResponseDto(2L, "테스트2", 5, 2));
        Slice<FriendRequestsGetResponseDto> response = new SliceImpl<>(dtos, pageable, false);

        //when
        given(friendRequestService.getAllFriendRequest(anyString(), any())).willReturn(response);
        ResultActions result = mockMvc.perform(
                get("/api/friends/request")
        );

        //then
        result.andExpect(status().isOk());
        verify(friendRequestService, times(1)).getAllFriendRequest(anyString(), any());
    }
}