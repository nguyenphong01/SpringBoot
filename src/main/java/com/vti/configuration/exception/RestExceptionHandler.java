package com.vti.configuration.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler implements AuthenticationEntryPoint, AccessDeniedHandler {
    @Autowired
    private MessageSource messageSource;

    private String getMessage(String code, String... args) {
        String defaultMessage = "Lỗi sever";
        return messageSource.getMessage(code, args, "Lỗi sever", LocaleContextHolder.getLocale());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception exception) {
        int code = 1;
        String message = getMessage("Exception.message");
        String detailMessage = exception.getLocalizedMessage();
        ErrorResponse response = new ErrorResponse(code, message, detailMessage);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException exception,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        int code = 2;
        String message = getMessage("NoHandlerFoundException.message")
                + exception.getHttpMethod() + " " + exception.getRequestURL();
        String detailMessage = exception.getLocalizedMessage();
        ErrorResponse response = new ErrorResponse(code, message, detailMessage);
        return new ResponseEntity<>(response, status);
    }

    private String getMessageFromHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        StringBuilder message = new StringBuilder(exception.getMethod());
        message.append(" ").append(getMessage("HttpRequestMethodNotSupportedException.message"));
        for (HttpMethod method : exception.getSupportedHttpMethods()) {
            message.append(method).append(" ");
        }
        return message.toString();
    }

    private String getMessageFromHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException exception) {
        StringBuilder message = new StringBuilder(exception.getContentType() + getMessage("HttpMediaTypeNotSupportedException.message"));
        for (MediaType method : exception.getSupportedMediaTypes()) {
            message.append(method).append(", ");
        }
        return message.substring(0, message.length() - 2);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException exception,
            HttpHeaders headers, HttpStatus status,
            WebRequest request
    ) {
        int code = 4;
        String message = getMessageFromHttpMediaTypeNotSupportedException(exception);
        String detailMessage = exception.getLocalizedMessage();
        ErrorResponse response = new ErrorResponse(code, message, detailMessage);
        return new ResponseEntity<>(response, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException exception,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        int code = 3;
        String message = getMessageFromHttpRequestMethodNotSupportedException(exception);
        String detailMessage = exception.getLocalizedMessage();
        ErrorResponse response = new ErrorResponse(code, message, detailMessage);
        return new ResponseEntity<>(response, status);
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        int code = 5;
        String message = getMessage("MethodArgumentNotValidException.message");
        String detailMessage = exception.getLocalizedMessage();
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : exception.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        ErrorResponse response = new ErrorResponse(code, message, detailMessage, errors);
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException exception) {
        int code = 6;
        String message = getMessage("ConstraintViolationException.message");
        String detailMessage = exception.getLocalizedMessage();
        Map<String, String> errors = new HashMap<>();
        for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            errors.put(violation.getPropertyPath().toString(), violation.getMessage());
        }
        ErrorResponse response = new ErrorResponse(code, message, detailMessage, errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException exception,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request
    ) {
        int code = 7;
        String message = exception.getParameterName() + " " + getMessage("MissingServletRequestParameterException.message");
        String detailMessage = exception.getLocalizedMessage();
        ErrorResponse response = new ErrorResponse(code, message, detailMessage);
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException exception) {
        int code = 8;
        String message = exception.getName() + getMessage("MethodArgumentTypeMismatchException.message")
                + exception.getRequiredType().getName();
        String detailMessage = exception.getLocalizedMessage();
        ErrorResponse response = new ErrorResponse(code, message, detailMessage);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        int code = 9;
        String message = getMessage("AuthenticationException.message");
        String detailMessage = exception.getLocalizedMessage();
        ErrorResponse errorResponse = new ErrorResponse(code, message, detailMessage);

        // convert object to json
        String json = new ObjectMapper()
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(errorResponse);
        // return json
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(json);
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) throws IOException {
        int code = 10;
        String message = getMessage("AccessDeniedException.message");
        String detailMessage = exception.getLocalizedMessage();
        ErrorResponse errorResponse = new ErrorResponse(code, message, detailMessage);

        // convert object to json
        String json = new ObjectMapper()
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(errorResponse);

        // return json
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(json);
    }
}
