package com.smallcase.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TradeType {
    BUY('B'),
    SELL('S');

    private final Character type;

    public static TradeType getEnum(Character type) {
        for (TradeType tradeType : TradeType.values()) {
            if (tradeType.type.equals(type))
                return tradeType;
        }
        return null;
    }
}
