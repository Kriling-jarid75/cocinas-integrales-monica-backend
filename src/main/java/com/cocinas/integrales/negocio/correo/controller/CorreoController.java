package com.cocinas.integrales.negocio.correo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cocinas.integrales.negocio.correo.model.CorreoRequest;
import com.cocinas.integrales.negocio.correo.service.impl.CorreoServiceImpl;
import com.cocinas.integrales.negocio.model.GenericResponse;

@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
@RestController
@RequestMapping("/api/gestion")
public class CorreoController {
 
    
    private final CorreoServiceImpl correoService;
    
    public CorreoController(CorreoServiceImpl correoService) {
		this.correoService = correoService;
	}
    
    
 
    @PostMapping("/enviar/email")
    public ResponseEntity<GenericResponse<String>> enviarCorreo(@RequestBody CorreoRequest request) {
        GenericResponse<String> respuesta = new GenericResponse<>();
        try {
           // correoService.enviarCorreo(request);
        	
        	
        	System.out.println("Mostramos lo enviado desde el correo " + "--" + request.toString());
        	
        	
            respuesta.setCode(HttpStatus.OK.value());
            respuesta.setMessage("Correo enviado correctamente");
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            respuesta.setCode(HttpStatus.BAD_REQUEST.value());
            respuesta.setMessage("Error al enviar correo: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
        }
    }
}
