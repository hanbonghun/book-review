package org.example.bookreview.common.response;


import lombok.Getter;
import org.example.bookreview.common.error.ErrorMessage;
import org.example.bookreview.common.error.ErrorType;

@Getter
public class ApiResponse<T> {

    private final ResultType result;
    private final T data;
    private final ErrorMessage error;

    private ApiResponse(ResultType result, T data, ErrorMessage error) {
        this.result = result;
        this.data = data;
        this.error = error;
    }

    public static ApiResponse<Void> success() {
        return new ApiResponse<>(ResultType.SUCCESS, null, null);
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(ResultType.SUCCESS, data, null);
    }

    public static ApiResponse<?> error(ErrorType errorType) {
        return new ApiResponse<>(ResultType.ERROR, null, new ErrorMessage(errorType));
    }

    public static ApiResponse<?> error(ErrorType errorType, String key, Object value) {
        ErrorMessage errorMessage = new ErrorMessage(errorType);
        errorMessage.addData(key, value);
        return new ApiResponse<>(ResultType.ERROR, null, errorMessage);
    }

    public static ApiResponse<?> error(ErrorType errorType, String customMessage) {
        return new ApiResponse<>(ResultType.ERROR, null, new ErrorMessage(errorType, customMessage));
    }
}