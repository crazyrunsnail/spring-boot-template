package com.github.crazyrunsnail.template.config;


import com.github.crazyrunsnail.template.dto.ApiResponse;
import com.github.crazyrunsnail.template.filter.JwtAuthenticationTokenFilter;
import com.github.crazyrunsnail.template.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.nio.charset.StandardCharsets;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@EnableAutoConfiguration(exclude = {UserDetailsServiceAutoConfiguration.class, SecurityAutoConfiguration.class})
public class SecurityConfig {

    private final JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(CsrfConfigurer::disable)
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(CorsConfigurer::disable)
                .headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(FormLoginConfigurer::disable)
                .logout(LogoutConfigurer::disable)
                .exceptionHandling((config -> {
                    config.accessDeniedHandler(getHandleAccessDeniedHandler());
                    config.authenticationEntryPoint(getAuthenticationEntryPoint());
                }))
                .authorizeHttpRequests((authorizeHttpRequests) ->
                        authorizeHttpRequests
                                .requestMatchers(HttpMethod.OPTIONS).permitAll()
                                .requestMatchers("/api-docs**").permitAll()
                                .requestMatchers("/swagger-ui*/**").permitAll()
                                .requestMatchers("/actuator*/**").permitAll()
                                .requestMatchers("/api/user/login").permitAll()
                                .requestMatchers("/api/**").authenticated()
                                .anyRequest().anonymous()
                ).build();

    }

    private AuthenticationEntryPoint getAuthenticationEntryPoint() {
        return (request, response, e) -> {
            log.info("[commence][访问 URL({}) 时，没有登录]", request.getRequestURI(), e);
            response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");
            // 返回 401
            response.getOutputStream().write(JsonUtils.toString(ApiResponse.fail(10002, "用户暂未登录"))
                    .getBytes(StandardCharsets.UTF_8));
            response.getOutputStream().flush();
        };
    }

    private AccessDeniedHandler getHandleAccessDeniedHandler() {
        return (request, response, e) -> {
            log.warn("[commence][访问 URL({}) 时，用户({}) 权限不够]", request.getRequestURI(),
                    SecurityContextHolder.getContext().getAuthentication() == null ? "匿名" :
                            ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());

            response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");
            response.getOutputStream().write(JsonUtils.toString(ApiResponse.fail(403, "用户无权限访问"))
                    .getBytes(StandardCharsets.UTF_8));

            response.getOutputStream().flush();
        };
    }


}
