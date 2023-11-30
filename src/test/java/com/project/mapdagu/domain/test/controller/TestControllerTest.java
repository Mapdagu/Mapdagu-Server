package com.project.mapdagu.domain.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.mapdagu.domain.test.dto.request.TestInfoRequestDto;
import com.project.mapdagu.domain.test.dto.request.TestRequestDto;
import com.project.mapdagu.domain.test.service.TestService;
import com.project.mapdagu.utils.TestUserArgumentResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TestControllerTest {

    @InjectMocks
    private TestController testController;
    @Mock
    private TestService testService;
    private ObjectMapper objectMapper = new ObjectMapper();
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(testController)
                .setCustomArgumentResolvers(new TestUserArgumentResolver())
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }
    @Test
    void 테스트_결과_저장() throws Exception {
        //given
        TestInfoRequestDto requestDto = new TestInfoRequestDto(1234,3);

        //when
        ResultActions result = mockMvc.perform(
                patch("/test/info")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
        );

        //then
        result.andExpect(status().isNoContent());
        verify(testService, times(1)).saveTestInfo(anyString(), any());

    }

    @Test
    void 테스트_평가_저장() throws Exception{
        //given
        List<TestRequestDto> dtoList = new ArrayList<TestRequestDto>();
        dtoList.add(new TestRequestDto("삼양라면", 2, null));
        dtoList.add(new TestRequestDto("신라면", 3, null));
        TestRequestDto requestDto = new TestRequestDto(null, null, dtoList);

        //when
        ResultActions result = mockMvc.perform(
                post("/test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
        );

        //then
        result.andExpect(status().isNoContent());
        verify(testService, times(1)).saveTest(anyString(), any());
    }

    @Test
    void 다시_진행한_테스트_평가_수정() throws Exception{
        //given
        List<TestRequestDto> dtoList = new ArrayList<TestRequestDto>();
        dtoList.add(new TestRequestDto("삼양라면", 2, null));
        dtoList.add(new TestRequestDto("신라면", 3, null));
        TestRequestDto requestDto = new TestRequestDto(null, null, dtoList);

        //when
        ResultActions result = mockMvc.perform(
                patch("/test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
        );

        //then
        result.andExpect(status().isNoContent());
        verify(testService, times(1)).updateReTest(anyString(), any());
    }
}