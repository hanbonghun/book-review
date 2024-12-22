package org.example.bookreview.common.error;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public class ErrorMessage {
    private final String code;
    private final String message;
    private final Map<String, Object> data;

    public ErrorMessage(ErrorType errorType) {
        this.code = errorType.getCode();
        this.message = errorType.getMessage();
        this.data = new HashMap<>();
    }

    public ErrorMessage(ErrorType errorType, String customMessage) {
        this.code = errorType.getCode();
        this.message = customMessage;
        this.data = new HashMap<>();
    }

    public void addData(String key, Object value) {
        this.data.put(key, value);
    }
}