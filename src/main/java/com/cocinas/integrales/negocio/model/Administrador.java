package com.cocinas.integrales.negocio.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Administrador {
   
    private String username;
    private String password;
    private String rol;
    private String fecha;

   

   
}