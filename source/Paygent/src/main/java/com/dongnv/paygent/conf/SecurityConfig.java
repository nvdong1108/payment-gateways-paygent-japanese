package com.dongnv.paygent.conf;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig  {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf().disable()
//                .authorizeRequests()
//                .antMatchers("/api/pay").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .httpBasic();

        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/**").permitAll() // Cho phép tất cả request
                .anyRequest().authenticated()
                .and()
                .httpBasic().disable();

        return http.build();
    }

}
