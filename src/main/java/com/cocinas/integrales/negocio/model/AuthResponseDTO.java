package com.cocinas.integrales.negocio.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponseDTO {
    private String token;
    private String administrador;
    private String rol;

  
}
