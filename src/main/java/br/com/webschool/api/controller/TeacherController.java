package br.com.webschool.api.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.webschool.domain.model.Teacher;
import br.com.webschool.domain.repository.TeacherRepository;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/teachers")
@AllArgsConstructor
public class TeacherController {
    TeacherRepository teacherRepository;

    @Controller
    public class PageRenderer {
        @GetMapping("/teachers/login")
        public String loginPage(){
            return "teachers-login";
        }

        @GetMapping("/teachers")
        public String home(Model model){
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if (this.isTeacher(principal)) {
                String username = ((Teacher)principal).getName();
                
                Teacher currentTeacher = teacherRepository.findByName(username).get();

                model.addAttribute("classrooms", currentTeacher.getClassrooms());
                return "teachers-home";
                
            } else {
                return "redirect:" + "/403";
            }
        }

        @GetMapping("/teachers/{classroomNameUrl}")
        public String temp(@PathVariable String classroomNameUrl, Model model, HttpServletRequest request){
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if(this.isTeacher(principal)){
                String username = ((Teacher)principal).getName();

                String classroomName = classroomNameUrl.replaceAll("_", " ");

                Teacher currentTeacher = teacherRepository.findByName(username).get();
                boolean isAllowed = currentTeacher.getClassrooms().stream().anyMatch( classroom -> classroom.getName().equals(classroomName) );

                if(isAllowed){
                    return "teachers-home";
                }

                String currentUrl = request.getRequestURL().toString();

                int index = currentUrl.indexOf("/");
                List<Integer> indexes = new ArrayList<>();

                while (index >= 0) {
                    indexes.add(index);
                    index = currentUrl.indexOf("/", index + 1);
                }

                index = indexes.get(indexes.size() - 1);

                return "redirect:" + request.getRequestURL().delete(index, request.getRequestURL().length()) + "?error";
                
            }else{
                return "redirect:" + "/403";
            }
        }

        public boolean isTeacher(Object principal) {
            try{
                ((Teacher)principal).getName();
                return true;
            }catch (ClassCastException ex){
                return false;
            }
        }
    }
}