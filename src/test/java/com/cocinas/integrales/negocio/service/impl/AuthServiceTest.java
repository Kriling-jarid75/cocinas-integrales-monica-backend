package com.cocinas.integrales.negocio.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
	
	
	
	
	
	@InjectMocks
	private AuthService service;

	@Test
	void testMetodoEncriptarPassword() {
		
		
		// Arrange
		String contra = "123456";

		// Act
		String passwordNueva = service.metodoEncriptarPassword(contra);

		// Assert
		// Validamos que no sea null ni vacío
		assertNotNull(passwordNueva);
		assertFalse(passwordNueva.isEmpty());

		// Validamos que sea distinto al original (porque está encriptado)
		assertNotEquals(contra, passwordNueva);

		// Si usas BCrypt, puedes verificar que haga match:
		assertTrue(new BCryptPasswordEncoder().matches(contra, passwordNueva));
		
	}

}
