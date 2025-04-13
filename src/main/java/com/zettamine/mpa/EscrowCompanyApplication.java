package com.zettamine.mpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@OpenAPIDefinition(info =@Info(title = "Escrow Microservice", version ="1.0" 
,description = "Escrow Microservie of a Mortage Process accelrator application"))
@EnableAspectJAutoProxy
public class EscrowCompanyApplication {

	public static void main(String[] args) {
		SpringApplication.run(EscrowCompanyApplication.class, args);
	}

}
