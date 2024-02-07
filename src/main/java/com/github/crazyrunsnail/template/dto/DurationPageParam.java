package com.github.crazyrunsnail.template.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DurationPageParam {

    @Schema(title = "每页条数", example = "10")
    private Integer per = 10;

    @Schema(title = "页数", example = "1")
    private Integer page = 1;

    @Schema(title = "查询开始日期，默认当天前30天", example = "2024-01-01")
    private LocalDate from = LocalDate.now().plusDays(-29);

    @Schema(title = "查询结束日期，默认当天", example = "2024-01-01")
    private LocalDate to = LocalDate.now();
}
