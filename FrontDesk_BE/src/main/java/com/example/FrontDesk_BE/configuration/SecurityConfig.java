    package com.example.FrontDesk_BE.configuration;

    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.context.annotation.PropertySource;
    import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.oauth2.jwt.JwtDecoders;
    import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
    import org.springframework.security.web.SecurityFilterChain;

    @Configuration
    @EnableWebSecurity
    @EnableMethodSecurity
    @PropertySource(value={"classpath:application.yml"})
    public class SecurityConfig {


        @Value("${spring.security.oauth2.resource-server.jwt.issuer-uri}")
        private String issuerUri;

        @Bean
        public JwtAuthenticationConverter jwtAuthenticationConverter(){
            JwtAuthenticationConverter converter=new JwtAuthenticationConverter();
            converter.setJwtGrantedAuthoritiesConverter(new CustomAuthorityConverter());
            return converter;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    .authorizeHttpRequests((requests) -> requests
                            .requestMatchers("/api/idcard/**")
                            .hasAnyRole("IDCard.Editor","Visitor.Editor")
                            .requestMatchers("/api/visitorAccess/**")
                            .hasRole("Visitor.Editor")
                            .requestMatchers("/api/**")
                            .authenticated()
                            .anyRequest().permitAll()
                    )
                    .oauth2ResourceServer(oauth2 -> oauth2
                            .jwt(jwt -> {
                                jwt.decoder(JwtDecoders.fromOidcIssuerLocation(issuerUri));
                                jwt.jwtAuthenticationConverter(jwtAuthenticationConverter());
                            })
                    );
            return http.build();
        }
    }