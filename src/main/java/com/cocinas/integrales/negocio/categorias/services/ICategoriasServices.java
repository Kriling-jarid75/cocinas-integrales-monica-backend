package com.cocinas.integrales.negocio.categorias.services;

import java.util.List;

import com.cocinas.integrales.negocio.categorias.model.CategoriasModels;
import com.cocinas.integrales.negocio.productos.model.Productos;

public interface ICategoriasServices {

	List<CategoriasModels> obtenerTodasLasCategorias();

	List<Productos> getProductosPorCategoria(String categoria);

	String agregarCategorias(CategoriasModels req);

	String editarCategorias(CategoriasModels req);

	boolean eliminarCategorias(CategoriasModels req);

	CategoriasModels obtenerCategoriaPorId(Long idCategoria);

}
