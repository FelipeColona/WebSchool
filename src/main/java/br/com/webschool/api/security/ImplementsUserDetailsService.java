package br.com.webschool.api.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import br.com.webschool.domain.model.Teacher;
import br.com.webschool.domain.repository.TeacherRepository;

@Repository
public class ImplementsUserDetailsService implements UserDetailsService{

    @Autowired
    private TeacherRepository teacherRepository; 

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {

        Optional<Teacher> teacher = teacherRepository.findByLogin(login);

        if(teacher.isEmpty()){
            throw new UsernameNotFoundException("User Not Found");
        }

        return teacher.get();
    }
    
}
