package com.cocinas.integrales.negocio.correo.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CorreoRequest {
	
	private String nombreCliente;
	private String correo_telefono_Cliente;
	private AsuntoCorreo asuntoCliente;
	private String mensajeCliente;

}
