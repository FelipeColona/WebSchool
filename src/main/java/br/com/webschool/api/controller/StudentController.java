package br.com.webschool.api.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.webschool.domain.model.Student;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Controller
    public class PageRenderer {
        @GetMapping("/students/login")
        public String loginPage(){
            return "students-login";
        }

        @GetMapping("/students")
        public String home(){
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if(this.isStudent(principal)){
                return "students-home";
            }else {
                return "redirect:" + "/403";
            }
        }

        public boolean isStudent(Object principal) {
            try{
                ((Student)principal).getName();
                return true;
            }catch (ClassCastException ex){
                return false;
            }
        }
    }

}
