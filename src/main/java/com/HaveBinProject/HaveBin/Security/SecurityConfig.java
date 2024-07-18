package com.HaveBinProject.HaveBin.Security;

import com.HaveBinProject.HaveBin.Jwt.JWTUtil;
import com.HaveBinProject.HaveBin.Jwt.JWTFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    //AuthenticationManager가 인자로 받을 AuthenticationConfiguraion 객체 생성자 주입
    private final AuthenticationConfiguration authenticationConfiguration;
    //JWTUtil 주입
    private final JWTUtil jwtUtil;

    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWTUtil jwtUtil) {

        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
    }

    //AuthenticationManager Bean 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }

    // 시큐리티 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable) //csrf 공격 보안

                .httpBasic(AbstractHttpConfigurer::disable) // http basic auth 인증기반 머시기인듯

                .formLogin(AbstractHttpConfigurer::disable) // 이 친구도 마찬가지인

                // 인증,인가가 필요한 URL 지정
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("*").permitAll()
                        //.requestMatchers("/admin").hasRole("ADMIN")
                        //.requestMatchers("/adminPage").hasRole("ADMIN")
                        .anyRequest().authenticated())

                .logout((logout) -> logout
                        .invalidateHttpSession(true))

                //JWTFilter 등록
                .addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class)

                // 필터 추가 LoginFilter()는 인자를 받음 (AuthenticationManager()
                // 메소드에 authenticationConfiguration 객체를 넣어야 함) 따라서 등록 필요
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil), UsernamePasswordAuthenticationFilter.class)

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        return http.build();
    }
}
