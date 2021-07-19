package br.com.webschool.api.assembler;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import br.com.webschool.api.model.TeacherModel;
import br.com.webschool.api.model.input.TeacherInput;
import br.com.webschool.domain.model.Teacher;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class TeacherAssembler {
    private ModelMapper modelMapper;

    public TeacherModel toModel(Teacher teacher){
        return modelMapper.map(teacher, TeacherModel.class);
    }

    public List<TeacherModel> toCollectionModel(List<Teacher> teachers){
        return teachers.stream()
                .map(this::toModel)
                .toList();
    }

    public Teacher toEntity(TeacherInput teacherInput){
        return modelMapper.map(teacherInput, Teacher.class);
    }
}
