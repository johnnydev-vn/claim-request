package com.claimrequest.configuration;

import com.claimrequest.Auth.CustomAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    private static final String[] ENABLE_PERMIT = {
            "/login", "/", "/logout",  "assets/**", "/home", "/index",
            "/css/**", "/js/**", "/image/**", "/fonts/**"
    };

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    public void config(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
         /** Phân quyền và xác thực*/
        http.authorizeHttpRequests(auth -> {
                    auth
                            /** Các đường dẫn không yêu cầu xác thực */
                            .requestMatchers(ENABLE_PERMIT).permitAll()

                            /** Chỉ cho phép rank 1 truy cập /admin/** */
                            .requestMatchers("/admin/**").hasAuthority("ROLE_RANK_1")
                            .requestMatchers("/claim/admin/**").hasAuthority("ROLE_RANK_1")
                            .requestMatchers("/claim/**").hasAnyAuthority("ROLE_RANK_3", "ROLE_RANK_2" )
                            .requestMatchers("/approve/**").hasAuthority("ROLE_RANK_2")
                            .requestMatchers("/approve/detailApprove/**").hasAuthority("ROLE_RANK_2")
                            .requestMatchers("/finance/**").hasAuthority("ROLE_RANK_4")
                            .requestMatchers("/fragment/**").hasAnyAuthority("ROLE_RANK_1", "ROLE_RANK_2", "ROLE_RANK_3", "ROLE_RANK_4");
                });

        /** Cấu hình form login */
        http.formLogin(form ->
                form
                .loginPage("/login")
                .loginProcessingUrl("/customLogin")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler(customAuthenticationSuccessHandler)
        );
        http.rememberMe(rememberMe ->
                rememberMe
                        .key("uniqueAndSecret")
                        .tokenValiditySeconds(1 * 24 * 60 * 60)
                        .rememberMeParameter("remember-me")
        );


        /** Cấu hình logout*/
        http.logout(
                logout -> logout.logoutUrl("/logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("remember-me")
                        .logoutSuccessUrl("/home")
        );

        return http.build();
    }

}
