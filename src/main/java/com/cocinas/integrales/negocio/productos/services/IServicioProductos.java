package com.cocinas.integrales.negocio.productos.services;


import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.cocinas.integrales.negocio.productos.model.Productos;



public interface IServicioProductos {

	String registroProductos(Productos productosRequest) throws UsernameNotFoundException;

}
