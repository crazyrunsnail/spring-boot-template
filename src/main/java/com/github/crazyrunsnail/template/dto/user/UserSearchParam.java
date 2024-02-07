package com.github.crazyrunsnail.template.dto.user;


import com.github.crazyrunsnail.template.dto.DurationPageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UserSearchParam extends DurationPageParam {

    @Schema(title = "名称，支持模糊查询", example = "张三")
    private String name;

}
