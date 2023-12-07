package com.project.mapdagu.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.mapdagu.error.dto.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.project.mapdagu.error.ErrorCode.NOT_AUTHENTICATED_REQUEST;

/**
 * 유효한 자격 증명을 제공하지 않고 접근하려 할 때, 401 UnAuthorized 에러를 리턴
 */
@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public JwtAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        log.info("인증되지 않은 요청입니다.");
        ErrorResponse errorResponse = ErrorResponse.of(NOT_AUTHENTICATED_REQUEST.getCode(), NOT_AUTHENTICATED_REQUEST.getMessage());
        String jsonResponse = objectMapper.writeValueAsString(errorResponse);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(jsonResponse);
    }
}