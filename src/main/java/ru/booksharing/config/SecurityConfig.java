package ru.booksharing.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.booksharing.services.PersonDetailsService;

@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
@Configuration
public class SecurityConfig {

    private final PersonDetailsService personDetailsService;

    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(personDetailsService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http    .authorizeHttpRequests()

                .requestMatchers("/admin", "/admin/**", "/works")
                .hasRole("ADMIN")

                .requestMatchers("/authors/new",
                                        "/books/new",
                                        "/genres/new",
                                        "/languages/new",
                                        "/publishing_houses/new",
                                        "/storages/new",
                                        "/translators/new",
                                        "/works/db_manager",
                                        "/works/db_manager/**")
                .hasAnyRole("ADMIN", "DB_MANAGER")

                .requestMatchers("/works/packer", "/works/packer/**")
                .hasAnyRole("ADMIN", "PACKER")

                .requestMatchers("/works/deliveryman", "/works/deliveryman/**")
                .hasAnyRole("ADMIN", "DELIVERYMAN")

                .requestMatchers("/works/my")
                .hasAnyRole("ADMIN", "DB_MANAGER", "PACKER", "DELIVERYMAN")

                .requestMatchers("/cart", "/rentals", "/rentals/checkout")
                .hasAnyRole("ADMIN", "USER", "DB_MANAGER", "PACKER", "DELIVERYMAN")

                .requestMatchers("/",
                        "/authors",
                        "/books",
                        "/books/search",
                        "/genres",
                        "/languages",
                        "/profile",
                        "/profile/**",
                        "/publishing_houses",
                        "/storages",
                        "/translators",
                        "/img/**")
                .permitAll()

                .anyRequest()
                .hasAnyRole("ADMIN", "USER", "DB_MANAGER", "PACKER", "DELIVERYMAN")

                .and()
                .formLogin()
                .loginPage("/profile/login")
                .loginProcessingUrl("/process_login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/profile/login?error")

                .and()
                .logout()
                .logoutUrl("/profile/logout")
                .logoutSuccessUrl("/")
                .deleteCookies("JSESSIONID");
        return http.build();
    }
}
