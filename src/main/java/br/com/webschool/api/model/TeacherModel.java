package br.com.webschool.api.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import br.com.webschool.domain.model.Classroom;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class TeacherModel {
    private Long id;
    private String name;
    private String login;
    List<Classroom> classrooms;
}
