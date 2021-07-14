package br.com.webschool.domain.service;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.stereotype.Service;

import br.com.webschool.domain.exception.GeneralException;
import br.com.webschool.domain.model.Teacher;
import br.com.webschool.domain.repository.TeacherRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class TeacherService {
    private TeacherRepository teacherRepository;

    public Teacher search(Long teacherId){
        return teacherRepository.findById(teacherId)
        .orElseThrow(() -> new GeneralException("Teacher not found"));
    }

    @Transactional
    public Teacher save(Teacher teacher){

        boolean emailInUse = teacherRepository.findByEmail(teacher.getEmail()).stream()
                .anyMatch(existingTeacher -> !existingTeacher.equals(teacher));

        if(emailInUse){
            throw new GeneralException("There is already a registered teacher with this email");
        }

        return teacherRepository.save(teacher);
    }

    @Transactional
    public void delete(Long teacherId){
        teacherRepository.deleteById(teacherId);
    }
}
