package com.cocinas.integrales.negocio.productos.model;

import java.util.ArrayList;
import java.util.List;

import com.cocinas.integrales.negocio.categorias.model.CategoriasModels;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Productos {


	private Long idProducto;
	private String nombre;
	private String descripcion;
	private CategoriasModels categoria; // ⚡ objeto completo
//	private String precio;
//	private String imagenBase64;
	private List<Imagenes> imagen = new ArrayList<>();   // nueva propiedad para la ruta
	

}
