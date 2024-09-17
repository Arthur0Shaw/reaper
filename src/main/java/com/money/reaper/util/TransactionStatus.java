package com.money.reaper.util;

import java.util.HashMap;
import java.util.Map;

public enum TransactionStatus {

    PENDING("PENDING", "T0"),
    SUCCESS("SUCCESS", "T1"),
    FAILURE("FAILURE", "T3");

    private final String displayName;
    private final String code;
    
    private static final Map<String, TransactionStatus> CODE_TO_STATUS_MAP = new HashMap<>();

    static {
        for (TransactionStatus status : values()) {
            CODE_TO_STATUS_MAP.put(status.code, status);
        }
    }

    TransactionStatus(String displayName, String code) {
        this.displayName = displayName;
        this.code = code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getCode() {
        return code;
    }

    public static TransactionStatus fromCode(String code) {
        return CODE_TO_STATUS_MAP.get(code);
    }

    @Override
    public String toString() {
        return displayName;
    }
}
