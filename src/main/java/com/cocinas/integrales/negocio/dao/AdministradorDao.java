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
		Administrador admin = new Administrador();

		String sql = ConstantesDB.consultar_usuario_contraseña.getQuery();

		try (Connection conn = DriverManager.getConnection(dbConfig.getUrl(), dbConfig.getUsername(),
				dbConfig.getPassword());
				CallableStatement cs = conn.prepareCall(sql);

				ResultSet rs = cs.executeQuery()) {

			cs.setString(1, username);

			if (rs.next()) {
				admin.setUsername(rs.getString("nombre_usuarios"));
				admin.setPassword(rs.getString("password_usuarios"));
				admin.setRol(rs.getString("nombre_rol")); // aquí guardamos el rol como texto
			}

		} catch (SQLException e) {
			LOG.info("Error al validar usuario: {}", e.getMessage());
		}

		return admin;
	}



	

}