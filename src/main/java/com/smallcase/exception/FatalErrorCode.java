package com.smallcase.exception;

import lombok.Getter;

@Getter
public enum FatalErrorCode {

    ERROR_TRADE_INFO_INVALID(501, "Invalid Trade Details."),
    ERROR_TRADE_DTO_INVALID(502, "Invalid Request"),
    ERR0R_TRADE_EXECUTION_INVALID(503, "Invalid Trade Request"),
    ERROR_TRADE_QUANTITY_INVALID(504, "Trade Quantity Invalid"),
    ERROR_TRADE_ID_INVALID(504, "no Trade exists with the trade Id");

    private final Integer type;
    private final String customMessage;

    FatalErrorCode(Integer type, String name) {
        this.type = type;
        this.customMessage = name;
    }

    public static String getCustomMessage(Integer type) {
        for (FatalErrorCode fatalErrorCode : FatalErrorCode.values()) {
            if (fatalErrorCode.type.equals(type)) {
                return fatalErrorCode.customMessage;
            }
        }
        return null;
    }

    public static FatalErrorCode getErrorCode(Integer type) {
        for (FatalErrorCode fatalErrorCode : FatalErrorCode.values()) {
            if (fatalErrorCode.type.equals(type)) {
                return fatalErrorCode;
            }
        }
        return null;
    }
}
