package de.jdtbee.dev.config;

import static org.springframework.boot.security.autoconfigure.web.servlet.PathRequest.toH2Console;
import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ProjectSecurityConfig {

	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(requests -> requests
				.requestMatchers(toH2Console()).permitAll()
				.anyRequest().authenticated())
			// the H2 console posts its login form without a CSRF token
			.csrf(csrf -> csrf.ignoringRequestMatchers(toH2Console()))
			// the H2 console renders inside frames from the same origin
			.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
			.formLogin(withDefaults())
			.httpBasic(withDefaults());
		return http.build();
	}

}
