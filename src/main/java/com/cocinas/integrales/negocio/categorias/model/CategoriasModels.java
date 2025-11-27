package com.cocinas.integrales.negocio.categorias.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CategoriasModels {
	private Long idCategoria;
	private String nombreCategoria;
	private boolean enUso;

}
