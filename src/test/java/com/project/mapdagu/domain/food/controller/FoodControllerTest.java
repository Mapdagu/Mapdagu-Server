package com.project.mapdagu.domain.food.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.mapdagu.domain.evaluation.dto.response.EvaluationGetResponseDto;
import com.project.mapdagu.domain.food.dto.response.FoodGetResponseDto;
import com.project.mapdagu.domain.food.dto.response.FoodScovilleSearchResponseDto;
import com.project.mapdagu.domain.food.dto.response.FoodSearchResponseDto;
import com.project.mapdagu.domain.food.service.FoodService;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class FoodControllerTest {

    @InjectMocks
    private FoodController foodController;
    @Mock
    private FoodService foodService;
    private ObjectMapper objectMapper = new ObjectMapper();
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(foodController)
                .setCustomArgumentResolvers(new TestUserArgumentResolver(), new PageableHandlerMethodArgumentResolver())
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    @Test
    void 음식_검색() throws Exception {
        //given
        PageRequest pageable = PageRequest.of(0, 2);
        List<FoodSearchResponseDto> dtos = new ArrayList<>();
        dtos.add(new FoodSearchResponseDto("신라면", 1, 3400));
        dtos.add(new FoodSearchResponseDto("진라면", 2, 1270));
        Slice<FoodSearchResponseDto> response = new SliceImpl<>(dtos, pageable, false);

        //when
        given(foodService.searchFood(anyString(), any())).willReturn(response);
        ResultActions result = mockMvc.perform(
                get("/api/foods")
                        .param("search", "라면")
        );

        //then
        result.andExpect(status().isOk());
        verify(foodService, times(1)).searchFood(anyString(), any());
    }

    @Test
    void 음식_스코빌_지수_검색() throws Exception {
        //given
        FoodScovilleSearchResponseDto responseDto = new FoodScovilleSearchResponseDto("신라면", 1, 3400);

        //when
        given(foodService.searchFoodScoville(anyString())).willReturn(responseDto);
        ResultActions result = mockMvc.perform(
                get("/api/foods/scoville")
                        .param("search", "신라면")
        );

        //then
        result.andExpect(status().isOk());
        verify(foodService, times(1)).searchFoodScoville(anyString());
    }

    @Test
    void 음식_상세_정보_조회() throws Exception {
        //given
        Long foodId = 1L;
        FoodGetResponseDto responseDto = new FoodGetResponseDto(foodId, "신라면", 1, true, 5);

        //when
        when(foodService.getOneFood(anyString(), anyLong())).thenReturn(responseDto);
        ResultActions result = mockMvc.perform(
                get("/api/foods/{foodId}", foodId)
        );

        //then
        result.andExpect(status().isOk());
        verify(foodService, times(1)).getOneFood(anyString(), anyLong());
    }
}