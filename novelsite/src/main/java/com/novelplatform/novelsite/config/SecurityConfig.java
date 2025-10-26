package com.novelplatform.novelsite.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer; // frameOptions를 위해 임포트
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.novelplatform.novelsite.security.MemberUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final MemberUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder; // AppConfig에서 주입받음

    public SecurityConfig(MemberUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. H2 콘솔은 CSRF 보호가 필요 없음 (문자열 경로 사용)
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/h2-console/**")
            )
            // 2. H2 콘솔이 iframe에서 표시될 수 있도록 허용
            .headers(headers -> headers
                .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
            )
            // 3. URL 권한 설정
            .authorizeHttpRequests(auth -> auth
                // H2 콘솔, 정적 리소스, 회원가입 등은 누구나 접근 허용 (문자열 경로 사용)
                .requestMatchers(
                    "/",
                    "/members/register",
                    "/css/**",
                    "/js/**",
                    "/images/**",
                    "/uploads/**",
                    "/h2-console/**" // H2 콘솔 경로 허용
                ).permitAll()
                .requestMatchers(HttpMethod.GET, "/novels").permitAll()
                .anyRequest().authenticated()) // 그 외 모든 요청은 인증 필요
            .formLogin(form -> form
                .loginPage("/login")
                .permitAll())
            .logout(Customizer.withDefaults())
            .authenticationProvider(authenticationProvider());

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder); // 주입받은 인코더 사용
        return provider;
    }
}

