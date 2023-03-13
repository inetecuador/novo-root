package com.base.controller;

import javax.validation.Valid;
import com.base.service.IPersonService;
import com.base.vo.PersonVo;
import com.base.vo.common.BaseResponseVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * PersonController.
 *
 * @author vsangucho on 10/03/2023
 * @version 1.0
 */
@Tag(name = "Person", description = "The Persons API")
@RestController
@RequestMapping("/api/v1/persons")
@Lazy
public class PersonController {

    @Lazy
    @Autowired
    private IPersonService service;

    /**
     * Save Person.
     *
     * @param request person
     * @return PersonVo saved in db
     */
    @PostMapping
    @Operation(summary = "Save Person")
    public ResponseEntity<BaseResponseVo> save(@Valid @RequestBody PersonVo request) {
        if (this.service.exist(request.getDocumentNumber())) {
            return ResponseEntity.ok(BaseResponseVo.builder().code(1).build());
        }
        this.service.save(request);
        return ResponseEntity.ok(BaseResponseVo.builder().data(request).build());
    }

    /**
     * Get all persons.
     *
     * @return list personVo
     */
    @GetMapping(path = "")
    @Operation(summary = "Get list of persons")
    public ResponseEntity<BaseResponseVo> getAll() {
        return ResponseEntity.ok(BaseResponseVo.builder().data(this.service.list()).build());
    }
}
