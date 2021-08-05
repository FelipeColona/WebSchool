package br.com.webschool.api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
public class WebSecurityConfig {
    
    //Admin Config
    @Configuration
    @Order(1)
    public static class AdminWebSecurityAdapter extends WebSecurityConfigurerAdapter {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/admin/**").authorizeRequests().antMatchers("/admin/**").hasRole("ADMIN")
                    .and().formLogin().loginPage("/admin/login").permitAll().and().logout().logoutUrl("/admin/logout")
                    .and().exceptionHandling().accessDeniedPage("/403");

            //http.httpBasic();
        }

        @Autowired
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.inMemoryAuthentication().withUser("admin").password("{noop}123").roles("ADMIN");
        }
    }

    //Teacher Config
    @Configuration
    @Order(2)
    public static class TeacherWebSecurityAdapter extends WebSecurityConfigurerAdapter {
        @Autowired
        private ImplementsTeacherDetailsService teacherDetailsService;

        @Autowired
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(teacherDetailsService).passwordEncoder(new BCryptPasswordEncoder());
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/teachers/**").authorizeRequests().antMatchers("/teachers/**").authenticated()
                    .and().formLogin().loginPage("/teachers/login")
                    .permitAll().and().logout().logoutUrl("/teachers/logout")
                    .and().exceptionHandling().accessDeniedPage("/403");
            
            //http.httpBasic();
        }

        @Override
        public void configure(WebSecurity web) throws Exception {
            web.ignoring().antMatchers("/templates/**, /materialize/**", "/style/**");
        }

    }

        //Student Config
        @Configuration
        @Order(3)
        public static class StudentWebSecurityAdapter extends WebSecurityConfigurerAdapter {
            @Autowired
            private ImplementsStudentDetailsService studentDetailsService;
    
            @Autowired
            protected void configure(AuthenticationManagerBuilder auth) throws Exception {
                auth.userDetailsService(studentDetailsService).passwordEncoder(new BCryptPasswordEncoder());
            }

            @Override
            protected void configure(HttpSecurity http) throws Exception {
                http.antMatcher("/students/**").authorizeRequests().antMatchers("/students/**").authenticated()
                        .and().formLogin().loginPage("/students/login")
                        .permitAll().and().logout().logoutUrl("/students/logout")
                        .and().exceptionHandling().accessDeniedPage("/403");
                
                //http.httpBasic();
            }
    
            @Override
            public void configure(WebSecurity web) throws Exception {
                web.ignoring().antMatchers("/templates/**, /materialize/**", "/style/**");
            }
    
        }

}
