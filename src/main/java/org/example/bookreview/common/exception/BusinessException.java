package org.example.bookreview.common.exception;

import lombok.Getter;
import org.example.bookreview.common.error.ErrorType;

@Getter
public class BusinessException extends RuntimeException {
    @Getter
    private final ErrorType errorType;
    private final String customMessage;

    public BusinessException(ErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
        this.customMessage = null;
    }

    public BusinessException(ErrorType errorType, String customMessage) {
        super(customMessage);
        this.errorType = errorType;
        this.customMessage = customMessage;
    }

    public String getCustomMessage() {
        return customMessage != null ? customMessage : errorType.getMessage();
    }
}