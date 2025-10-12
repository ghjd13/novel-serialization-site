package com.novelplatform.novelsite.config;

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
        // BCrypt는 가장 널리 사용되는 비밀번호 해시 알고리즘입니다 [2].
        return new BCryptPasswordEncoder();
    }

    /**
     * 2. & 3. HTTP 보안 설정 및 로그인 처리 경로 설정
     * HTTP 요청에 대한 접근 권한과 폼 로그인 처리 방식을 정의합니다 [3, 4].
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. HTTP 요청에 대한 접근 권한 설정 [3]
                .authorizeHttpRequests((requests) -> requests
                        // 정적 리소스(css, js), 회원 가입, 메인 페이지는 누구나 접근 허용 (permitAll) [3]
                        .requestMatchers("/css/**", "/js/**", "/members/new", "/").permitAll()
                        // 이외의 모든 요청은 인증(로그인)된 사용자에게만 허용 (authenticated) [3]
                        .anyRequest().authenticated()
                )
                // 2. 폼 로그인 설정 (Custom Login Page) [4]
                .formLogin((form) -> form
                        .loginPage("/login")        // 사용자 정의 로그인 페이지 URL (GET 요청) [4]
                        .loginProcessingUrl("/login") // 로그인 폼 제출(POST)을 처리할 URL (생략 시 loginPage와 동일)
                        .defaultSuccessUrl("/", true)// 로그인 성공 후 리다이렉트될 기본 URL [4]
                        .permitAll()                // 로그인 페이지는 누구나 접근 허용 [4]
                )
                // 3. 로그아웃 설정 [4]
                .logout((logout) -> logout
                        .logoutUrl("/logout")       // 로그아웃을 처리할 URL (기본 POST) [4]
                        .logoutSuccessUrl("/")      // 로그아웃 성공 후 리다이렉트될 URL [4]
                        .permitAll()
                );

        // CSRF 보호는 기본적으로 활성화되어 있으며, 이를 명시적으로 해제하지 않습니다 (보안 원칙).
        return http.build();
    }
}
