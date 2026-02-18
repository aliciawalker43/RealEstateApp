package RealEstateApp.Config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.context.annotation.Bean;


@EnableWebSecurity
public class SecurityConfig {

	
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
            	.requestMatchers("/").permitAll() 
                .requestMatchers("/homelogin").permitAll() 
                .requestMatchers("/landlord/**").hasRole("COMPANY")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/Employee/**").hasRole("EMPLOYEE")
                .requestMatchers("/tenant/**").hasRole("TENANT")
                .anyRequest().authenticated()
            )

            .formLogin(form -> form.loginPage("/homelogin"))
            .oauth2Login(with -> {}); 
        return http.build();
    }
}
