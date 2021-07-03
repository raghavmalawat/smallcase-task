package com.smallcase.repository;

public interface DomainPersistence<T> {
    Object add(T t);

    T get(T t);

    void delete(T t);

    T update(T t);
}
