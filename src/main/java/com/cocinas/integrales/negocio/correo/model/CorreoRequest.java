package com.cocinas.integrales.negocio.correo.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CorreoRequest {
	
	private String nombreCliente;
	private String correoCliente;
	private String asuntoCliente;
	private String mensajeCliente;

}
