package com.cocinas.integrales.negocio.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cocinas.integrales.exception.AuthenticationException;
import com.cocinas.integrales.negocio.model.AuthResponseDTO;
import com.cocinas.integrales.negocio.model.GenericResponse;
import com.cocinas.integrales.negocio.model.LoginRequest;
import com.cocinas.integrales.negocio.service.impl.AuthService;

import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<GenericResponse<AuthResponseDTO>> login(
            @RequestBody LoginRequest loginRequest,
            HttpServletResponse responseHttp) {

        LOG.info("Parámetros de entrada - username: {}", loginRequest.getUsername());

        // Evita que la respuesta se guarde en caché
        responseHttp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, private");
        responseHttp.setHeader("Pragma", "no-cache");
        responseHttp.setDateHeader("Expires", 0);

        try {
            // Llamamos al servicio de autenticación
            ResponseEntity<GenericResponse<AuthResponseDTO>> response =
                    authService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());

            // Simplemente devolvemos la respuesta que genera el servicio
            return response;

        } catch (AuthenticationException e) {
            LOG.error("Error en la autenticación del usuario: {}", e.getMessage());

            GenericResponse<AuthResponseDTO> errorResponse = new GenericResponse<>();
            errorResponse.setCode(HttpStatus.BAD_REQUEST.value());
            errorResponse.setMessage("Ocurrió un error: " + e.getMessage());
            errorResponse.setData(null);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

        } catch (Exception e) {
            LOG.error("Error inesperado durante el login: {}", e.getMessage());

            GenericResponse<AuthResponseDTO> errorResponse = new GenericResponse<>();
            errorResponse.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.setMessage("Error interno del servidor");
            errorResponse.setData(null);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
