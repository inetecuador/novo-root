package com.base.vo;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PersonVo.
 *
 * @author vsangucho on 10/03/2023
 * @version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonVo {

    private String personId;
    @NotBlank
    private String documentNumber;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    private String email;
}
