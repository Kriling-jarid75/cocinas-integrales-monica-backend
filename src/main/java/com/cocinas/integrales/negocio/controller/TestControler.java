package com.cocinas.integrales.negocio.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestControler {

	@PostMapping("/secure")
    public ResponseEntity<String> secureEndpoint() {
        return ResponseEntity.ok("✅ Accediste al endpoint seguro con un JWT válido");
    }

    
}

