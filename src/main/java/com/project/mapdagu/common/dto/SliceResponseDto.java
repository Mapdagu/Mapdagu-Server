package com.project.mapdagu.common.dto;

import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public record SliceResponseDto<T>(
        List<T> content,
        int numberOfElements,
        boolean hasNext

) {
    public static <T> ResponseEntity<SliceResponseDto> ok(Slice<T> slice) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SliceResponseDto<>(
                        slice.getContent(),
                        slice.getNumberOfElements(),
                        slice.hasNext()
                ));
    }

}
