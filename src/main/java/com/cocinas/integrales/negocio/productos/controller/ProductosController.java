package com.cocinas.integrales.negocio.productos.controller;


import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cocinas.integrales.negocio.cloudinary.service.impl.CloudinaryService;
import com.cocinas.integrales.negocio.model.GenericResponse;
import com.cocinas.integrales.negocio.productos.model.Imagenes;
import com.cocinas.integrales.negocio.productos.model.Productos;
import com.cocinas.integrales.negocio.productos.service.impl.ServicioProductos;


@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
@RestController
@RequestMapping("/api/gestion")
public class ProductosController {

	private static final Logger LOG = LoggerFactory.getLogger(ProductosController.class);

	private final ServicioProductos serviceProductos;
	private final CloudinaryService serviceCloudinary;

	public ProductosController(ServicioProductos serviceProductos,
			CloudinaryService serviceCloudinary) {
		this.serviceProductos = serviceProductos;
		this.serviceCloudinary = serviceCloudinary;
	}

	  @PostMapping("/productos/listar")
	    public ResponseEntity<GenericResponse<List<Productos>>> obtenerProductos() {

	        GenericResponse<List<Productos>> respuesta = new GenericResponse<>();

	        try {
	            // Llama al servicio para obtener los datos
	            List<Productos> todosLosProductos = serviceProductos.getProductos();
	            
	            // *** Verificar si la lista está vacía ***
	            if (!todosLosProductos.isEmpty()) {
	                // Caso 1: La lista tiene elementos (OK 200)
	                respuesta.setCode(HttpStatus.OK.value());
	                respuesta.setMessage("Operación exitosa. Se encontraron " + todosLosProductos.size() + " productos.");
	                respuesta.setData(todosLosProductos);
	                
	                // Devuelve la respuesta exitosa inmediatamente
	                return ResponseEntity.ok(respuesta); 

	            } else {
	                // Caso 2: La lista está vacía (NOT FOUND 404 o OK 200 con mensaje)
	                // Usar 404 (Not Found) es semánticamente correcto si esperas encontrar algo.
	                respuesta.setCode(HttpStatus.NOT_FOUND.value());
	                respuesta.setMessage("No se encontraron productos disponibles en la base de datos.");
	                respuesta.setData(todosLosProductos); // Devuelve la lista vacía si lo prefieres

	                // Devuelve la respuesta con el estado HTTP 404
	                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
	            }

	        } catch (Exception e) {
	            // Caso 3: Ocurrió un error inesperado (BAD REQUEST 400 o INTERNAL SERVER ERROR 500)
	            e.printStackTrace();

	            respuesta.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value()); // Mejor usar 500 para errores de servidor
	            respuesta.setMessage("Ocurrió un error interno del servidor: " + e.getMessage());
	            respuesta.setData(null);

	            // Devuelve la respuesta con el estado HTTP 500
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
	        }
	    }
	



	@PostMapping("/productos/registro")
	public ResponseEntity<GenericResponse<String>> crearProducto(@RequestPart("producto") Productos producto,
			@RequestPart("imagenes") List<MultipartFile> imagenes) {

		GenericResponse<String> respuesta = new GenericResponse<>();

		List<Imagenes> listaImagenes = new ArrayList<>();
		try {
			System.out.println("🟢 Producto recibido: " + producto.getNombre());
			System.out.println("📸 Cantidad de imágenes: " + imagenes.size());

			// Recorrer archivos y subirlos a Cloudinary (aún no implementado)
			// Por ahora solo imprimimos
			 for (MultipartFile imagen : imagenes) {
		            System.out.println("➡️ Imagen: " + imagen.getOriginalFilename());

		            // Subimos la imagen a Cloudinary
		            String url = serviceCloudinary.subirImagen(imagen);

		            Imagenes img = new Imagenes();
		            img.setNombreImagen(imagen.getOriginalFilename());
		            img.setUrlImagen(url); // ahora sí tiene URL real
		            listaImagenes.add(img);
		        }

			producto.setImagen(listaImagenes);

			serviceProductos.agregarProductos(producto);

			// Guardar producto (ya con sus datos)
			// productoRepository.save(producto);
			respuesta.setCode(HttpStatus.OK.value());

			respuesta.setMessage("Operación exitosa");
			return ResponseEntity.ok(respuesta); // Retorna 200
		} catch (Exception e) {
			e.printStackTrace();
			respuesta.setCode(HttpStatus.BAD_REQUEST.value());
			respuesta.setMessage("Ocurrió un error: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
		}
	}

	

//	@PostMapping("/productos/editar")
//	public ResponseEntity<GenericResponse<String>> editarProductos(@RequestBody Productos productoEditado) {
//		GenericResponse<String> respuesta = new GenericResponse<>();
//
//		try {
//			boolean actualizado = serviceProductos.editarProducto(productoEditado);
//
//			if (actualizado) {
//				respuesta.setCode(HttpStatus.OK.value());
//				respuesta.setMessage("Producto editado correctamente");
//				return ResponseEntity.ok(respuesta);
//			} else {
//				respuesta.setCode(HttpStatus.NOT_FOUND.value());
//				respuesta.setMessage("No se encontró el producto con el ID especificado");
//				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			respuesta.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
//			respuesta.setMessage("Error al editar el producto: " + e.getMessage());
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
//		}
//	}

	@PostMapping("/productos/eliminar")
	public ResponseEntity<GenericResponse<String>> eliminarProductos(
			@RequestBody Productos req) {
		GenericResponse<String> respuesta = new GenericResponse<>();

		try {
			boolean eliminado = serviceProductos.eliminarProductos(req);

			if (eliminado) {
				respuesta.setCode(HttpStatus.OK.value());
				respuesta.setMessage("Producto eliminado correctamente");
				return ResponseEntity.ok(respuesta);
			} else {
				respuesta.setCode(HttpStatus.NOT_FOUND.value());
				respuesta.setMessage("No se encontró el producto con el ID especificado");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
			}

		} catch (Exception e) {
			e.printStackTrace();
			respuesta.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			respuesta.setMessage("Error al editar el producto: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
		}
	}
	
	

}
