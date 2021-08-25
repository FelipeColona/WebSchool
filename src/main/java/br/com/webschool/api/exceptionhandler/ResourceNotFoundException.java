package br.com.webschool.api.exceptionhandler;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import br.com.webschool.api.exceptionhandler.ErrorDetails.Field;
import lombok.Getter;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
@Getter
public class ResourceNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	private List<Field> fields;

	public ResourceNotFoundException(List<Field> fields) {
		super(fields.toString());
		this.fields = fields;
	}
}
