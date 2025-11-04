package com.cocinas.integrales.negocio.categorias.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.cocinas.integrales.negocio.categorias.model.CategoriasModels;
import com.cocinas.integrales.negocio.config.Database.DatabaseConfig;
import com.cocinas.integrales.negocio.constansDB.ConstantesDB;
import com.cocinas.integrales.negocio.productos.model.Productos;




@Repository
public class CategoriasDao {
	
	private static final Logger LOG = LoggerFactory.getLogger(CategoriasDao.class);

	private final DatabaseConfig dbConfig;

	public CategoriasDao(DatabaseConfig dbConfig) {
		this.dbConfig = dbConfig;
	}
	
	
	public List<CategoriasModels> consultarCategoriasDao() {


		String sql = ConstantesDB.consulta_todas_categorias.getQuery();

	    List<CategoriasModels> categoriasList = new ArrayList<>();

	    try (Connection conn = DriverManager.getConnection(
	            dbConfig.getUrl(),
	            dbConfig.getUsername(),
	            dbConfig.getPassword());
	         CallableStatement cs = conn.prepareCall(sql);
	         ResultSet rs = cs.executeQuery()) {

	        while (rs.next()) {
	            CategoriasModels categoria = new CategoriasModels();
	            categoria.setIdCategoria(rs.getInt("id_categoria"));
	            categoria.setNombreCategoria(rs.getString("nombre_categoria"));
	            categoriasList.add(categoria);
	        }

	        LOG.info("✅ Consulta de categorías exitosa, total: {}", categoriasList.size());
	        return categoriasList;

	    } catch (SQLException e) {
	        LOG.error("❌ Error al consultar categorías: ", e);
	        return Collections.emptyList();
	    }
	}
	
	
	
	public List<Productos> consultarProdutoPorCategoriaDao(String nombreCategoria) {

		String sql = ConstantesDB.consultar_producto_por_categoria.getQuery();

		List<Productos> productosList = new ArrayList<>();

		try (Connection conn = DriverManager.getConnection(dbConfig.getUrl(), dbConfig.getUsername(),
				dbConfig.getPassword()); CallableStatement cs = conn.prepareCall(sql)) {

			cs.setString(1, nombreCategoria);

			try (ResultSet rs = cs.executeQuery()) {
				while (rs.next()) {
					Productos producto = new Productos();
					producto.setIdProducto(rs.getLong("id_producto"));
					producto.setNombre(rs.getString("nombre_producto"));
					producto.setDescripcion(rs.getString("descripcion_producto"));

					// ✅ Crear y asignar el objeto categoría completo
					CategoriasModels categoria = new CategoriasModels();
					categoria.setIdCategoria(rs.getInt("id_categoria"));
					categoria.setNombreCategoria(rs.getString("nombre_categoria"));

					producto.setCategoria(categoria);

					productosList.add(producto);
				}
			}

			LOG.info("✅ Consulta exitosa, productos encontrados: {}", productosList.size());
			return productosList;

		} catch (SQLException e) {
			LOG.error("❌ Error al consultar categorías: ", e);
			return Collections.emptyList();
		}
	}

	
	
	public boolean registrarCategoriasDao(CategoriasModels categorias) {
		
		String sql = ConstantesDB.registrar_categoria.getQuery();
	

        try (Connection conn = DriverManager.getConnection(
                dbConfig.getUrl(),
                dbConfig.getUsername(),
                dbConfig.getPassword());
             CallableStatement cs = conn.prepareCall(sql)) {

            // 🔹 Parámetros de entrada
            cs.setString(1, categorias.getNombreCategoria());
           

            // 🔹 Ejecutamos el SP
            int rowsAffected = cs.executeUpdate();

            LOG.info("✅ Producto registrado correctamente, filas afectadas: {}", rowsAffected);
            return rowsAffected > 0;

        } catch (SQLException e) {
            LOG.error("❌ Error al registrar producto: ", e);
            return false;
        }
    }
}
