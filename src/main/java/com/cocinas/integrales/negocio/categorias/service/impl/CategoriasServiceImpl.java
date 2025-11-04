package com.cocinas.integrales.negocio.categorias.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cocinas.integrales.negocio.categorias.dao.CategoriasDao;
import com.cocinas.integrales.negocio.categorias.model.CategoriasModels;
import com.cocinas.integrales.negocio.productos.dao.ProductosDao;
import com.cocinas.integrales.negocio.productos.model.Productos;
import com.cocinas.integrales.negocio.productos.service.impl.ServicioProductos;


@Service
public class CategoriasServiceImpl {
	
	
	private final ServicioProductos productoService;
	private final CategoriasDao vamosAlDaoDeCategorias;

	    // 🔹 Inyección por constructor (recomendada)
	    public CategoriasServiceImpl(ServicioProductos productoService,
	    		CategoriasDao vamosAlDaoDeCategorias) {
	        this.productoService = productoService;
	        this.vamosAlDaoDeCategorias = vamosAlDaoDeCategorias;
	    }
	    
	    
	 
	
	public final List<CategoriasModels> todosLasCategorias = new ArrayList<>();
	private int nextId = 1; // Contador para asignar IDs automáticamente
	
	
//	 public List<CategoriasModels> agregarCategorias(CategoriasModels req) {
//    	 if (req.getIdCategoria() == 0) {
//    		 req.setIdCategoria(nextId++);
//         }
//    	     	
//    	 todosLasCategorias.add(req);
//		return todosLasCategorias;
//	}
//	 
	 
	 
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
				return "Ocurrió un error al registrar la categoria: " + e.getMessage();
			}
		}
	 
	 
	 
	 public List<CategoriasModels> obtenerTodasLasCategorias() {
		 
		 List<CategoriasModels> listaDeCategorias = new ArrayList<>();
		 
		 listaDeCategorias = vamosAlDaoDeCategorias.consultarCategoriasDao();
		 
		 return listaDeCategorias;
	 }
	 
	 
	 
	 

//	public List<CategoriasModels> getCategorias() {
//
//		System.out.println("Mostramos las categorias registradas "+"\n" + todosLasCategorias);
//		return todosLasCategorias;
//	}
    
	
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
	    	
	    	
	    	
	    	
	    	 List<Productos> listaDeProductosPorCategoria =  vamosAlDaoDeCategorias.consultarProdutoPorCategoriaDao(categoria);
			 
			 return listaDeProductosPorCategoria;
	    	
	    	
	    	
//	    			
//	        String catNormalizada = categoria.toLowerCase().replaceAll("\\s", "").replaceAll("[áéíóú]", "a");
//	        return productoService.getProductos().stream() 
//	        		//todosLosProductos.stream()
//	                .filter(p -> {
//	                    String nombreCat = p.getCategoria().getNombreCategoria().toLowerCase()
//	                                       .replaceAll("\\s", "").replaceAll("[áéíóú]", "a");
//	                    return nombreCat.equals(catNormalizada);
//	                })
//	                .collect(Collectors.toList());
	    }
    
   

}
