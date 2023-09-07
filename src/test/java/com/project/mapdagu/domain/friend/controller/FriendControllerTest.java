package com.project.mapdagu.domain.friend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.mapdagu.domain.friend.dto.response.FriendSearchResponseDto;
import com.project.mapdagu.domain.friend.service.FriendService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class FriendControllerTest {

    @InjectMocks
    private FriendController friendController;
    @Mock
    private FriendService friendService;
    private ObjectMapper objectMapper = new ObjectMapper();
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(friendController)
                .setCustomArgumentResolvers(new TestUserArgumentResolver(), new PageableHandlerMethodArgumentResolver())
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    @Test
    void 회원_이름_검색() throws Exception{
        //given
        PageRequest pageable = PageRequest.of(0, 2);
        List<FriendSearchResponseDto> dtos = new ArrayList<>();
        dtos.add(new FriendSearchResponseDto(1L, 2, "린서", 3));
        dtos.add(new FriendSearchResponseDto(2L, 3, "경혁", 2));
        Slice<FriendSearchResponseDto> response = new SliceImpl<>(dtos, pageable, false);

        //when
        given(friendService.searchMember(anyString(), anyString(), any())).willReturn(response);
        ResultActions result = mockMvc.perform(
                get("/api/friends")
                        .param("search", "test")
        );

        //then
        result.andExpect(status().isOk());
        verify(friendService, times(1)).searchMember(anyString(), anyString(), any());
    }
}