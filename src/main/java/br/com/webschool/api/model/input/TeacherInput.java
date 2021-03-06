package br.com.webschool.api.model.input;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.webschool.api.common.UniqueChecker;
import br.com.webschool.api.common.UniqueTeacher;
import br.com.webschool.domain.model.Classroom;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeacherInput {
    @UniqueTeacher(groups = UniqueChecker.class)
    @NotBlank(groups = UniqueChecker.class)
    @Size(max = 255, groups = UniqueChecker.class)
    @NotBlank
    @Size(max = 255)
    private String name;

    @NotNull
    @NotNull(groups = UniqueChecker.class)
    private List<Classroom> classrooms;
}
