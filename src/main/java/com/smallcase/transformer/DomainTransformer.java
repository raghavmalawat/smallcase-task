package com.smallcase.transformer;

public interface DomainTransformer<T1,T2> {
    T2 transformObject(T1 t1);

    T2 transformObject(T2 t2, T1 t1);
}