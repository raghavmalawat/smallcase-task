package com.smallcase.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SecurityType {
    STOCK('S'),
    BOND('B'),
    COMMODITY('C'),
    CURRENCY('R'),
    CASH_EQUIVALENT('E');

    private final Character type;

    public static SecurityType getEnum(Character type) {
        for (SecurityType securityType : SecurityType.values()) {
            if (securityType.type.equals(type))
                return securityType;
        }
        return null;
    }
}
