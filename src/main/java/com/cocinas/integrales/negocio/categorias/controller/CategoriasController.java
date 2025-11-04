package com.cocinas.integrales.negocio.categorias.controller;

import java.util.List;

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
	
	private CategoriasServiceImpl serviceCategorias;
	
	public CategoriasController(CategoriasServiceImpl serviceCategirias){
		this.serviceCategorias = serviceCategirias;
		
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

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
		}

	}
	
	/*LISTAMOS LAS CATEGORIAS*/
	@PostMapping("/listar/categorias")
	public ResponseEntity<GenericResponse<List<CategoriasModels>>> obtenerCategorias() {

	    GenericResponse<List<CategoriasModels>> respuesta = new GenericResponse<>();

	    try {
	     
	        
	       // todosLosProductos.clear();
	    	
	    	List<CategoriasModels> todosLosProductos = serviceCategorias.obtenerTodasLasCategorias();
	       

	        // Armas la respuesta
	        respuesta.setCode(HttpStatus.OK.value());
	        respuesta.setMessage("Operación exitosa");
	        respuesta.setData(todosLosProductos);

	        return ResponseEntity.ok(respuesta);

	    } catch (Exception e) {
	        e.printStackTrace();

	        respuesta.setCode(HttpStatus.BAD_REQUEST.value());
	        respuesta.setMessage("Ocurrió un error: " + e.getMessage());
	        respuesta.setData(null);

	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
	    }
	}
	
	
	/*BUSCAMOS UN PRODUCTO POR CATEGORIA*/
	@PostMapping("/categoria/{categoria}")
	public ResponseEntity<GenericResponse<List<Productos>>> obtenerPorCategoria(
	        @PathVariable String categoria) {

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
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
	    }
	}
}
