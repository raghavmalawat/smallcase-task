package com.smallcase.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Status {
    ACTIVE('A'),
    INACTIVE('I');

    private final Character type;

    public static Status getEnum(Character type) {
        for (Status status : Status.values()) {
            if (status.type.equals(type))
                return status;
        }
        return null;
    }
}
