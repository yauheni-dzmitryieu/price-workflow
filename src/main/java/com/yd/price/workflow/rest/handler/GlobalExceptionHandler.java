package com.yd.price.workflow.rest.handler;

import com.yd.price.workflow.config.properties.ErrorMessages;
import com.yd.price.workflow.exception.ErrorCode;
import com.yd.price.workflow.exception.ErrorCodeAwareException;
import com.yd.price.workflow.model.ErrorResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@EnableConfigurationProperties(ErrorMessages.class)
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private static final String UNKNOWN_ERROR_TYPE = "Unknown error type";

    private final ErrorMessages errorMessages;

    public GlobalExceptionHandler(ErrorMessages errorMessages) {
        this.errorMessages = errorMessages;
    }

    @ExceptionHandler(ErrorCodeAwareException.class)
    public ResponseEntity<ErrorResponse> handleException(final ErrorCodeAwareException exception) {
        log.debug("Accepted exception: {}", exception.getMessage(), exception);
        return createResponseEntity(exception.getErrorCode(), exception.getArguments());
    }


    /**
     * Handles {@link HttpMessageNotReadableException}.
     *
     * @param e exception, to be handled
     * @return response entity with error
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(final HttpMessageNotReadableException e) {
        return createResponseEntity(ErrorCode.MALFORMED_PAYLOAD, extractReadableMessagePart(e));
    }

    private String extractReadableMessagePart(final Throwable throwable) {
        final var nestedExceptionPart = "nested exception is";
        if (throwable != null && throwable.getMessage().contains(nestedExceptionPart)) {
            return throwable.getMessage().substring(0, throwable.getMessage().indexOf(';'));
        }
        return null;
    }


    /**
     * Handles {@link MethodArgumentTypeMismatchException}.
     *
     * @param exception exception, to be handled
     * @return response entity with error
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException exception) {
        return createResponseEntity(ErrorCode.INVALID_PARAMETER, exception.getValue());
    }

    /**
     * Handles {@link PropertyReferenceException}.
     *
     * @param exception exception, to be handled
     * @return response entity with error
     */
    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<ErrorResponse> handlePropertyReferenceException(final PropertyReferenceException exception) {
        return createResponseEntity(ErrorCode.INVALID_PARAMETER, exception.getMessage());
    }


    /**
     * Handles {@link HttpRequestMethodNotSupportedException}.
     *
     * @param e exception, to be handled
     * @return response entity with error
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupportedException(final HttpRequestMethodNotSupportedException e) {
        log.info("Unsupported operation: ", e);
        return createResponseEntity(ErrorCode.NOT_IMPLEMENTED);
    }

    //TODO: Update response error with multiple error codes.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationErrors(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles all other exceptions, which are not covered with specific exception handlers.
     *
     * @param exception exception, to be handled
     * @return response entity with error
     */
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorResponse> handleException(final Throwable exception) {
        log.error("Internal server error: ", exception);
        return createResponseEntity(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse> createResponseEntity(ErrorCode errorCode, Object... arguments) {
        ErrorMessages.ResponseMessage responseMessage = errorMessages.getResponseMessage(errorCode.getCode());
        String template = responseMessage.getMessage();

        int httpStatus = responseMessage.getCode();
        return ResponseEntity.status(httpStatus)
                .body(new ErrorResponse(String.valueOf(httpStatus), getMessage(template, arguments)));
    }

    private String getMessage(String message, Object... arguments) {
        return MessageFormat.format(StringUtils.defaultIfEmpty(message, UNKNOWN_ERROR_TYPE), arguments);
    }
}
