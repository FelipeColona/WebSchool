package br.com.webschool.api.common;

import java.util.Optional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.webschool.domain.model.Teacher;
import br.com.webschool.domain.repository.TeacherRepository;

@Component
public class UniqueTeacherValidator implements ConstraintValidator<UniqueTeacher, String>{

    @Autowired
    private TeacherRepository teacherRepository;

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        Optional<Teacher> teacher = teacherRepository.findByName(name);

        if(teacher.isPresent()){
            return false;
        }
        return true;
    }
    
}