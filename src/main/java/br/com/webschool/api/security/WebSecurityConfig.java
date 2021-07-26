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

    @Autowired
    private ImplementsUserDetailsService userDetailsService;

    //Global Config
    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder()).and()
                .inMemoryAuthentication().withUser("admin").password("{noop}123").roles("ADMIN");
    }
    
    //Admin Config
    @Configuration
    @Order(1)
    public static class AdminWebSecurityAdapter extends WebSecurityConfigurerAdapter {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/admin/**").authorizeRequests().antMatchers("/admin/**").hasRole("ADMIN")
                    .and().formLogin().loginPage("/admin/login").permitAll().and().logout().logoutUrl("/admin/logout");
        }
    }

    //Teacher Config
    @Configuration
    public static class TeacherWebSecurityAdapter extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests().antMatchers("/teachers/**").authenticated()
                    .and().formLogin().loginPage("/teachers/login")
                    .permitAll().and().logout().logoutUrl("/teachers/logout");
        }

        @Override
        public void configure(WebSecurity web) throws Exception {
            web.ignoring().antMatchers("/templates/**, /materialize/**", "/style/**");
        }

    }

}
