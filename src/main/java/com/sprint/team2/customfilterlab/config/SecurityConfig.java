package com.sprint.team2.customfilterlab.config;

import filter.AuditFilter;
import filter.PerformanceMonitoringFilter;
import filter.RequestLoggingFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain defaultChain(HttpSecurity http) throws Exception {
        http
                // 커스텀 필터 체인에 삽입
                .addFilterBefore(new RequestLoggingFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new AuditFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new PerformanceMonitoringFilter(), UsernamePasswordAuthenticationFilter.class)
                // 간단한 인증 정책(데모용)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/public/**").permitAll()
                        .requestMatchers("/actuator/health", "/actuator/info").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/secure/**").authenticated()
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults()) // 테스트 편의를 위해 Http Basic 활성화
                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}
