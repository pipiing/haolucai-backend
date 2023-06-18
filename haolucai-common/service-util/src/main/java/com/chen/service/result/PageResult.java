package com.chen.service.result;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Tag(name="PageResult",description = "分页结果")
public final class PageResult<T> implements Serializable {

  @Schema(description = "数据", requiredMode = Schema.RequiredMode.REQUIRED)
  private List<T> list;

  @Schema(description = "总量", requiredMode = Schema.RequiredMode.REQUIRED)
  private Long total;

  public PageResult() {}

  public PageResult(List<T> list, Long total) {
    this.list = list;
    this.total = total;
  }

  public PageResult(Long total) {
    this.list = new ArrayList<>();
    this.total = total;
  }

  public static <T> PageResult<T> empty() {
    return new PageResult<>(0L);
  }

  public static <T> PageResult<T> empty(Long total) {
    return new PageResult<>(total);
  }
}
