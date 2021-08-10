package br.com.webschool.api.model.input;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import br.com.webschool.api.common.UniqueChecker;
import br.com.webschool.api.common.UniqueStudent;
import br.com.webschool.domain.model.Classroom;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentInput {
    @UniqueStudent(groups = UniqueChecker.class)
    @NotBlank(groups = UniqueChecker.class)
    @Size(max = 255, groups = UniqueChecker.class)
    @NotBlank
    @Size(max = 255)
    private String name;

    @NotEmpty
    @NotEmpty(groups = UniqueChecker.class)
    private Classroom classroom;
}
