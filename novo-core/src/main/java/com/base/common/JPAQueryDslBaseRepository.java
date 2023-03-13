package com.base.common;

import java.util.Date;
import com.base.security.audit.IKeycloakUserInfo;
import com.base.util.DateUtil;
import com.querydsl.core.dml.UpdateClause;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAUpdateClause;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

/**
 * Repository base with support querydsl.
 *
 * @param <T> Entity
 * @author vsangucho on 10/03/2023
 * @version 1.0
 */
public abstract class JPAQueryDslBaseRepository<T> extends QuerydslRepositorySupport implements
    IQueryDslBaseRepository<T> {

    @Lazy
    @Autowired
    private IKeycloakUserInfo keycloakUserInfo;


    /**
     * Entity class
     */
    private final Class<T> domainClass;

    /**
     * Creates a new {@link QuerydslRepositorySupport} instance for the given domain type.
     *
     * @param domainClass must not be {@literal null}.
     * @author vsangucho on 2022/09/26
     */
    public JPAQueryDslBaseRepository(Class<T> domainClass) {
        super(domainClass);
        this.domainClass = domainClass;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(T obj) {
        getEntityManager().persist(obj);
    }

    /**
     * Update with audit fields.
     *
     * @param path EntityPath
     * @return UpdateClause
     */
    protected UpdateClause<JPAUpdateClause> updateWithAudit(EntityPath<?> path) {
        UpdateClause<JPAUpdateClause> update = super.update(path);
        update.set(Expressions.path(String.class,
                getNameFromPath(QAbstractBaseAuditable.abstractBaseAuditable.lastModifiedByUser)),
            keycloakUserInfo.getUserId());
        update.set(Expressions.path(Date.class,
                getNameFromPath(QAbstractBaseAuditable.abstractBaseAuditable.lastModifiedDate)),
            DateUtil.currentDate());
        return update;
    }

    /**
     * Find page by query.
     *
     * @param query JPQLQuery
     * @param pageable Pageable
     * @param count boolean
     * @param <Q> entity
     * @return Page
     * @author vsangucho on 2022/09/26
     */
    protected <Q> Page<Q> findPagedData(JPQLQuery<Q> query, Pageable pageable, boolean count) {
        long totalSupplier = count ? query.fetchCount() : 0L;
        return new PageImpl<>(getQuerydsl().applyPagination(pageable, query).fetch(), pageable,
            totalSupplier);
    }

    /**
     * Get name from path.
     *
     * @param path Path
     * @return name
     */
    protected String getNameFromPath(Path<?> path) {
        return path.getMetadata().getName();
    }

}
