package br.com.webschool.api.common;

import java.util.Optional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.webschool.domain.model.Classroom;
import br.com.webschool.domain.repository.ClassroomRepository;

@Component
public class UniqueClassroomValidator implements ConstraintValidator<UniqueClassroom, String>{

    @Autowired
    private ClassroomRepository classroomRepository;

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        Optional<Classroom> classroom = classroomRepository.findByName(name);

        if(classroom.isPresent()){
            return false;
        }
        return true;
    }
    
}
