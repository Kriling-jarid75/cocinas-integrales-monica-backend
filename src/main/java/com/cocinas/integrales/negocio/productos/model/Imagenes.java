package com.cocinas.integrales.negocio.productos.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Imagenes {
	private Long idImagen;
	private String nombre_imagen;
	private String url_imagen;
	private String public_id;

}
