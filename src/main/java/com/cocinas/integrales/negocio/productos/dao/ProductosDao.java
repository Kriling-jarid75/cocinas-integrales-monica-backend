package com.cocinas.integrales.negocio.productos.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.cocinas.integrales.negocio.productos.model.Productos;



@Repository
public class ProductosDao {

	
private static final Logger LOG = LoggerFactory.getLogger(ProductosDao.class);
	
	
	private final String url = "jdbc:mysql://localhost:3306/cocinas_integrales_monica";
	private final String user = "root";
	private final String password = "root";

   

	public boolean registrarProductoDao(Productos producto) {
	    String sql = "INSERT INTO productos (nombre_producto, descripcion_producto, id_categoria) VALUES (?, ?, ?)";

	    try (Connection conn = DriverManager.getConnection(url, user, password);
	         PreparedStatement ps = conn.prepareStatement(sql)) {

	        ps.setString(1, producto.getNombre());
	        ps.setString(2, producto.getDescripcion());
	      //  ps.setString(3, producto.getCategoria());

	        int rowsAffected = ps.executeUpdate();
	        LOG.info("Producto registrado: {} filas afectadas", rowsAffected);
	        return rowsAffected > 0;

	    } catch (SQLException e) {
	        LOG.error("Error al registrar producto: ", e); // imprime stacktrace completo
	        return false;
	    }
	}

	
}
