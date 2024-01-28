package com.github.crazyrunsnail.template.controller.global;

import com.github.crazyrunsnail.template.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.resource.NoResourceFoundException;


@Slf4j
@RestController
@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(Exception.class)
    public ApiResponse<?> handleException(Exception ex) {
        // https://github.com/spring-projects/spring-security/issues/6908
        if (ex instanceof AccessDeniedException ade) {
            throw ade;
        }
        log.error("服务器异常--> {}", ex.getMessage(), ex);
        return ApiResponse.fail(500, "服务器异常");
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ApiResponse<?> handleException(NoResourceFoundException ex) {
        return ApiResponse.fail(404, "访问的资源不存在");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<?> handleValidateException(MethodArgumentNotValidException ex) {
        FieldError error = (FieldError) ex.getBindingResult().getAllErrors().iterator().next();
        return ApiResponse.fail(422, "参数校验失败：[" + error.getField() + "]" +
                error.getDefaultMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResponse<?> handleIllegalArgumentException(IllegalArgumentException e) {
        return ApiResponse.fail(422, e.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ApiResponse<?> handleIllegalStateException(IllegalStateException e) {
        return ApiResponse.fail(422, e.getMessage());
    }
}
