package br.com.webschool.api.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import br.com.webschool.domain.model.Student;
import br.com.webschool.domain.repository.StudentRepository;

@Repository
public class ImplementsStudentDetailsService implements UserDetailsService{

    @Autowired
    private StudentRepository studentRepository; 

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {

        Optional<Student> student = studentRepository.findByLogin(login);

        if(student.isEmpty()){
            throw new UsernameNotFoundException("User Not Found");
        }

        return student.get();
    }
    
}
