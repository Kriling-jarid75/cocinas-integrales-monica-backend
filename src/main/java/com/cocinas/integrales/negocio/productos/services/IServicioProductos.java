package com.cocinas.integrales.negocio.productos.services;


import java.util.List;



import com.cocinas.integrales.negocio.productos.model.Productos;



public interface IServicioProductos {

	List<Productos> getProductos();
	
	Productos obtenerProductoPorId(Long idProducto);

	String agregarProductos(Productos req);

	boolean eliminarProductos(Productos req);

	String actualizarProducto(Productos productoDB);



}
