package com.project.mapdagu.domain.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.mapdagu.domain.auth.dto.request.SignUpRequestDto;
import com.project.mapdagu.domain.auth.dto.request.SocialSignUpRequestDto;
import com.project.mapdagu.domain.auth.service.AuthService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class SignUpControllerTest {

    @InjectMocks
    private SignUpController signUpController;
    @Mock
    private AuthService authService;
    private ObjectMapper objectMapper = new ObjectMapper();
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(signUpController)
                .setCustomArgumentResolvers(new TestUserArgumentResolver())
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    @Test
    void 자체_회원가입_성공() throws Exception{
        //given
        SignUpRequestDto requestDto = new SignUpRequestDto("nickname",
                "email@naver.com",
                "password",
                "userName",
                1,
                "intro");
        //when
        ResultActions result = mockMvc.perform(
                post("/api/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
        );
        //then
        result.andExpect(status().isNoContent());
        verify(authService, times(1)).signUp(any());

    }

    @Test
    void 소셜_추가_회원가입_성공() throws Exception{
        //given
        SocialSignUpRequestDto requestDto = new SocialSignUpRequestDto(
                "userName",
                1,
                "intro"
        );
        //when
        ResultActions result = mockMvc.perform(
                patch("/api/sign-up/social")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
        );
        //then
        result.andExpect(status().isCreated());
        verify(authService, times(1)).socialSignUp(any(), any(), any());
    }

}