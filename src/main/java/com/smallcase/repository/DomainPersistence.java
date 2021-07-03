package com.smallcase.repository;

import com.smallcase.domainentity.Security;

import java.util.List;

public interface DomainPersistence<T> {
    Object add(T t);

    T get(T t);

    void delete(T t);

    T update(T t);

    List<T> bulkGet(Long userId, Security security, List<T> objects);
}
