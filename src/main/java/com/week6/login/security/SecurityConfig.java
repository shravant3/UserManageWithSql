package com.week6.login.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    public AuthenticationSuccessHandler customhandler;

    @Bean
    public UserDetailsService getuserdtlsService(){
        return new UserDetailsServiceImpl();
    }


    @Bean
    public BCryptPasswordEncoder getpasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider getDaoAuthProvider(){

        DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(getuserdtlsService());
        daoAuthenticationProvider.setPasswordEncoder(getpasswordEncoder());
        return daoAuthenticationProvider;
    }


    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
         http
                 .authorizeHttpRequests((authz) -> authz
                                 .requestMatchers("/admin/**").hasRole("ADMIN")
                                 .requestMatchers("/user/**").hasRole("USER")
                                 .requestMatchers("/**","/register").permitAll()
                                 .anyRequest().authenticated())
                 .formLogin(login -> login.loginPage("/login")
                                .loginProcessingUrl("/login")
                                .successHandler(customhandler))
                                .csrf(csrf -> csrf.disable())
                .logout(logout -> logout
                                .logoutSuccessUrl("/login")
                                // .invalidateHttpSession(true)
                                .deleteCookies("JSESSIONID")
                                .permitAll());

        http 
                .authenticationProvider(getDaoAuthProvider());
            
        return http.build();
    }
}
