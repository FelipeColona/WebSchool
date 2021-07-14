package br.com.webschool.api.exceptionhandler;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private MessageSource messageSource;

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        
        List<Problem.Field> fields = new ArrayList<>();

        Problem problem = new Problem();
        problem.setStatus(status.value());
        problem.setDateTime(OffsetDateTime.now());
        problem.setTitle("There are one or more invalid fields. Please fill it in the correct way and try again.");
        problem.setFields(fields);
                
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String nome = ((FieldError) error).getField();
            String mensagem = messageSource.getMessage(error, LocaleContextHolder.getLocale());

            fields.add(new Problem.Field(nome, mensagem));
        });

        return handleExceptionInternal(ex, problem, headers, status, request);
    }
}
