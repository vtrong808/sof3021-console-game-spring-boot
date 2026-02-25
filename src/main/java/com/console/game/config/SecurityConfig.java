package com.console.game.config;

import org.springframework.beans.factory.annotation.Autowired;
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

        @Autowired
        private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

        @Bean
        public PasswordEncoder passwordEncoder() {
                return org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();
                // return new BCryptPasswordEncoder();
                // nếu đang test mật khẩu plain text thì đổi lại NoOpPasswordEncoder
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

                http.csrf(csrf -> csrf.disable());

                http.authorizeHttpRequests(auth -> auth

                                // PUBLIC
                                .requestMatchers(
                                                "/",
                                                "/home/**",
                                                "/product/**",
                                                "/products/**",
                                                "/account/sign-up",
                                                "/account/forgot-password",
                                                "/account/reset-password",
                                                "/shop/**",
                                                "/auth/**",
                                                "/css/**",
                                                "/js/**",
                                                "/utils/**",
                                                "/images/**",
                                                "/oauth2/**")
                                .permitAll()

                                // CUSTOMER
                                .requestMatchers(
                                                "/cart/**",
                                                "/checkout/**",
                                                "/order/**",
                                                "/account/**")
                                .hasRole("CUSTOMER")

                                // ADMIN
                                .requestMatchers("/admin/**")
                                .hasAnyRole("ADMIN")

                                .anyRequest().authenticated());

                // LOGIN LOCAL
                http.formLogin(form -> form
                                .loginPage("/auth/login")
                                .loginProcessingUrl("/auth/login")
                                .usernameParameter("email")
                                .passwordParameter("password")
                                // Bỏ dòng defaultSuccessUrl và thay bằng successHandler
                                .successHandler((request, response, authentication) -> {
                                        boolean isAdmin = authentication.getAuthorities().stream()
                                                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

                                        if (isAdmin) {
                                                response.sendRedirect("/admin/dashboard"); // Nhảy thẳng vào Admin
                                        } else {
                                                response.sendRedirect("/"); // Khách hàng nhảy vào trang chủ
                                        }
                                })
                                .failureUrl("/auth/login?error=true")
                                .permitAll());

                // LOGIN GOOGLE
                http.oauth2Login(oauth -> oauth
                                .loginPage("/auth/login")
                                .successHandler(oAuth2LoginSuccessHandler));

                // LOGOUT
                http.logout(logout -> logout
                                .logoutUrl("/auth/logout")
                                .logoutSuccessUrl("/auth/login?logout=true")
                                .invalidateHttpSession(true)
                                .deleteCookies("JSESSIONID")
                                .permitAll());

                return http.build();
        }
}