package com.base.entity;

import com.base.common.AbstractBaseAuditable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Contrato entity.
 *
 * @author vsangucho on 24/03/2023
 * @version 1.0
 */
@Getter
@Setter
@SuperBuilder
@Entity(name = "AFILIACION")
public class AfiliacionEntity extends AbstractBaseAuditable<Long> {

    @Id
    @Column(name = "ID")
    private Long id;

    @Override
    public Long getId() {
        return this.id;
    }

    /**
     * numeroAfiliado dentro de la familia
     */
    @Column(nullable=false)
    private Integer numero;

    @Column(nullable = false)
    private Long numeroContrato;


}
