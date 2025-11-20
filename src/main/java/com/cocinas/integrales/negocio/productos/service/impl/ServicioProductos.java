package com.cocinas.integrales.negocio.productos.service.impl;


import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.cocinas.integrales.negocio.productos.dao.ProductosDao;

import com.cocinas.integrales.negocio.productos.model.Productos;
import com.cocinas.integrales.negocio.productos.services.IServicioProductos;


@Service
public class ServicioProductos implements IServicioProductos{

	private static final Logger LOG = LoggerFactory.getLogger(ServicioProductos.class);

	private final ProductosDao vamosAlDao;

	public ServicioProductos(ProductosDao vamosAlDao) {
		this.vamosAlDao = vamosAlDao;
	}
	


	@Override	
	public List<Productos> getProductos() {

		List<Productos> todosLosProductos = vamosAlDao.consultarTodosLosProductosDao();

		return todosLosProductos;
	}
	
	@Override
	public String agregarProductos(Productos req) {
		try {
			boolean registrado = vamosAlDao.registrarProductoDao(req);

			if (registrado) {
				return "Producto registrado exitosamente.";
			} else {
				return "No se pudo registrar el producto. Verifique los datos.";
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.info("Ocurrió un error al eliminado el producto: " + e.getMessage()); 
			return "Ocurrió un error al registrar el producto: " + e.getMessage();
		}
	}
	
	
	/* METODO DE EDITAR */

	
	@Override	
	public boolean eliminarProductos(Productos req) {
		
		boolean banderaPrincipal = false;
		
		try {
			boolean eliminado = vamosAlDao.eliminarIDProductoDao(req);

			if (eliminado) {
				return banderaPrincipal = true;
			} else {
				return banderaPrincipal = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.info("Ocurrió un error al eliminado el producto: " + e.getMessage()); 
		}
		
		return banderaPrincipal;
	}

}
