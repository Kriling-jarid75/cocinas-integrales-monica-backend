package com.cocinas.integrales.negocio.productos.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.cocinas.integrales.negocio.config.Database.DatabaseConfig;
import com.cocinas.integrales.negocio.constansDB.ConstantesDB;
import com.cocinas.integrales.negocio.productos.model.Productos;
import com.google.gson.Gson;




@Repository
public class ProductosDao {

	private static final Logger LOG = LoggerFactory.getLogger(ProductosDao.class);

	private final DatabaseConfig dbConfig;

	public ProductosDao(DatabaseConfig dbConfig) {
		this.dbConfig = dbConfig;
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
            cs.setInt(3, producto.getCategoria().getIdCategoria());

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

}
