package com.smallcase.repository;

import java.util.List;

public interface DomainPersistence<T> {
    Object add(T t);

    T get(T t);

    void delete(T t);

    void update(T t);

    List<T> bulkGet(Long userId, List<T> objects);
}
