package com.project.mapdagu.domain.friendRequest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.mapdagu.domain.friend.controller.FriendController;
import com.project.mapdagu.domain.friend.service.FriendService;
import com.project.mapdagu.domain.friendRequest.service.FriendRequestService;
import com.project.mapdagu.utils.TestUserArgumentResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

}