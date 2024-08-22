package com.sks.gateway;

import com.sks.gateway.users.OAuthHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan("com.sks")
@EnableWebSecurity
public class GatewayConfig implements WebMvcConfigurer {
    @Value("${app.oauth2.successRedirectUrl}")
    private String oauthSuccessRedirectUrl;

    @Value("${app.oauth2.failRedirectUrl}")
    private String oauthFailureRedirectUrl;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    private final OAuthHandler oAuthHandler;

    public GatewayConfig(OAuthHandler oAuthHandler) {
        this.oAuthHandler = oAuthHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET, "/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/**").authenticated()
                        .anyRequest().permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(oAuthHandler)
                        .failureUrl(oauthFailureRedirectUrl)
                ).logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/users/logout", "GET"))
                        .logoutSuccessUrl(oauthSuccessRedirectUrl)
                        .clearAuthentication(true)
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(frontendUrl)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true);
    }
}
