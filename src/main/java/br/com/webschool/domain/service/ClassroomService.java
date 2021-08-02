package br.com.webschool.domain.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import br.com.webschool.api.assembler.ClassroomAssembler;
import br.com.webschool.api.model.input.ClassroomInput;
import br.com.webschool.domain.exception.GeneralException;
import br.com.webschool.domain.model.Classroom;
import br.com.webschool.domain.model.Teacher;
import br.com.webschool.domain.repository.ClassroomRepository;
import br.com.webschool.domain.repository.TeacherRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ClassroomService {
    ClassroomRepository classroomRepository;
    ClassroomAssembler classroomAssembler;

    TeacherRepository teacherRepository;
    
    @Transactional
    public Classroom save(ClassroomInput classroomInput){
        Classroom classroom = classroomAssembler.toEntity(classroomInput);

        classroom.getTeachers().forEach( teacher -> {
            Optional<Teacher> teacherFound = teacherRepository.findByName(teacher.getName());

            List<Classroom> classrooms = teacherFound.get().getClassrooms();
            classrooms.add(classroom);

            teacher.setClassrooms(classrooms);
        });

        return classroomRepository.save(classroom);
    }

    @Transactional
    public void delete(Long classroomId) {
        classroomRepository.removeRelationshipByClassroomId(classroomId);
        classroomRepository.deleteByClassroomId(classroomId);
    }

    @Transactional
    public Classroom update(Long classroomId, ClassroomInput classroomInput){
        Classroom classroom = classroomAssembler.toEntity(classroomInput);

        Optional<Classroom> classroomFound = classroomRepository.findById(classroomId);
        Optional<Classroom> classroomFound2 = classroomRepository.findByName(classroom.getName());

        if(classroomFound2.isEmpty() || classroomFound.get().getName().equals(classroom.getName())){
            if(classroom.getTeachers().isEmpty()){
                classroomRepository.removeRelationshipByClassroomId(classroomId);
            }else{
                classroom.getTeachers().forEach( teacher -> {
                    Optional<Teacher> teacherFound = teacherRepository.findByName(teacher.getName());
        
                    List<Classroom> classrooms = teacherFound.get().getClassrooms();
                    classrooms.clear();
                    classrooms.add(classroom);
        
                    teacher.setClassrooms(classrooms);
                });
            }
    
            classroom.setId(classroomId);
    
            return classroomRepository.save(classroom);
        }else{
            throw new GeneralException("Class name must be unique");
        }
    }
}
