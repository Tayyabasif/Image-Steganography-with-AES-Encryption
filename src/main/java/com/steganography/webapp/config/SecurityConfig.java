package com.steganography.webapp.config;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private UserDetailsService userDetailsService;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	
				
				http.csrf(customizer -> customizer.disable());
				http.authorizeHttpRequests(request ->{ request.requestMatchers("/login","/register","/signup","/css/**","/js/**","images/**", "/authenticate").permitAll();	
				request.anyRequest().authenticated();
				});
				http.formLogin(formLogin ->{
					formLogin.loginPage("/login");
					formLogin.loginProcessingUrl("/authenticate");
					formLogin.successForwardUrl("/index");
					formLogin.failureUrl("/login?error=true");
					formLogin.usernameParameter("email");
					formLogin.passwordParameter("userPassword");
					
				});
				
				http.logout(logoutForm -> {
					logoutForm.logoutUrl("/logout");
					logoutForm.logoutSuccessUrl("/login?logout=true");
					
				});
				
				http.oauth2Login(oauth ->{
					oauth.loginPage("/login");
				});
				
				http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));
				
				return http.build();
	}
	
//	@Bean
//	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//		http.csrf(customizer -> customizer.disable())
//				.authorizeHttpRequests(request -> request.anyRequest().authenticated())
//				.httpBasic(Customizer.withDefaults())
//				.formLogin(Customizer.withDefaults())
//				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));
//
//		return http.build();
//	}
	
	
	@Bean
	public AuthenticationProvider authProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
		
		return provider;
	}
	
	
}
