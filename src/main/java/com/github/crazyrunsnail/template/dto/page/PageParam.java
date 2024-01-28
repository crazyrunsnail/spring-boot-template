package com.github.crazyrunsnail.template.dto.page;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageParam {

    @Schema(title = "每页条数", example = "10")
    private Integer per = 10;

    @Schema(title = "页数", example = "1")
    private Integer page = 1;


}
