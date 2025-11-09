package com.cocinas.integrales.negocio.dao;



import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.cocinas.integrales.negocio.config.Database.DatabaseConfig;
import com.cocinas.integrales.negocio.constansDB.ConstantesDB;
import com.cocinas.integrales.negocio.model.Administrador;
import com.cocinas.integrales.negocio.service.IAdministrador;




@Repository
public class AdministradorDao implements IAdministrador {
	
	
	private static final Logger LOG = LoggerFactory.getLogger(AdministradorDao.class);
	
	
	private final DatabaseConfig dbConfig;
	
		
	public AdministradorDao(DatabaseConfig dbConfig) {
			this.dbConfig = dbConfig;
		}

   

	@Override
	public Administrador findByUsername(String username) {
	    Administrador admin = null; // Será null si no se encuentra el usuario

	    String sql = ConstantesDB.consultar_usuario_contraseña.getQuery(); // ejemplo: "{CALL sp_consulta_admin(?)}"

	    try (Connection conn = DriverManager.getConnection(
	                dbConfig.getUrl(),
	                dbConfig.getUsername(),
	                dbConfig.getPassword());
	         CallableStatement cs = conn.prepareCall(sql)) {

	        // ✅ 1. Asignar el parámetro antes de ejecutar
	        cs.setString(1, username);

	        // ✅ 2. Ejecutar el SP
	        try (ResultSet rs = cs.executeQuery()) {
	            if (rs.next()) {
	                admin = new Administrador();
	                admin.setUsername(rs.getString("nombre_usuarios"));
	                admin.setPassword(rs.getString("password_usuarios"));
	                admin.setRol(rs.getString("nombre_rol"));
	            }
	        }

	    } catch (SQLException e) {
	        LOG.error("❌ Error al validar usuario: {}", e.getMessage(), e);
	    }

	    return admin;
	}




	

}