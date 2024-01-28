package com.github.crazyrunsnail.template.dto.user;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginParam {


    @NotBlank(message = "用户名不能为空")
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    @NotBlank(message = "密码不能为空")
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

}
