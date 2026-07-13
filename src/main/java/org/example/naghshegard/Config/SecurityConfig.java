package org.example.naghshegard.Config;

import org.example.naghshegard.Jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // صفحات عمومی
                        .requestMatchers("/", "/login", "/register", "/places", "/travelogues").permitAll()
                        .requestMatchers("/place/**", "/travelogue/**").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/static/**", "/favicon.ico").permitAll()

                        // APIهای عمومی
                        .requestMatchers("/api/auth/**", "/api/places/public/**", "/api/places/photos/**").permitAll()
                        .requestMatchers("/api/travelogues/public/**", "/api/travelogues/photos/**").permitAll()
                        .requestMatchers("/api/reviews/place/**").permitAll()

                        // پنل ادمین
                        .requestMatchers("/admin-panel", "/api/admin/**").hasAnyRole("ADMIN", "MODERATOR")
                        // صفحات نیازمند لاگین
                        .requestMatchers("/add-place", "/my-places", "/create-travelogue", "/my-travelogues").authenticated()
                        .requestMatchers("/api/places/add", "/api/places/my-places", "/api/places/*/like").authenticated()
                        .requestMatchers("/api/reviews/add").authenticated()
                        .requestMatchers("/api/travelogues/add", "/api/travelogues/my-travelogues", "/api/travelogues/*/like").authenticated()
                        .requestMatchers("/api/places/my-places").authenticated()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}