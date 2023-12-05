package com.project.mapdagu.domain.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.mapdagu.domain.auth.dto.request.RefreshTokenRequest;
import com.project.mapdagu.domain.auth.service.AuthService;
import com.project.mapdagu.jwt.service.JwtService;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @InjectMocks
    private AuthController authController;
    @Mock
    private AuthService authService;
    @Mock
    private JwtService jwtService;
    private ObjectMapper objectMapper = new ObjectMapper();
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setCustomArgumentResolvers(new TestUserArgumentResolver())
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    @Test
    void 로그아웃() throws Exception {
        //given
        //when
        ResultActions result = mockMvc.perform(
                post("/auth/logout")
        );
        //then
        result.andExpect(status().isNoContent());
        verify(authService, times(1)).logout(any(), anyString());
    }

    @Test
    void 토큰_재발급() throws Exception {
        //given
        RefreshTokenRequest request = new RefreshTokenRequest("refreshToken");
        //when
        ResultActions result = mockMvc.perform(
                post("/auth/reIssue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );
        //then
        result.andExpect(status().isNoContent());
        verify(jwtService, times(1)).reIssueToken(any(), anyString());
    }
}