package br.com.webschool.domain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.webschool.api.assembler.StudentAssembler;
import br.com.webschool.api.model.input.StudentInput;
import br.com.webschool.domain.exception.GeneralException;
import br.com.webschool.domain.model.Classroom;
import br.com.webschool.domain.model.Student;
import br.com.webschool.domain.repository.ClassroomRepository;
import br.com.webschool.domain.repository.StudentRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Service
public class StudentService {
    StudentRepository studentRepository;
    ClassroomRepository classroomRepository;
    StudentAssembler studentAssembler;

    @Transactional
    public Student save(StudentInput studentInput){

        Optional<Classroom> existingClassroom = classroomRepository.findByName(studentInput.getClassroom().getName());

        if(existingClassroom.isEmpty()){
            throw new GeneralException("Class not found");
        }

        studentInput.getClassroom().setId(existingClassroom.get().getId());

        Student student = studentAssembler.toEntity(studentInput);
        LoginAndPassword loginAndPassword = this.genLoginAndPassword();
        student.setLogin(loginAndPassword.getLogin());
        
        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
        String encodedPassword = bcrypt.encode(loginAndPassword.getPassword());

        student.setPassword(encodedPassword);
        Student savedstudent = studentRepository.save(student);

        Student t1 = new Student();
        t1.setId(savedstudent.getId());
        t1.setName(savedstudent.getName());
        t1.setLogin(savedstudent.getLogin());
        t1.setPassword(loginAndPassword.getPassword());
        t1.setClassroom(savedstudent.getClassroom());

        return t1;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    class LoginAndPassword {
        private String login;
        private String password;
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

        Optional<Student> existingStudent = studentRepository.findByLogin(login);

        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

        if(existingStudent.isPresent()){

            if(bcrypt.matches(password, existingStudent.get().getPassword())){
                this.genLoginAndPassword();
            }
        }
        return new LoginAndPassword(login, password);
    }


    @Transactional
    public Student update(Long studentId, StudentInput studentInput){
        Student student = studentAssembler.toEntity(studentInput);

        Student studentFound = studentRepository.findById(studentId).get();
        Optional<Student> studentFound2 = studentRepository.findByName(student.getName());

        if(studentFound2.isEmpty() || studentFound.getName().equals(student.getName())){
            student.setId(studentId);
            student.setLogin(studentFound.getLogin());
            student.setPassword(studentFound.getPassword());
    
            Optional<Classroom> classroomFound = classroomRepository.findByName(student.getClassroom().getName());

            if(classroomFound.isEmpty()){
                throw new GeneralException("Classroom not found");
            }

            student.getClassroom().setName(classroomFound.get().getName());
            student.getClassroom().setId(classroomFound.get().getId());
    
            return studentRepository.save(student);
        }else{
            throw new GeneralException("Student name must be unique");
        }
    }


    @Transactional
    public void delete(Long studentId){
        studentRepository.deleteById(studentId);
    }
}
