package com.cocinas.integrales.negocio.categorias.service.impl;

import java.util.ArrayList;
import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.cocinas.integrales.negocio.categorias.dao.CategoriasDao;
import com.cocinas.integrales.negocio.categorias.model.CategoriasModels;
import com.cocinas.integrales.negocio.categorias.services.ICategoriasServices;
import com.cocinas.integrales.negocio.productos.model.Productos;


@Service
public class CategoriasServiceImpl implements ICategoriasServices{
	
	private static final Logger LOG = LoggerFactory.getLogger(CategoriasServiceImpl.class);


	private final CategoriasDao vamosAlDaoDeCategorias;

	public final List<CategoriasModels> todosLasCategorias = new ArrayList<>();


	// 🔹 Inyección por constructor (recomendada)
	
	public CategoriasServiceImpl(CategoriasDao vamosAlDaoDeCategorias) {
		this.vamosAlDaoDeCategorias = vamosAlDaoDeCategorias;
	}
	
	@Override
	public CategoriasModels obtenerCategoriaPorId(Long idCategoria) {
		
		CategoriasModels idCateg = new CategoriasModels();
		
		idCateg = vamosAlDaoDeCategorias.obtenerIdDeCategoria(idCategoria);
		
		
		return idCateg;
	}

	
	@Override
	public List<CategoriasModels> obtenerTodasLasCategorias() {

		List<CategoriasModels> listaDeCategorias = new ArrayList<>();

		listaDeCategorias = vamosAlDaoDeCategorias.consultarCategoriasDao();
		return listaDeCategorias;
	}
	
	@Override
	public List<Productos> getProductosPorCategoria(String categoria) {

		List<Productos> listaDeProductosPorCategoria = vamosAlDaoDeCategorias
				.consultarProdutoPorCategoriaDao(categoria);

		return listaDeProductosPorCategoria;
	}

 
	@Override
	public String agregarCategorias(CategoriasModels req) {
		try {
			boolean registrado = vamosAlDaoDeCategorias.registrarCategoriasDao(req);

			if (registrado) {
				return "Categoria registrado exitosamente.";
			} else {
				return "No se pudo registrar la categoria. Verifique los datos.";
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.info("Ocurrió un error al registrar la categoria: " + e.getMessage());
			return "Ocurrió un error al registrar la categoria: " + e.getMessage();
		}
	}
	
	
	@Override
	public String editarCategorias(CategoriasModels req) {
		try {
			boolean editado = vamosAlDaoDeCategorias.actualizarCategoriasDao(req);

			if (editado) {
				return "Categoria actualizada exitosamente.";
			} else {
				return "No se pudo actualizar la categoria. Verifique los datos.";
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.info("Ocurrió un error al actualizar la categoria: " + e.getMessage());
			return "Ocurrió un error al actualizar la categoria: " + e.getMessage();
		}
	}
	
	@Override
	public boolean eliminarCategorias(CategoriasModels req) {

		boolean banderaPrincipal = false;

		try {
			boolean eliminado = vamosAlDaoDeCategorias.eliminarIDCategoriaDao(req);

			if (eliminado) {
				return banderaPrincipal = true;
			} else {
				return banderaPrincipal = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.info("Ocurrió un error al eliminar la categoria: " + e.getMessage());
		}

		return banderaPrincipal;
	}


	

}
