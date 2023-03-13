package com.base.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import com.base.common.AbstractBaseAuditable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

/**
 * Person entity.
 *
 * @author vsangucho on 10/03/2023
 * @version 1.0
 */
@Getter
@Setter
@SuperBuilder
@Entity(name = "PERSON")
public class PersonEntity extends AbstractBaseAuditable<String> {

    private static final long serialVersionUID = 2L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "PERSON_ID")
    private String personId;

    @Column(name = "DOCUMENT_NUMBER")
    private String documentNumber;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "EMAIL")
    private String email;

    /**
     * Get entity id.
     */
    @Override
    public String getId() {
        return this.personId;
    }
}
