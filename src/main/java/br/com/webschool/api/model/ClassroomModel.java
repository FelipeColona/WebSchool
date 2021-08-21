package br.com.webschool.api.model;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import br.com.webschool.api.common.UniqueClassroom;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClassroomModel {
    private Long id;

    @UniqueClassroom
    @NotBlank
    @Size(max = 60)
    private String name;

    private List<TeacherModel> teachers;

    private List<StudentModel> students;
}
