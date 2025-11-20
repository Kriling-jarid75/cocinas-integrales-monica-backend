package com.cocinas.integrales.negocio.productos.services;


import java.util.List;



import com.cocinas.integrales.negocio.productos.model.Productos;



public interface IServicioProductos {

	List<Productos> getProductos();

	String agregarProductos(Productos req);

	boolean eliminarProductos(Productos req);

}
