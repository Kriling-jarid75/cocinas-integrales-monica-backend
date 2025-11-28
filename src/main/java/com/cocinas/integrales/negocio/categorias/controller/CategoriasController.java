package com.cocinas.integrales.negocio.categorias.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cocinas.integrales.negocio.categorias.model.CategoriasModels;
import com.cocinas.integrales.negocio.categorias.service.impl.CategoriasServiceImpl;
import com.cocinas.integrales.negocio.model.GenericResponse;
import com.cocinas.integrales.negocio.productos.model.Productos;


@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
@RestController
@RequestMapping("/api/gestion")
public class CategoriasController {
	
	private static final Logger LOG = LoggerFactory.getLogger(CategoriasController.class);
	
	private final CategoriasServiceImpl serviceCategorias;
	
	public CategoriasController(CategoriasServiceImpl serviceCategirias){
		this.serviceCategorias = serviceCategirias;
		
	}

	
	/* LISTAMOS LAS CATEGORIAS */
	@PostMapping("/listar/categorias")
	public ResponseEntity<GenericResponse<List<CategoriasModels>>> obtenerCategorias() {

		GenericResponse<List<CategoriasModels>> respuesta = new GenericResponse<>();

		try {

			// todosLosProductos.clear();

			List<CategoriasModels> todosLasCategorias = serviceCategorias.obtenerTodasLasCategorias();

			// *** Verificar si la lista está vacía ***
			if (!todosLasCategorias.isEmpty()) {
				// Caso 1: La lista tiene elementos (OK 200)
				respuesta.setCode(HttpStatus.OK.value());
				respuesta.setMessage("Operación exitosa. Se encontraron " + todosLasCategorias.size() + " productos.");
				respuesta.setData(todosLasCategorias);

				// Devuelve la respuesta exitosa inmediatamente
				return ResponseEntity.ok(respuesta);

			} else {
				// Caso 2: La lista está vacía (NOT FOUND 404 o OK 200 con mensaje)
				// Usar 404 (Not Found) es semánticamente correcto si esperas encontrar algo.
				respuesta.setCode(HttpStatus.NO_CONTENT.value()); // 204
                respuesta.setMessage("No hay productos registrados.");
                respuesta.setData(new ArrayList<>());

                //
                return ResponseEntity.ok(respuesta);
			}

		} catch (Exception e) {
			// Caso 3: Ocurrió un error inesperado (BAD REQUEST 400 o INTERNAL SERVER ERROR
			// 500)
			e.printStackTrace();

			respuesta.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value()); // Mejor usar 500 para errores de servidor
			respuesta.setMessage("Ocurrió un error interno del servidor: " + e.getMessage());
			respuesta.setData(null);
			 LOG.info("Error" + respuesta.getMessage());
			// Devuelve la respuesta con el estado HTTP 500
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);

		}
	}
	

	
	/* BUSCAMOS UN PRODUCTO POR CATEGORIA */
	@PostMapping("/categoria/{categoria}")
	public ResponseEntity<GenericResponse<List<Productos>>> obtenerPorCategoria(@PathVariable String categoria) {

		GenericResponse<List<Productos>> respuesta = new GenericResponse<>();

		try {
			// Filtra productos por categoría, ignorando mayúsculas, espacios y tildes
			List<Productos> productoFiltrado = serviceCategorias.getProductosPorCategoria(categoria);

			respuesta.setCode(HttpStatus.OK.value());

			if (productoFiltrado.isEmpty()) {
				respuesta.setMessage("No hay productos disponibles en la categoría '" + categoria + "'");
			} else {
				respuesta.setMessage("Operación exitosa");
			}

			respuesta.setData(productoFiltrado);

			return ResponseEntity.ok(respuesta);

		} catch (Exception e) {
			e.printStackTrace();
			respuesta.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			respuesta.setMessage("Ocurrió un error: " + e.getMessage());
			respuesta.setData(null);
			 LOG.info("Error" + respuesta.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
		}
	}
	
	
	
	
	/*REGISTRAMOS LAS CATEGORIAS*/
	@PostMapping("/categoria/registro")
	public ResponseEntity<GenericResponse<String>> accountLAM(@RequestBody CategoriasModels req) {

		GenericResponse<String> respuesta = new GenericResponse<>();

		try {
			// Aquí llamas a tu servicio
			serviceCategorias.agregarCategorias(req);

		
			respuesta.setCode(HttpStatus.OK.value());
			respuesta.setMessage("Operación exitosa");

			return ResponseEntity.ok(respuesta); // Retorna 200
		} catch (Exception e) {
			e.printStackTrace();

			// Respuesta de error

			respuesta.setCode(HttpStatus.BAD_REQUEST.value());
			respuesta.setMessage("Ocurrió un error: " + e.getMessage());
			 LOG.info("Error" + respuesta.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
		}

	}
	
	
	/*EDITAMOS LAS CATEGORIAS*/
	@PostMapping("/categoria/editar")
	public ResponseEntity<GenericResponse<String>> editarCategorias(@RequestBody CategoriasModels req) {

		GenericResponse<String> respuesta = new GenericResponse<>();

		try {
			// Aquí llamas a tu servicio
			serviceCategorias.editarCategorias(req);

		
			respuesta.setCode(HttpStatus.OK.value());
			respuesta.setMessage("Operación exitosa");

			return ResponseEntity.ok(respuesta); // Retorna 200
		} catch (Exception e) {
			e.printStackTrace();

			// Respuesta de error

			respuesta.setCode(HttpStatus.BAD_REQUEST.value());
			respuesta.setMessage("Ocurrió un error: " + e.getMessage());
			 LOG.info("Error" + respuesta.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
		}

	}
	
	@PostMapping("/categoria/eliminar")
	public ResponseEntity<GenericResponse<String>> eliminarCategoria(
			@RequestBody CategoriasModels req) {
		GenericResponse<String> respuesta = new GenericResponse<>();

		try {
			boolean eliminado = serviceCategorias.eliminarCategorias(req);

			if (eliminado) {
				respuesta.setCode(HttpStatus.OK.value());
				respuesta.setMessage("Categoria eliminado correctamente");
				return ResponseEntity.ok(respuesta);
			} else {
				respuesta.setCode(HttpStatus.NOT_FOUND.value());
				respuesta.setMessage("No se encontró la categoria con el ID especificado");
				 LOG.info("Error" + respuesta.getMessage());
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
			}

		} catch (Exception e) {
			e.printStackTrace();
			respuesta.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			respuesta.setMessage("Error al eliminar las categorias: " + e.getMessage());
			 LOG.info("Error" + respuesta.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
		}
	}
	
	
	@PostMapping("/categoria/eliminar/todas")
	public ResponseEntity<GenericResponse<String>> eliminarTodasCategorias(
			@RequestBody List<CategoriasModels> req) {
		GenericResponse<String> respuesta = new GenericResponse<>();

		try {
			 // Necesitas adaptar tu servicio para que acepte la lista
	        boolean eliminado = serviceCategorias.eliminarCategoriasMasivamente(req); 


			if (eliminado) {
				respuesta.setCode(HttpStatus.OK.value());
				respuesta.setMessage("Categorias eliminadas correctamente");
				return ResponseEntity.ok(respuesta);
			} else {
				 // Este else puede requerir lógica diferente si no se elimino ninguna, o solo algunas
	            respuesta.setCode(HttpStatus.NOT_FOUND.value());
	            respuesta.setMessage("No se encontró al menos una de las categorías a eliminar o hubo un problema.");
	             LOG.info("Error" + respuesta.getMessage());
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
			}

		} catch (Exception e) {
			e.printStackTrace();
			respuesta.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			respuesta.setMessage("Error al eliminar todas las categorias: " + e.getMessage());
			 LOG.info("Error" + respuesta.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
		}
	}
	
}
