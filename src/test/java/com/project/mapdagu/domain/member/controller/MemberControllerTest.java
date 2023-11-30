package com.project.mapdagu.domain.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.mapdagu.common.dto.PageResponseDto;
import com.project.mapdagu.domain.evaluation.dto.request.EvaluationInfoRequestDto;
import com.project.mapdagu.domain.evaluation.dto.request.EvaluationSaveRequestDto;
import com.project.mapdagu.domain.evaluation.dto.request.EvaluationUpdateRequestDto;
import com.project.mapdagu.domain.evaluation.dto.response.EvaluationGetResponseDto;
import com.project.mapdagu.domain.evaluation.dto.response.EvaluationsGetResponseDto;
import com.project.mapdagu.domain.evaluation.service.EvaluationService;
import com.project.mapdagu.domain.member.dto.request.MemberUpdateInfoRequestDto;
import com.project.mapdagu.domain.member.dto.request.MemberUserNameRequestDto;
import com.project.mapdagu.domain.member.dto.response.MemberReadInfoResponseDto;
import com.project.mapdagu.domain.member.dto.response.MemberReadMainResponseDto;
import com.project.mapdagu.domain.member.dto.response.MemberUserNameResponseDto;
import com.project.mapdagu.domain.member.service.MemberService;
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
class MemberControllerTest {

    @InjectMocks
    private MemberController memberController;
    @Mock
    private MemberService memberService;
    private ObjectMapper objectMapper = new ObjectMapper();
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(memberController)
                .setCustomArgumentResolvers(new TestUserArgumentResolver())
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    @Test
    void 회원_정보_수정() throws Exception {
        //given
        MemberUpdateInfoRequestDto requestDto = new MemberUpdateInfoRequestDto(5, "test", "안녕하세요!");

        //when
        ResultActions result = mockMvc.perform(
                patch("/members/me/info")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
        );

        //then
        result.andExpect(status().isNoContent());
        verify(memberService, times(1)).updateMemberInfo(anyString(), any());
    }

    @Test
    void 프로필_정보_조회() throws Exception {
        //given
        MemberReadInfoResponseDto response = new MemberReadInfoResponseDto(5, "test", "안녕하세요!");

        //when
        given(memberService.readMemberInfo(anyString())).willReturn(response);
        ResultActions result = mockMvc.perform(
                get("/members/me/info")
        );

        //then
        result.andExpect(status().isOk());
        verify(memberService, times(1)).readMemberInfo(anyString());
    }

    @Test
    void 메인_사용자_이름_레벨_조회() throws Exception {
        //given
        MemberReadMainResponseDto response = new MemberReadMainResponseDto("test", 8, 2);

        //when
        given(memberService.readMainInfo(anyString())).willReturn(response);
        ResultActions result = mockMvc.perform(
                get("/members/me/main")
        );

        //then
        result.andExpect(status().isOk());
        verify(memberService, times(1)).readMainInfo(anyString());
    }

    @Test
    void 회원_이름_중복_검사() throws Exception {
        //given
        MemberUserNameRequestDto requestDto = new MemberUserNameRequestDto("test");
        MemberUserNameResponseDto response = new MemberUserNameResponseDto(true);

        //when
        given(memberService.checkUserName(requestDto)).willReturn(response);
        ResultActions result = mockMvc.perform(
                post("/members/userName/isDuplicated")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
        );

        //then
        result.andExpect(status().isOk());
        verify(memberService, times(1)).checkUserName(requestDto);
    }
}