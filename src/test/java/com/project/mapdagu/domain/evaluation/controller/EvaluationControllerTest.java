package com.project.mapdagu.domain.evaluation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.mapdagu.common.dto.PageResponseDto;
import com.project.mapdagu.domain.evaluation.dto.request.EvaluationInfoRequestDto;
import com.project.mapdagu.domain.evaluation.dto.request.EvaluationSaveRequestDto;
import com.project.mapdagu.domain.evaluation.dto.request.EvaluationUpdateRequestDto;
import com.project.mapdagu.domain.evaluation.dto.response.EvaluationGetResponseDto;
import com.project.mapdagu.domain.evaluation.dto.response.EvaluationsGetResponseDto;
import com.project.mapdagu.domain.evaluation.service.EvaluationService;
import com.project.mapdagu.domain.test.dto.request.TestInfoRequestDto;
import com.project.mapdagu.utils.TestUserArgumentResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class EvaluationControllerTest {

    @InjectMocks
    private EvaluationController evaluationController;
    @Mock
    private EvaluationService evaluationService;
    private ObjectMapper objectMapper = new ObjectMapper();
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(evaluationController)
                .setCustomArgumentResolvers(new TestUserArgumentResolver())
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }
    @Test
    void 맵기_평가_저장() throws Exception {
        //given
        EvaluationSaveRequestDto requestDto = new EvaluationSaveRequestDto("신라면", 3);

        //when
        ResultActions result = mockMvc.perform(
                post("/api/evaluations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
        );

        //then
        result.andExpect(status().isNoContent());
        verify(evaluationService, times(1)).saveEvaluation(anyString(), any());
    }

    @Test
    void 맵기_평가_수정() throws Exception {
        //given
        Long evaluationId = 1L;
        EvaluationUpdateRequestDto requestDto = new EvaluationUpdateRequestDto(3);

        //when
        ResultActions result = mockMvc.perform(
                patch("/api/evaluations/{evaluationId}", evaluationId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
        );

        //then
        result.andExpect(status().isNoContent());
        verify(evaluationService, times(1)).updateEvaluation(anyString(), any(), anyLong());
    }

    @Test
    void 맵기_평가_이후_정보_저장() throws Exception {
        //given
        EvaluationInfoRequestDto requestDto = new EvaluationInfoRequestDto(1234,3);

        //when
        ResultActions result = mockMvc.perform(
                patch("/api/evaluations/info")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
        );

        //then
        result.andExpect(status().isNoContent());
        verify(evaluationService, times(1)).saveEvaluationInfo(anyString(), any());
    }

    @Test
    void 맵기_평가_하나_조회() throws Exception {
        //given
        Long evaluationId = 1L;
        EvaluationGetResponseDto responseDto = new EvaluationGetResponseDto("신라면", 1, 2);

        //when
        when(evaluationService.getOneEvaluation(anyString(), anyLong())).thenReturn(responseDto);
        ResultActions result = mockMvc.perform(
                get("/api/evaluations/{evaluationId}", evaluationId)
        );

        //then
        result.andExpect(status().isOk());
        verify(evaluationService, times(1)).getOneEvaluation(anyString(), anyLong());
    }

//    @Test
//    void 맵기_평가_목록_조회() throws Exception {
//        //given
//        Pageable pageable = PageRequest.of(0, 10);
//        List<EvaluationsGetResponseDto> dtos = new ArrayList<>();
//        dtos.add(new EvaluationsGetResponseDto("신라면", 1, 3));
//        dtos.add(new EvaluationsGetResponseDto("진라면", 2, 2));
//        Page<EvaluationsGetResponseDto> pageResult = new PageImpl<EvaluationsGetResponseDto>(dtos, pageable, 2);
//
//        //when
////        when(evaluationService.getEvaluations(anyString(), any(Pageable.class)).getContent().
//        ResultActions result = mockMvc.perform(
//                get("/api/evaluations")
//                        .param("page", "0")
//                        .param("size", "10")
//        );
//
//        //then
//        result.andExpect(status().isOk());
//        verify(evaluationService, times(1)).getEvaluations(anyString(), any(Pageable.class));
//    }
}