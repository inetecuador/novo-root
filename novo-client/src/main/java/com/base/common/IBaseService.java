package com.base.common;

/**
 * IBaseService.
 *
 * @param <T>
 * @author vsangucho on 10/03/2023
 * @version 1.0
 */
public interface IBaseService<T> {

    /**
     * Save entity
     *
     * @param obj Entity to save
     */
    void save(T obj);
}
