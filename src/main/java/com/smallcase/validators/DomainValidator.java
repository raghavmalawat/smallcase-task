package com.smallcase.validators;

import com.smallcase.exception.FatalCustomException;

public interface DomainValidator<T> {

    boolean validate(T t) throws FatalCustomException;
}
