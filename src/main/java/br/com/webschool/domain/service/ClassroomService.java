package br.com.webschool.domain.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import br.com.webschool.api.assembler.ClassroomAssembler;
import br.com.webschool.api.model.input.ClassroomInput;
import br.com.webschool.domain.exception.GeneralException;
import br.com.webschool.domain.model.Classroom;
import br.com.webschool.domain.model.Student;
import br.com.webschool.domain.model.Teacher;
import br.com.webschool.domain.repository.ClassroomRepository;
import br.com.webschool.domain.repository.StudentRepository;
import br.com.webschool.domain.repository.TeacherRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ClassroomService {
    ClassroomRepository classroomRepository;
    ClassroomAssembler classroomAssembler;

    TeacherRepository teacherRepository;
    StudentRepository studentRepository;
    
    @Transactional
    public Classroom save(ClassroomInput classroomInput){
        Classroom classroom = classroomAssembler.toEntity(classroomInput);

        classroom.getTeachers().forEach( teacher -> {
            Optional<Teacher> teacherFound = teacherRepository.findByName(teacher.getName());

            List<Classroom> classrooms = teacherFound.get().getClassrooms();
            classrooms.add(classroom);

            teacher.setClassrooms(classrooms);
        });

        classroom.getStudents().forEach( student -> {
            Optional<Student> studentFound = studentRepository.findByName(student.getName());
            
            student.setId(studentFound.get().getId());
            student.setClassroom(classroom);
        });

        return classroomRepository.save(classroom);
    }

    @Transactional
    public void delete(Long classroomId) {
        classroomRepository.removeTeacherRelationshipByClassroomId(classroomId);
        classroomRepository.removeAllStudentRelationshipByClassroomId(classroomId);
        classroomRepository.deleteByClassroomId(classroomId);
    }

    @Transactional
    public Classroom update(Long classroomId, ClassroomInput classroomInput){
        Classroom classroom = classroomAssembler.toEntity(classroomInput);

        Optional<Classroom> classroomFound = classroomRepository.findById(classroomId);
        Optional<Classroom> classroomFound2 = classroomRepository.findByName(classroom.getName());

        classroomRepository.removeTeacherRelationshipByClassroomId(classroomId);

        if(classroomFound2.isEmpty() || classroomFound.get().getName().equals(classroom.getName())){
            if(classroom.getTeachers().isEmpty()){
                //classroomRepository.removeTeacherRelationshipByClassroomId(classroomId);
            }else{
                classroom.getTeachers().forEach( teacher -> {
                    Optional<Teacher> teacherFound = teacherRepository.findByName(teacher.getName());
                    
                    List<Classroom> classrooms = teacherFound.get().getClassrooms();
                    classrooms.clear();
                    classrooms.add(classroom);
        
                    teacher.setClassrooms(classrooms);
                });

/*                 if(classroomFound.get().getTeachers().size() > classroom.getTeachers().size()){
                    
                } */
                  
            }

            //classroomRepository.removeTeacherRelationshipByClassroomId(classroomId);


            if(!classroom.getStudents().isEmpty()){
                classroom.getStudents().forEach( student -> {
                    Optional<Student> studentFound = studentRepository.findByName(student.getName());
                    
                    student.setId(studentFound.get().getId());
                    student.setClassroom(classroom);
                });
            }

            classroom.setId(classroomId);
            return classroomRepository.save(classroom);

        }else{
            throw new GeneralException("Class name must be unique");
        }
    }
}
