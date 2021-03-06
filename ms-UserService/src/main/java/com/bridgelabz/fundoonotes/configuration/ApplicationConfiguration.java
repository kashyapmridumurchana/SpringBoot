package com.bridgelabz.fundoonotes.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.bridgelabz.fundoonotes.utility.Validation;

@Configuration
@ComponentScan("com.bridgelabz.fundoonotes")
public class ApplicationConfiguration {

	@Bean
	public BCryptPasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();

	}

	@Bean
	public Validation userValidation() {
		return new Validation();
	}

	@SuppressWarnings("deprecation")
	    @Bean
	    public WebMvcConfigurer corsConfigurer() {
	        return new WebMvcConfigurerAdapter() {
	            @Override
	            public void addCorsMappings(CorsRegistry registry) {
	                registry.addMapping("/**").allowedMethods("GET", "POST", "PUT", "DELETE")
	                        .allowedHeaders("token", "Content-Type").exposedHeaders("token", "Content-Type")
	                        .allowCredentials(false).maxAge(10000);
	            }
	        };
	    }
}
