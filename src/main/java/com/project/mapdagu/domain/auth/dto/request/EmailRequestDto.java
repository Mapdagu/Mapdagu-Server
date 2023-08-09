package com.project.mapdagu.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailRequestDto(@Email(message="이메일 형식에 맞지 않습니다.")
                              @NotBlank(message = "이메일은 필수 입력 값입니다.")String email) {
}
