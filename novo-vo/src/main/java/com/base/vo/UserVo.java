package com.base.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * UserVo.
 *
 * @author vsangucho on 10/03/2023
 * @version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserVo {

    private String id;
    private String userId;
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
}
