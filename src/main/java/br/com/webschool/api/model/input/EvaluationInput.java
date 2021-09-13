package br.com.webschool.api.model.input;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import br.com.webschool.api.common.UpdateEvaluation;
import br.com.webschool.domain.model.Student;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EvaluationInput {

    @NotBlank(groups = UpdateEvaluation.class)
    @NotBlank
    private String type;

    @NotBlank(groups = UpdateEvaluation.class)
    @NotBlank
    private String subject;

    @NotNull(groups = UpdateEvaluation.class)
    @NotNull
    private Long grade;

    @NotNull(groups = UpdateEvaluation.class)
    @NotNull
    private Long trimester;

    @NotNull
    private Student student;
}
