package com.github.crazyrunsnail.template.dto.page;

import com.github.pagehelper.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T>  {

    @Schema(title = "总条数")
    private Long total;

    @Schema(title = "总页数")
    private Integer pages;

    @Schema(title = "当前指定页数")
    private Integer current;

    @Schema(title = "列表")
    private List<T> list;

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <E> PageResult<E> of(Page<E> page) {
        return PageResult.builder()
                .total(page.getTotal())
                .pages(page.getPages())
                .current(page.getPageNum())
                .list((List)page.getResult()).build();
    }
}
