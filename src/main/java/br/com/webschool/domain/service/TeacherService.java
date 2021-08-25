package br.com.webschool.domain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.webschool.api.assembler.TeacherAssembler;
import br.com.webschool.api.exceptionhandler.ErrorDetails.Field;
import br.com.webschool.api.exceptionhandler.NotUniqueException;
import br.com.webschool.api.exceptionhandler.ResourceNotFoundException;
import br.com.webschool.api.model.input.TeacherInput;
import br.com.webschool.domain.model.Classroom;
import br.com.webschool.domain.model.Teacher;
import br.com.webschool.domain.repository.ClassroomRepository;
import br.com.webschool.domain.repository.TeacherRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Service
public class TeacherService {
    private TeacherRepository teacherRepository;
    private TeacherAssembler teacherAssembler;

    private ClassroomRepository classroomRepository;

    @Getter
    @Setter
    @AllArgsConstructor
    class LoginAndPassword {
        private String login;
        private String password;
    }


    public Teacher search(Long teacherId){
        List<Field> fields = new ArrayList<>();
        fields.add(new Field("name", "Teacher not found"));

        return teacherRepository.findById(teacherId)
        .orElseThrow(() -> new ResourceNotFoundException(fields));
    }

    @Transactional
    public Teacher save(TeacherInput teacherInput){

        teacherInput.getClassrooms().forEach( classroom -> {
            Optional<Classroom> existingClassroom = classroomRepository.findByName(classroom.getName());

            if(existingClassroom.isEmpty()){
                List<Field> fields = new ArrayList<>();
                fields.add(new Field("classroom", "Classroom not found"));
                throw new ResourceNotFoundException(fields);
            }

            classroom.setId(existingClassroom.get().getId());
        });

        Teacher teacher = teacherAssembler.toEntity(teacherInput);
        LoginAndPassword loginAndPassword = this.genLoginAndPassword();
        teacher.setLogin(loginAndPassword.getLogin());
        
        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
        String encodedPassword = bcrypt.encode(loginAndPassword.getPassword());

        teacher.setPassword(encodedPassword);
        Teacher savedTeacher = teacherRepository.save(teacher);

        Teacher t1 = new Teacher();
        t1.setId(savedTeacher.getId());
        t1.setName(savedTeacher.getName());
        t1.setLogin(savedTeacher.getLogin());
        t1.setPassword(loginAndPassword.getPassword());
        t1.setClassrooms(savedTeacher.getClassrooms());

        return t1;
    }

    @Transactional
    private LoginAndPassword genLoginAndPassword(){
        List<Integer> loginArray = new ArrayList<>();
        List<Integer> passwordArray = new ArrayList<>();

        for(int i = 0; i < 6; i ++){
            int randomNumber = (int) Math.floor(Math.random()*(9 - 0 + 1) + 0);
            loginArray.add(randomNumber);
        }

        for(int i = 0; i < 5; i ++){
            int randomNumber = (int) Math.floor(Math.random()*(9 - 0 + 1) + 0);
            passwordArray.add(randomNumber);
        }

        StringBuilder sbLogin = new StringBuilder();
        for (int i : loginArray)
        {
            sbLogin.append(i);
        }
        
        StringBuilder sbPassword = new StringBuilder();
        for (int i : passwordArray)
        {
            sbPassword.append(i);
        }

        String login = sbLogin.toString();
        String password = sbPassword.toString();

        Optional<Teacher> existingTeacher = teacherRepository.findByLogin(login);

        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

        if(existingTeacher.isPresent()){

            if(bcrypt.matches(password, existingTeacher.get().getPassword())){
                this.genLoginAndPassword();
            }
        }
        return new LoginAndPassword(login, password);
    } 

    @Transactional
    public Teacher update(Long teacherId, TeacherInput teacherInput){
        Teacher teacher = teacherAssembler.toEntity(teacherInput);

        Teacher teacherFound = teacherRepository.findById(teacherId).get();
        Optional<Teacher> teacherFound2 = teacherRepository.findByName(teacher.getName());

        if(teacherFound2.isEmpty() || teacherFound.getName().equals(teacher.getName())){
            teacher.setId(teacherId);
            teacher.setLogin(teacherFound.getLogin());
            teacher.setPassword(teacherFound.getPassword());
    
            if(teacher.getClassrooms().isEmpty()){
                List<Classroom> l1 = new ArrayList<>();
                teacher.setClassrooms(l1);
            }else{
                teacher.getClassrooms().forEach( classroom -> {
                    Optional<Classroom> classroomFound = classroomRepository.findByName(classroom.getName());
    
                    if(classroomFound.isEmpty()){
                        List<Field> fields = new ArrayList<>();
                        fields.add(new Field("classroom", "Classroom not found"));
                        throw new ResourceNotFoundException(fields);
                    }
    
                    classroom.setId(classroomFound.get().getId());
                });
            }
    
            return teacherRepository.save(teacher);
        }else{
            List<Field> fields = new ArrayList<>();
            fields.add(new Field("name", "Teacher name must be unique"));
            throw new NotUniqueException(fields);
        }
    }

    @Transactional
    public void delete(Long teacherId){
        teacherRepository.deleteById(teacherId);
    }

}
