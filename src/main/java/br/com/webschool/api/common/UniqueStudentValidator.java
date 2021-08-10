package br.com.webschool.api.common;

import java.util.Optional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.webschool.domain.model.Student;
import br.com.webschool.domain.repository.StudentRepository;

@Component
public class UniqueStudentValidator implements ConstraintValidator<UniqueStudent, String>{

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        Optional<Student> student = studentRepository.findByName(name);

        if(student.isPresent()){
            return false;
        }
        return true;
    }
}    
