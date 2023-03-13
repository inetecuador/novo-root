package com.base.vo.common;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * BaseAuditableVO.
 *
 * @author vsangucho on 10/03/2023
 * @version 1.0
 * @since 1.0.0
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BaseAuditableVo {

    private Integer companyCode;
    private Boolean status;
    private String createdByUser;
    private Date createdDate;
    private String lastModifiedByUser;
    private Date lastModifiedDate;
    private String userId;

}
