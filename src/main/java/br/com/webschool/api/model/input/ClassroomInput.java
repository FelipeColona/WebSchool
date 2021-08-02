package br.com.webschool.api.model.input;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.webschool.api.common.UniqueClassroom;
import br.com.webschool.api.common.UniqueChecker;
import br.com.webschool.domain.model.Teacher;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClassroomInput {
    @UniqueClassroom(groups = UniqueChecker.class)
    @NotBlank(groups = UniqueChecker.class)
    @Size(groups = UniqueChecker.class, max = 60)
    @NotBlank
    @Size(max = 60)
    private String name;

    @NotNull
    @NotNull(groups = UniqueChecker.class)
    List<Teacher> teachers;
}
