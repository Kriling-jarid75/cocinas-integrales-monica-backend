package com.cocinas.integrales.negocio.productos.controller;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

import com.cocinas.integrales.negocio.categorias.model.CategoriasModels;
import com.cocinas.integrales.negocio.categorias.service.impl.CategoriasServiceImpl;
import com.cocinas.integrales.negocio.cloudinary.service.impl.CloudinaryService;
import com.cocinas.integrales.negocio.model.GenericResponse;
import com.cocinas.integrales.negocio.productos.model.Imagenes;
import com.cocinas.integrales.negocio.productos.model.Productos;
import com.cocinas.integrales.negocio.productos.service.impl.ServicioProductos;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
@RestController
@RequestMapping("/api/gestion")
public class ProductosController {

	private static final Logger LOG = LoggerFactory.getLogger(ProductosController.class);

	private final ServicioProductos serviceProductos;
	private final CloudinaryService serviceCloudinary;
	private final CategoriasServiceImpl serviceCategorias;


	public ProductosController(ServicioProductos serviceProductos, CloudinaryService serviceCloudinary,
			CategoriasServiceImpl serviceCategorias) {
		super();
		this.serviceProductos = serviceProductos;
		this.serviceCloudinary = serviceCloudinary;
		this.serviceCategorias = serviceCategorias;
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
	            	respuesta.setCode(HttpStatus.NO_CONTENT.value()); // 204
	                respuesta.setMessage("No hay productos registrados.");
	                respuesta.setData(new ArrayList<>());

	                //
	                return ResponseEntity.ok(respuesta);
	            }

	        } catch (Exception e) {
	            // Caso 3: Ocurrió un error inesperado (BAD REQUEST 400 o INTERNAL SERVER ERROR 500)
	            e.printStackTrace();

	            respuesta.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value()); // Mejor usar 500 para errores de servidor
	            respuesta.setMessage("Ocurrió un error interno del servidor: " + e.getMessage());
	            respuesta.setData(null);

	            LOG.info("Error" + respuesta.getMessage());
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
		            Map<String, String> uploadResult = serviceCloudinary.subirImagen(imagen);

		            Imagenes img = new Imagenes();
		            img.setNombre_imagen(imagen.getOriginalFilename());
		            img.setUrl_imagen(uploadResult.get("url")); // ahora sí tiene URL real
		            img.setPublic_id(uploadResult.get("public_id"));
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
			LOG.info("Error" + respuesta.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
		}
	}

	@PostMapping("/productos/editar")
	public ResponseEntity<GenericResponse<String>> editarProducto(@RequestPart("producto") Productos producto,
			@RequestPart(value = "imagenes", required = false) List<MultipartFile> nuevasImagenes,
			@RequestPart(value = "imagenesEliminadas", required = false) String imagenesEliminadasJson) {

		GenericResponse<String> respuesta = new GenericResponse<>();

		try {
			// 1️⃣ Obtener producto de la DB
			Productos productoDB = serviceProductos.obtenerProductoPorId(producto.getIdProducto());
			if (productoDB == null) {
				respuesta.setCode(HttpStatus.NOT_FOUND.value());
				respuesta.setMessage("Producto no encontrado");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
			}

			// 2️⃣ Actualizar campos básicos
			productoDB.setNombre(producto.getNombre());
			productoDB.setDescripcion(producto.getDescripcion());

			// 3️⃣ Manejar categoría de forma segura
			if (producto.getCategoria() != null && producto.getCategoria().getIdCategoria() != null) {
				CategoriasModels cat = serviceCategorias.obtenerCategoriaPorId(producto.getCategoria().getIdCategoria());
				productoDB.setCategoria(cat); // Ahora es un objeto completo
			}

			// 4️⃣ Procesar imágenes eliminadas
			if (imagenesEliminadasJson != null && !imagenesEliminadasJson.isEmpty()) {
				
				//asegurar que la lista no sea nula 
				if (productoDB.getImagen() == null) {
					productoDB.setImagen(new ArrayList<>());
					
				}
				
				
				ObjectMapper mapper = new ObjectMapper();
				List<String> imagenesEliminadas = mapper.readValue(imagenesEliminadasJson,
						new TypeReference<List<String>>() {
						});
				productoDB.getImagen().removeIf(img -> imagenesEliminadas.contains(img.getUrl_imagen()));
			}

			// 5️⃣ Subir nuevas imágenes
			if (nuevasImagenes != null && !nuevasImagenes.isEmpty()) {
				for (MultipartFile imagen : nuevasImagenes) {
					
					 // Subimos la imagen a Cloudinary
		            Map<String, String> uploadResult = serviceCloudinary.subirImagen(imagen);

		            Imagenes img = new Imagenes();
		            img.setNombre_imagen(imagen.getOriginalFilename());
		            img.setUrl_imagen(uploadResult.get("url")); // ahora sí tiene URL real
		            //img.setPublic_id(uploadResult.get("public_id"));
					productoDB.getImagen().add(img);
				}
			}

			// 6️⃣ Guardar cambios
			serviceProductos.actualizarProducto(productoDB);

			respuesta.setCode(HttpStatus.OK.value());
			respuesta.setMessage("Producto actualizado correctamente");
			return ResponseEntity.ok(respuesta);

		} catch (Exception e) {
			e.printStackTrace();
			respuesta.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			respuesta.setMessage("Error al editar producto: " + e.getMessage());
			LOG.info("Error" + respuesta.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
		}
	}

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
			LOG.info("Error" + respuesta.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
		}
	}
	
	

}
