package com.console.game.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        // SỬA: Khai báo bằng Interface UserDetailsService thay vì class cụ thể
        @Autowired
        private UserDetailsService userDetailsService;

        // Cấu hình mã hóa mật khẩu (Không mã hóa - chỉ dùng cho bài tập/test)
        @SuppressWarnings("deprecation")
        @Bean
        public PasswordEncoder passwordEncoder() {
                return NoOpPasswordEncoder.getInstance();
        }

        // Cấu hình Provider xác thực
        // @Bean
        // public DaoAuthenticationProvider authenticationProvider() {
        // DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        // authProvider.setUserDetailsService(userDetailsService);
        // authProvider.setPasswordEncoder(passwordEncoder());
        // return authProvider;
        // }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(csrf -> csrf.disable())

                                .authorizeHttpRequests(auth -> auth

                                                // 1. PUBLIC – chưa đăng nhập vẫn vào được
                                                .requestMatchers(
                                                                "/",
                                                                "/home/**",
                                                                "/product/**",
                                                                "/products/**",
                                                                "/shop/**",
                                                                "/auth/**",
                                                                "/css/**",
                                                                "/js/**",
                                                                "/images/**")
                                                .permitAll()

                                                // 2. CUSTOMER – phải đăng nhập và có role CUSTOMER
                                                .requestMatchers(
                                                                "/cart/**",
                                                                "/checkout/**",
                                                                "/order/**",
                                                                "/account/**")
                                                .hasRole("CUSTOMER")

                                                // 3. ADMIN / STAFF
                                                .requestMatchers("/admin/**")
                                                .hasAnyRole("ADMIN", "STAFF")

                                                // 4. Bất kỳ request nào khác → cần đăng nhập
                                                .anyRequest().authenticated())

                                .formLogin(form -> form
                                                .loginPage("/auth/login")
                                                .loginProcessingUrl("/auth/login")
                                                .usernameParameter("email")
                                                .passwordParameter("password")
                                                .defaultSuccessUrl("/", true)
                                                .failureUrl("/auth/login?error=true")
                                                .permitAll())

                                .logout(logout -> logout
                                                .logoutUrl("/auth/logout")
                                                .logoutSuccessUrl("/auth/login?logout=true")
                                                .permitAll());

                return http.build();
        }

}