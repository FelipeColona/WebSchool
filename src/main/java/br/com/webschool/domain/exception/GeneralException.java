package br.com.webschool.domain.exception;

public class GeneralException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public GeneralException(String message){
        super(message);
    }
}
