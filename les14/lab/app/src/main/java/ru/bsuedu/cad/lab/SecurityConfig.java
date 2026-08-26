package ru.bsuedu.cad.lab;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public InMemoryUserDetailsManager users() {

        UserDetails user = User
                .withUsername("user")
                .password("{noop}user")
                .roles("USER")
                .build();

        UserDetails manager = User
                .withUsername("manager")
                .password("{noop}manager")
                .roles("MANAGER")
                .build();

        return new InMemoryUserDetailsManager(
                user,
                manager
        );
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**")
                )

                .authorizeHttpRequests(auth -> auth

                        // REST API:
                        // чтение доступно обоим пользователям
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/**"
                        ).hasAnyRole("USER", "MANAGER")

                        // изменение REST API только manager
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/**"
                        ).hasRole("MANAGER")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/**"
                        ).hasRole("MANAGER")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/**"
                        ).hasRole("MANAGER")

                        // Web-интерфейс:
                        // просмотр доступен обоим
                        .requestMatchers(
                                HttpMethod.GET,
                                "/orders",
                                "/orders/"
                        ).hasAnyRole("USER", "MANAGER")

                        // остальные операции только manager
                        .requestMatchers(
                                HttpMethod.GET,
                                "/orders/new"
                        ).hasRole("MANAGER")

                        .requestMatchers(
                                HttpMethod.POST,
                                "/orders"
                        ).hasRole("MANAGER")

                        .requestMatchers(
                                HttpMethod.GET,
                                "/orders/*/edit"
                        ).hasRole("MANAGER")

                        .requestMatchers(
                                HttpMethod.POST,
                                "/orders/*"
                        ).hasRole("MANAGER")

                        .requestMatchers(
                                HttpMethod.POST,
                                "/orders/*/delete"
                        ).hasRole("MANAGER")

                        .anyRequest()
                        .authenticated()
                )

                .formLogin(form -> form
                        .defaultSuccessUrl("/orders", true)
                        .permitAll()
                )

                .httpBasic(basic -> {});

        return http.build();
    }
}