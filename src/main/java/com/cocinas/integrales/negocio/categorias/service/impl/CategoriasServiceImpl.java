package com.cocinas.integrales.negocio.categorias.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cocinas.integrales.negocio.categorias.model.CategoriasModels;
import com.cocinas.integrales.negocio.productos.model.Productos;
import com.cocinas.integrales.negocio.productos.service.impl.ServicioProductos;


@Service
public class CategoriasServiceImpl {
	
	
	private final ServicioProductos productoService;

	    // 🔹 Inyección por constructor (recomendada)
	    public CategoriasServiceImpl(ServicioProductos productoService) {
	        this.productoService = productoService;
	    }
	
	public final List<CategoriasModels> todosLasCategorias = new ArrayList<>();
	private int nextId = 1; // Contador para asignar IDs automáticamente
	
	
	 public List<CategoriasModels> agregarCategorias(CategoriasModels req) {
    	 if (req.getIdCategoria() == 0) {
    		 req.setIdCategoria(nextId++);
         }
    	     	
    	 todosLasCategorias.add(req);
		return todosLasCategorias;
	}

	public List<CategoriasModels> getCategorias() {

		System.out.println("Mostramos las categorias registradas "+"\n" + todosLasCategorias);
		return todosLasCategorias;
	}
    
	
//	 public List<CategoriasModels> getCategorias() {
//			List<CategoriasModels> categorias = new ArrayList<>();
//			
//			// Esto está bien, se agrega un objeto de tipo CategoriasProductos
//			categorias.add(new CategoriasModels(1,"Integrales",""));
//			categorias.add(new CategoriasModels(2,"Electrodomésticos",""));
//			categorias.add(new CategoriasModels(3,"Muebles",""));
//			categorias.add(new CategoriasModels(4,"Decoración",""));
//			categorias.add(new CategoriasModels(5,"todos",""));
//			
//			//categorias.removeAll(categorias);
//
//			System.out.println("Mostramos los productos");
//			return categorias;
//		}
	    
	    
	    public List<Productos> getProductosPorCategoria(String categoria) {
	    	
	    			
	        String catNormalizada = categoria.toLowerCase().replaceAll("\\s", "").replaceAll("[áéíóú]", "a");
	        return productoService.getProductos().stream() 
	        		//todosLosProductos.stream()
	                .filter(p -> {
	                    String nombreCat = p.getCategoria().getNombreCategoria().toLowerCase()
	                                       .replaceAll("\\s", "").replaceAll("[áéíóú]", "a");
	                    return nombreCat.equals(catNormalizada);
	                })
	                .collect(Collectors.toList());
	    }
    
   

}
