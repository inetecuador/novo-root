package com.base.vo.common;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * BaseResponseVo.
 *
 * @author vsangucho on 10/03/2023
 * @version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponseVo<T> {

    @Builder.Default
    private Integer code = 200;
    private String message;
    private List<String> errors;
    private T data;
}
