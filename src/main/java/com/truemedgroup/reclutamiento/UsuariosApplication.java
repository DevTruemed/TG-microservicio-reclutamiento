package com.truemedgroup.reclutamiento;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EntityScan({"com.truemedgroup.commonsRecruit.postulaciones",
			 "com.truemedgroup.commonsRecruit.usuario",
			 "com.truemedgroup.commonsRecruit.usuarios",
			 "com.truemedgroup.commonsRecruit.pruebas",
			 "com.truemedgroup.reclutamiento.models.pruebas",
			 "com.truemedgroup.reclutamiento.models.address"})
public class UsuariosApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsuariosApplication.class, args);

	}
}
