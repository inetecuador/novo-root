package com.base.controller;

import com.base.vo.common.BaseResponseVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * PersonController.
 *
 * @author vsangucho on 16/03/2023
 * @version 1.0
 */
@Tag(name = "Test", description = "Test API")
@RestController
@RequestMapping("/public")
@Lazy
public class TestController {

    /**
     * Get test.
     *
     * @return value
     */
    @GetMapping(path = "/test")
    @Operation(summary = "Get list of contracts")
    public ResponseEntity<BaseResponseVo> test() {
        return ResponseEntity.ok(BaseResponseVo.builder().data("OK test").build());
    }
}
