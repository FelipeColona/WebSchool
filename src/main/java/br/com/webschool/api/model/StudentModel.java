package br.com.webschool.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import br.com.webschool.domain.model.Classroom;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class StudentModel {
    private Long id;
    private String name;
    private String login;
    private Classroom classroom;
}
