package ro.tuc.ds2020.websocket;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()  // Enable CORS first
                .and()
                .csrf().disable()  // Disable CSRF
                .authorizeRequests()
                .antMatchers("/ws/**").permitAll()
                .antMatchers("/measurement/from-date").permitAll()
                .antMatchers("/measurement/details").permitAll()
                .antMatchers("/deviceReference/**").permitAll()
                .antMatchers("/measurement/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .cors()  // Enable CORS
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

}
