package br.com.webschool.domain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.webschool.api.assembler.TeacherAssembler;
import br.com.webschool.api.model.input.TeacherInput;
import br.com.webschool.domain.exception.GeneralException;
import br.com.webschool.domain.model.Teacher;
import br.com.webschool.domain.repository.TeacherRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Service
public class TeacherService {
    private TeacherRepository teacherRepository;
    private TeacherAssembler teacherAssembler;

    @Getter
    @Setter
    @AllArgsConstructor
    class LoginAndPassword {
        private String login;
        private String password;
    }


    public Teacher search(Long teacherId){
        return teacherRepository.findById(teacherId)
        .orElseThrow(() -> new GeneralException("Teacher not found"));
    }

    @Transactional
    public Teacher save(TeacherInput teacherInput){

        Teacher teacher = teacherAssembler.toEntity(teacherInput);
        LoginAndPassword loginAndPassword = this.genLoginAndPassword();
        teacher.setLogin(loginAndPassword.getLogin());
        teacher.setPassword(loginAndPassword.getPassword());
        
        return teacherRepository.save(teacher);
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

        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

        Optional<Teacher> existingTeacher = teacherRepository.findByLogin(login);

        if(existingTeacher.isPresent()){

            if(bcrypt.matches(password, existingTeacher.get().getPassword())){
                this.genLoginAndPassword();
            }
        }
        return new LoginAndPassword(login, bcrypt.encode(password));
    } 

    @Transactional
    public Teacher update(Long teacherId, TeacherInput teacherInput){
        Teacher teacher = teacherAssembler.toEntity(teacherInput);

        Teacher teacherFound = teacherRepository.findById(teacherId).get();

        teacher.setId(teacherId);
        teacher.setLogin(teacherFound.getLogin());
        teacher.setPassword(teacherFound.getPassword());

        return teacherRepository.save(teacher);
    }

    @Transactional
    public void delete(Long teacherId){
        teacherRepository.deleteById(teacherId);
    }

}
