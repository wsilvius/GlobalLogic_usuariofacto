package com.usuarios.usuariofacto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages = {
		"entrypoints",
		"service",
		"data",
		"helpers",
		"usercase",
		"model",
		"entities"
})
@EnableJpaRepositories(basePackages = "data")
@EntityScan(basePackages = "entities")
@SpringBootApplication
public class UsuariofactoApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsuariofactoApplication.class, args);
	}

}
