package br.com.webschool.domain.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import br.com.webschool.api.common.UniqueClassroom;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
@Entity
@JsonInclude(Include.NON_NULL)
public class Classroom {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @UniqueClassroom
    @NotBlank
    @Size(max = 60)
    private String name;

    @ManyToMany(mappedBy = "classrooms")
    @NotNull
    private List<Teacher> teachers;

    @OneToMany(mappedBy = "classroom")
    private List<Student> students;
}
