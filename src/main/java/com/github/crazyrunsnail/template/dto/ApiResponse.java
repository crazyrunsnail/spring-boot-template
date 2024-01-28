package com.github.crazyrunsnail.template.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {

    private T data;


    private Integer code;


    private String message;


    @SuppressWarnings(value = {"unchecked"})
    public static <T> ApiResponse<T> ok(T data) {
        return (ApiResponse<T>) ApiResponse.builder()
                .code(0).message("请求成功")
                .data(data).build();
    }

    public static ApiResponse<?> fail(String message) {
        return ApiResponse.builder().code(1).message(message)
                .build();
    }


    public static ApiResponse<?> fail(Integer code, String message) {
        return ApiResponse.builder().code(code).message(message)
                .build();
    }

}
