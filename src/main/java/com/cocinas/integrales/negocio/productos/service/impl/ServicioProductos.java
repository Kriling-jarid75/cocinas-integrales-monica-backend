package com.cocinas.integrales.negocio.productos.service.impl;


import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.cocinas.integrales.negocio.productos.dao.ProductosDao;

import com.cocinas.integrales.negocio.productos.model.Productos;


@Service
public class ServicioProductos {

	private static final Logger LOG = LoggerFactory.getLogger(ServicioProductos.class);

	private final ProductosDao vamosAlDao;

	public ServicioProductos(ProductosDao vamosAlDao) {
		this.vamosAlDao = vamosAlDao;
	}
	private long nextId = 1; // Contador para asignar IDs automáticamente
	private long nextIdImagen = 1; // contador global de imágenes

//	public List<Productos> getProductos() {
//
//		System.out.println("Mostramos los productos " + "\n" + todosLosProductos);
//		return todosLosProductos;
//	}

//	public List<Productos> agregarProductos(Productos req) {
//		if (req.getIdProducto() == null) {
//			req.setIdProducto(nextId++);
//		}
//
//		// Asignar ID único a cada imagen si existen
//		if (req.getImagen() != null) {
//			for (Imagenes img : req.getImagen()) {
//				if (img.getIdImagen() == null) {
//					img.setIdImagen(nextIdImagen++);
//				}
//			}
//		}
//
//		todosLosProductos.add(req);
//		return todosLosProductos;
//	}

	public List<Productos> getProductos() {

		List<Productos> todosLosProductos = vamosAlDao.consultarTodosLosProductosDao();

		return todosLosProductos;
	}
	
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

	
	
//	public boolean editarProducto(Productos productoEditado) {
//		for (int i = 0; i < todosLosProductos.size(); i++) {
//			Productos producto = todosLosProductos.get(i);
//
//			if (producto.getIdProducto().equals(productoEditado.getIdProducto())) {
//				// Reemplaza los campos
//				producto.setNombre(productoEditado.getNombre());
//				producto.setDescripcion(productoEditado.getDescripcion());
//				producto.setPrecio(productoEditado.getPrecio());
//				producto.setCategoria(productoEditado.getCategoria());
//				// producto.setImagen(productoEditado.getImagen()); // actualizar imágenes
//
//				todosLosProductos.set(i, producto); // Actualiza la lista
//				return true;
//			}
//		}
//		return false; // No se encontró el producto
//	}

	// Opcional: eliminar producto
//	public boolean eliminarProducto(Productos productoEliminado) {
//		return todosLosProductos.removeIf(p -> p.getIdProducto().equals(productoEliminado.getIdProducto()));
//	}
	
	
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
