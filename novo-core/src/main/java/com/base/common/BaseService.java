package com.base.common;

import org.springframework.transaction.annotation.Transactional;

/**
 * BaseService.
 *
 * @param <T> class
 * @param <R> repository
 * @author vsangucho on 210/03/2023
 */
public class BaseService<T, R extends IQueryDslBaseRepository<T>> implements IBaseService<T> {

    /**
     * Repository.
     */
    protected final R repository;

    /**
     * Constructor.
     *
     * @param repository repository
     * @author vsangucho on 2022/09/26
     */
    public BaseService(R repository) {
        this.repository = repository;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void save(T obj) {
        this.repository.save(obj);
    }

}
