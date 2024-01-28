package com.github.crazyrunsnail.template.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {

    @Schema(description = "接口码：0-成功，1-失败，10002-未登录或者token过期", example = "0")
    private Integer code;

    @Schema(description = "接口消息")
    private String message;

    private T data;


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
