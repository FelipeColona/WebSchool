package br.com.webschool.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class EvaluationModel {
    private Long id;
    private String type;
    private String subject;
    private Long grade;
    private Long trimester;
    private StudentModel student;
}
