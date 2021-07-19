package br.com.webschool.api.model.input;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeacherInput {
    @NotBlank
    @Size(max = 255)
    private String name;
}
