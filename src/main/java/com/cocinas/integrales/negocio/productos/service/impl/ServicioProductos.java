package com.cocinas.integrales.negocio.productos.service.impl;


import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.cocinas.integrales.negocio.categorias.model.CategoriasModels;
import com.cocinas.integrales.negocio.cloudinary.service.impl.CloudinaryService;
import com.cocinas.integrales.negocio.productos.dao.ProductosDao;
import com.cocinas.integrales.negocio.productos.model.Imagenes;
import com.cocinas.integrales.negocio.productos.model.Productos;
import com.cocinas.integrales.negocio.productos.services.IServicioProductos;


@Service
public class ServicioProductos implements IServicioProductos{

	private static final Logger LOG = LoggerFactory.getLogger(ServicioProductos.class);

	private final ProductosDao vamosAlDao;
	private final CloudinaryService serviceCloudinary;

	public ServicioProductos(ProductosDao vamosAlDao,CloudinaryService serviceCloudinary) {
		this.vamosAlDao = vamosAlDao;
		this.serviceCloudinary = serviceCloudinary;
	}
	


	@Override	
	public List<Productos> getProductos() {

		List<Productos> todosLosProductos = vamosAlDao.consultarTodosLosProductosDao();

		return todosLosProductos;
	}
	
	@Override
	public Productos obtenerProductoPorId(Long idProducto) {
		
		Productos idProd = new Productos();
		
		idProd = vamosAlDao.obtenerIdDeProducto(idProducto);
		
	
		return idProd;
	}
	
	/*public CategoriasModels obtenerCategoriaPorId(Long idCategoria) {
		
		CategoriasModels idCateg = new CategoriasModels();
		
		idCateg = vamosAlDaoDeCategorias.obtenerIdDeCategoria(idCategoria);
		
		
		return idCateg;
	}*/

	
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
	public String actualizarProducto(Productos req) {
		try {
			boolean editado = vamosAlDao.actualizarProductoDao(req);

			if (editado) {
				return "Producto actualizado exitosamente.";
			} else {
				return "No se pudo actualizar el producto. Verifique los datos.";
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.info("Ocurrió un error al actualizar el producto: " + e.getMessage());
			return "Ocurrió un error al actualizar el producto: " + e.getMessage();
		}
	}
	
	@Override	
	public boolean eliminarProductos(Productos req) {
		
		boolean banderaPrincipal = false;
		
		try {
			
			// 2. Eliminar cada imagen en Cloudinary
		    for (Imagenes publicId : req.getImagen()) {
		    	serviceCloudinary.deteleImageCloudinary(publicId.getPublic_id());
		    }
			
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
