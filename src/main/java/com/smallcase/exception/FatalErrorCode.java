package com.smallcase.exception;

import lombok.Getter;

@Getter
public enum FatalErrorCode {

    ERROR_TRADE_INFO_INVALID(501, "Invalid Trade Details."),
    ERROR_TRADE_DTO_INVALID(502, "Invalid Request"),
    ERR0R_UNKNOWN_RECORD_TYPE(503, "Invalid Record Type");

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
