package com.cocinas.integrales.negocio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = "com.cocinas.integrales.negocio")
public class NegocioApplication {

	public static void main(String[] args) {
		SpringApplication.run(NegocioApplication.class, args);
		System.out.println("El backend esta corriendo......");
	}
	
	
	

}
