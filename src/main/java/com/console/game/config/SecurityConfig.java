package com.console.game.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Tắt CSRF tạm thời để dễ test POST
            .authorizeHttpRequests(auth -> auth
                // Cho phép truy cập tự do các trang này:
                .requestMatchers("/", "/home/**", "/product/**", "/auth/**", "/account/**", "/css/**", "/js/**", "/images/**").permitAll()
                // Các trang còn lại (Giỏ hàng, Đơn hàng) cần đăng nhập:
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/auth/login") // Trang login custom của bạn
                .loginProcessingUrl("/auth/login") // URL post form login
                .defaultSuccessUrl("/", true)
                .failureUrl("/auth/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/auth/logout")
                .logoutSuccessUrl("/auth/login?logout=true")
                .permitAll()
            );

        return http.build();
    }
}