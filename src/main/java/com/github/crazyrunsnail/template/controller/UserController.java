package com.github.crazyrunsnail.template.controller;


import com.github.crazyrunsnail.template.dto.ApiResponse;
import com.github.crazyrunsnail.template.dto.user.LoginParam;
import com.github.crazyrunsnail.template.dto.user.LoginResult;
import com.github.crazyrunsnail.template.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "1.Auth", description = "签权相关Api")
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    @Operation(summary = "登录", description = "登录")
    public ApiResponse<LoginResult> login(@RequestBody @Valid LoginParam authLoginForm) {
        return ApiResponse.ok(LoginResult.builder()
                .token(userService.login(authLoginForm.getUsername(), authLoginForm.getPassword()))
                .build());
    }
}
