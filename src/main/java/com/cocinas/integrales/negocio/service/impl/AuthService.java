package com.cocinas.integrales.negocio.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cocinas.integrales.exception.AuthenticationException;
import com.cocinas.integrales.negocio.model.AuthResponseDTO;
import com.cocinas.integrales.negocio.model.GenericResponse;
import com.cocinas.integrales.negocio.security.JwtUtil;

@Service
public class AuthService {

    private static final Logger LOG = LoggerFactory.getLogger(AuthService.class);

    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserDetailsService userDetailsService, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<GenericResponse<AuthResponseDTO>> authenticate(String username, String password) {
        GenericResponse<AuthResponseDTO> response = new GenericResponse<>();

        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            boolean passwordValida = passwordEncoder.matches(password, userDetails.getPassword());

            if (!passwordValida) {
                response.setCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("Credenciales inválidas");
                response.setData(null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            // ✅ Generamos el token JWT
            String jwt = jwtUtil.generateToken(userDetails.getUsername());

            // ✅ Obtenemos el rol real
            String rol = userDetails.getAuthorities().iterator().next().getAuthority();

            // ✅ Armamos el objeto de respuesta
            AuthResponseDTO loginResp = new AuthResponseDTO();
            loginResp.setAdministrador(userDetails.getUsername());
            loginResp.setToken(jwt);
            loginResp.setRol(rol); // 👈 ahora sí contiene "ADMIN" o "CLIENTE"
          

            response.setCode(HttpStatus.OK.value());
            response.setMessage("Autenticación exitosa");
            response.setData(loginResp);

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            LOG.error("Credenciales inválidas para el usuario: {}", username);

            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage("Credenciales inválidas");
            response.setData(null);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        } catch (Exception e) {
            LOG.error("Error durante la autenticación: {}", e.getMessage());
            throw new AuthenticationException("Error interno durante la autenticación",
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    public String metodoEncriptarPassword(String passwordObtenidaDeBase) {
        String encodedPassword = passwordEncoder.encode(passwordObtenidaDeBase);
        LOG.info("Contraseña encriptada: {}", encodedPassword);
        return encodedPassword;
    }
}
