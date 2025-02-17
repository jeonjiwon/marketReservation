package com.example.marketReservation.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter authenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(httpBasicConfigurer -> httpBasicConfigurer.disable())  // HTTP 기본 인증 비활성화
                .csrf(csrfConfigurer -> csrfConfigurer.disable())  // CSRF 보호 비활성화
                .sessionManagement(sessionManagementConfigurer ->
                        sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // 세션 정책을 STATELESS로 설정
                )
                .authorizeHttpRequests(authorizeRequestsConfigurer ->
                        authorizeRequestsConfigurer
                                .requestMatchers(
                                        "/auth/signup", "/auth/signin"
                                        , "/read/market", "/read/marketDetail"
                                        , "/update/arrivedCheck"
//                                        ,"/create/market","/update/market", "/delete/market",
//                                        "/read/reservation", "/create/reservation", "/cancel/reservation",
//                                        "/create/review", "/update/review", "/delete/review"
                                ).permitAll()
//                                .requestMatchers("/**/signup", "/**/signin").permitAll()  // 회원가입 및 로그인시 인증 없이 접근 허용
                                .anyRequest().authenticated()  // 다른 모든 요청은 인증 필요
                )
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);  // JWT 인증 필터 추가

        return http.build();
    }
}
