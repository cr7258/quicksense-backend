package pro.quicksense.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // TODO: for convenience, temporarily disable CSRF protection and permit all requests
        http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(authz -> authz.anyRequest().permitAll());
        return http.build();
    }
}