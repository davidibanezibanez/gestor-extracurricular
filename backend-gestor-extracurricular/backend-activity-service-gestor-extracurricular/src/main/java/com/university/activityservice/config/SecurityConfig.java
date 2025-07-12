package com.university.activityservice.config;

import com.university.activityservice.filter.JwtTokenValidator;
import com.university.activityservice.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtUtils jwtUtils;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(http -> {
                    // ACTIVITY ENDPOINTS
                    http.requestMatchers(HttpMethod.GET, "/activities/**").hasAuthority("READ_ACTIVITY");
                    http.requestMatchers(HttpMethod.POST, "/activities").hasAuthority("CREATE_ACTIVITY");
                    http.requestMatchers(HttpMethod.PUT, "/activities/**").hasAuthority("UPDATE_ACTIVITY");
                    http.requestMatchers(HttpMethod.DELETE, "/activities/**").hasAuthority("DELETE_ACTIVITY");
                    http.requestMatchers(HttpMethod.PATCH, "/activities/*/enable").hasAuthority("ENABLE_ACTIVITY");
                    http.requestMatchers(HttpMethod.PATCH, "/activities/*/disable").hasAuthority("DISABLE_ACTIVITY");
                    // ENROLLMENT ENDPOINTS
                    http.requestMatchers(HttpMethod.GET, "/enrollments/student/**").hasAuthority("READ_ACTIVITY");
                    http.requestMatchers(HttpMethod.GET, "/enrollments/activity/*/check/**").hasAuthority("READ_ACTIVITY");
                    http.requestMatchers(HttpMethod.GET, "/enrollments/activity/**").hasAuthority("READ_ACTIVITY");
                    http.requestMatchers(HttpMethod.POST, "/enrollments/activity/**").hasAuthority("ENROLL_ACTIVITY");
                    http.requestMatchers(HttpMethod.DELETE, "/enrollments/activity/**").hasAuthority("UNENROLL_ACTIVITY");

                    http.anyRequest().authenticated();
                })
                .addFilterBefore(new JwtTokenValidator(jwtUtils), BasicAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
