package com.base.common;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Abstract auditable.
 *
 * @author vsangucho on 10/03/2023
 * @version 1.0
 */
@SuperBuilder
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractBaseAuditable<PK extends Serializable> {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * True if the record is active otherwise false.
     */
    @Builder.Default
    @Column(name = "STATUS")
    protected String status = "1";

    @Column(name = "COMPANY_CODE")
    protected Integer companyCode;

    /**
     * User who created the record.
     */
    @CreatedBy
    @NotNull
    @Column(name = "CREATED_BY_USER", updatable = false)
    protected String createdByUser;

    /**
     * Date the record was created.
     */
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_DATE", updatable = false)
    protected Date createDate;

    /**
     * User who made the last modification of the registry.
     */
    @LastModifiedBy
    @Column(name = "LAST_MODIFIED_BY_USER")
    protected String lastModifiedByUser;

    /**
     * Date of the last modification.
     */
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_MODIFIED_DATE")
    protected Date lastModifiedDate;

    /**
     * Created from ip.
     */
    @Column(name = "CREATED_FROM_IP")
    protected String createdFromIp;

    /**
     * Updated from ip.
     */
    @Column(name = "UPDATED_FROM_IP")
    protected String updatedFromIp;

    @Version
    @Column(name = "VERSION")
    protected Integer version;

    public abstract PK getId();
}
