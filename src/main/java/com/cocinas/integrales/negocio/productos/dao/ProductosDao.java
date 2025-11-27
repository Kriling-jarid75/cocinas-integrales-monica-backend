package com.cocinas.integrales.negocio.productos.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.cocinas.integrales.negocio.categorias.model.CategoriasModels;
import com.cocinas.integrales.negocio.config.Database.DatabaseConfig;
import com.cocinas.integrales.negocio.constansDB.ConstantesDB;
import com.cocinas.integrales.negocio.productos.model.Imagenes;
import com.cocinas.integrales.negocio.productos.model.Productos;
import com.google.gson.Gson;




@Repository
public class ProductosDao {

	private static final Logger LOG = LoggerFactory.getLogger(ProductosDao.class);

	private final DatabaseConfig dbConfig;

	public ProductosDao(DatabaseConfig dbConfig) {
		this.dbConfig = dbConfig;
	}
	
	
	
	public List<Productos> consultarTodosLosProductosDao() {


		String sql = ConstantesDB.consultar_todos_produtos.getQuery();


		List<Productos> todosLosProductos = new ArrayList<>();


	    try (Connection conn = DriverManager.getConnection(
	            dbConfig.getUrl(),
	            dbConfig.getUsername(),
	            dbConfig.getPassword());
	         CallableStatement cs = conn.prepareCall(sql);
	         ResultSet rs = cs.executeQuery()) {
	    	
	    	
	    	  // Mapa para agrupar imágenes por producto
	        Map<Long, Productos> productosMap = new HashMap<>();

	        while (rs.next()) {
	            Long idProducto = rs.getLong("id_producto");

	            // Si el producto aún no se ha agregado, lo creamos
	            Productos producto = productosMap.get(idProducto);
	            if (producto == null) {
	                producto = new Productos();
	                producto.setIdProducto(idProducto);
	                producto.setNombre(rs.getString("nombre_producto"));
	                producto.setDescripcion(rs.getString("descripcion_producto"));

	                // Crear objeto categoría
	                CategoriasModels categoria = new CategoriasModels();
	                categoria.setIdCategoria(rs.getLong("id_categoria"));
	                categoria.setNombreCategoria(rs.getString("nombre_categoria"));
	                producto.setCategoria(categoria);

	                // Inicializar lista de imágenes
	                producto.setImagen(new ArrayList<>());

	                productosMap.put(idProducto, producto);
	            }

	            // Agregar imagen (si existe)
	            Long idImagen = rs.getLong("id_imagen");
	            String nombreImagen = rs.getString("nombre_imagen");
	            String rutaImagen = rs.getString("url_imagen");
	            String id_imagen_cloudinary = rs.getString("public_id");
	            
	            if (idImagen != 0 && rutaImagen != null) {
	                Imagenes imagen = new Imagenes();
	                imagen.setIdImagen(idImagen);
	                imagen.setNombre_imagen(nombreImagen);
	                imagen.setUrl_imagen(rutaImagen);
	                imagen.setPublic_id(id_imagen_cloudinary);
	                producto.getImagen().add(imagen);
	            }
	        }

	        todosLosProductos.addAll(productosMap.values());

	        LOG.info("✅ Consulta de productos exitosa, total: {}", todosLosProductos.size());
	        return todosLosProductos;

	    } catch (SQLException e) {
	        LOG.error("❌ Error al consultar productos: ", e);
	        return Collections.emptyList();
	    }
	}
	
	public boolean registrarProductoDao(Productos producto) {

		String sql = ConstantesDB.registro_productos.getQuery();

		

        try (Connection conn = DriverManager.getConnection(
                dbConfig.getUrl(),
                dbConfig.getUsername(),
                dbConfig.getPassword());
             CallableStatement cs = conn.prepareCall(sql)) {

            // 🔹 Parámetros de entrada
            cs.setString(1, producto.getNombre());
            cs.setString(2, producto.getDescripcion());
            cs.setLong(3, producto.getCategoria().getIdCategoria());

            // 🔹 Convertimos lista de imágenes a JSON (para el SP)
           String imagenesJson = new Gson().toJson(producto.getImagen());
            
         
            
            cs.setString(4, imagenesJson);

            // 🔹 Ejecutamos el SP
            int rowsAffected = cs.executeUpdate();

            LOG.info("✅ Producto registrado correctamente, filas afectadas: {}", rowsAffected);
            return rowsAffected > 0;

        } catch (SQLException e) {
            LOG.error("❌ Error al registrar producto: ", e);
            return false;
        }
    }
	
	public boolean actualizarProductoDao(Productos productos) {

		String sql = ConstantesDB.editar_producto.getQuery();

		try (Connection conn = DriverManager.getConnection(dbConfig.getUrl(), dbConfig.getUsername(),
				dbConfig.getPassword()); CallableStatement cs = conn.prepareCall(sql)) {

			cs.setLong(1, productos.getIdProducto());
			cs.setString(2, productos.getNombre());
			cs.setLong(3, productos.getCategoria().getIdCategoria());
			cs.setString(4, productos.getDescripcion());

			// 🔹 Convertimos lista de imágenes a JSON (para el SP)
			String imagenesJson = new Gson().toJson(productos.getImagen());

			cs.setString(5, imagenesJson);

			int rowsAffected = cs.executeUpdate();

			LOG.info("✅ El producto se actualizó correctamente, filas afectadas: {}", rowsAffected);

			

			return rowsAffected > 0;

		} catch (SQLException e) {
			LOG.error("❌ Error al actualizar el producto: ", e);
			return false;
		}
	}
	
	public boolean eliminarIDProductoDao(Productos producto) {

		String sql = ConstantesDB.eliminar_producto.getQuery();

		

        try (Connection conn = DriverManager.getConnection(
                dbConfig.getUrl(),
                dbConfig.getUsername(),
                dbConfig.getPassword());
             CallableStatement cs = conn.prepareCall(sql)) {

            // 🔹 Parámetros de entrada
            cs.setLong(1, producto.getIdProducto());
            
            // 🔹 Ejecutamos el SP
            int rowsAffected = cs.executeUpdate();

            LOG.info("✅ Producto eliminado correctamente, filas afectadas: {}", rowsAffected);
            return rowsAffected > 0;

        } catch (SQLException e) {
            LOG.error("❌ Error al eliminar producto: ", e);
            return false;
        }
    }



	public Productos obtenerIdDeProducto(Long idProducto) {
		 
	    String sql = ConstantesDB.busqueda_por_id_producto.getQuery();
	 
	    Productos pro = new Productos();
	 
	    try (Connection conn = DriverManager.getConnection(
	            dbConfig.getUrl(),
	            dbConfig.getUsername(),
	            dbConfig.getPassword());
	         CallableStatement cs = conn.prepareCall(sql)) {
	 
	        // 🔹 Parámetro de entrada
	        cs.setLong(1, idProducto);
	 
	        // 🔹 Ejecutar query después de setear parámetros
	        try (ResultSet rs = cs.executeQuery()) {
	            while (rs.next()) {
	                pro.setIdProducto(rs.getLong("id_producto"));
	                // puedes setear otros campos si quieres
	            }
	        }
	 
	    } catch (SQLException e) {
	        LOG.error("❌ Error al consultar el ID del producto: ", e);
	        return pro;
	    }
	 
	    return pro;
	}

}
