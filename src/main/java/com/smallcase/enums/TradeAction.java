package com.smallcase.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TradeAction {
    NORMAL('N'),
    REVERT('R');

    private final Character type;

    public static TradeAction getEnum(Character type) {
        for (TradeAction tradeAction : TradeAction.values()) {
            if (tradeAction.type.equals(type))
                return tradeAction;
        }
        return null;
    }
}
