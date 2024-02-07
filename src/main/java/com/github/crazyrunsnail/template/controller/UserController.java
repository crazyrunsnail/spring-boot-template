package com.github.crazyrunsnail.template.controller;


import com.github.crazyrunsnail.template.dto.ApiResponse;
import com.github.crazyrunsnail.template.dto.page.PageResult;
import com.github.crazyrunsnail.template.dto.user.LoginParam;
import com.github.crazyrunsnail.template.dto.user.LoginResult;
import com.github.crazyrunsnail.template.dto.user.UserDetailsDTO;
import com.github.crazyrunsnail.template.dto.user.UserSearchParam;
import com.github.crazyrunsnail.template.model.User;
import com.github.crazyrunsnail.template.service.UserService;
import com.github.crazyrunsnail.template.util.JsonUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "01.User", description = "用户相关Api")
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    @Operation(summary = "登录", description = "登录")
    public ApiResponse<LoginResult> login(@RequestBody @Valid LoginParam authLoginForm) {
        return ApiResponse.ok(LoginResult.builder()
                .token(userService.login(authLoginForm.getUsername(), authLoginForm.getPassword()))
                .build());
    }



    @GetMapping("/info")
    @Operation(summary = "用户信息", description = "用户信息")
    public ApiResponse<User> info(@AuthenticationPrincipal UserDetailsDTO userDetails) {
        log.info("Get info: {}", userDetails.getId());
        return ApiResponse.ok(userService.getById(userDetails.getId()));
    }


    @GetMapping("/search")
    @Operation(description = "查询用户", summary = "查询用户")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<PageResult<User>> search(@Valid UserSearchParam param,
                                                @AuthenticationPrincipal UserDetailsDTO userDetails) {
        log.info("Searched by {} with param: {}", userDetails.getId(), JsonUtils.toString(param));
        param.setTo(param.getTo().plusDays(1));
        return ApiResponse.ok(PageResult.of(userService.getBySearchParam(param)));
    }


}
