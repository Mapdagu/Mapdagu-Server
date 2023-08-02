package com.project.mapdagu.error.exception.custom;

import com.project.mapdagu.error.ErrorCode;
import lombok.Getter;

@Getter
public class TokenException extends RuntimeException{

    private final int code;

    public TokenException(final ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }
}
