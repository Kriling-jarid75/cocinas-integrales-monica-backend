package com.cocinas.integrales.negocio.dao;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import com.cocinas.integrales.negocio.model.Administrador;
import com.cocinas.integrales.negocio.service.IAdministrador;




@Repository
public class AdministradorDao implements IAdministrador {
	
	
	private static final Logger LOG = LoggerFactory.getLogger(AdministradorDao.class);
	
	
	private final String url = "jdbc:mysql://localhost:3306/cocinas_integrales_monica";
	private final String user = "root";
	private final String password = "root";

   

	@Override
    public Administrador findByUsername(String username) {
        Administrador admin = new Administrador();
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT u.nombre_usuarios, u.password_usuarios, r.nombre_rol " +
                         "FROM usuarios u " +
                         "JOIN roles r ON u.id_rol = r.id_rol " +
                         "WHERE u.nombre_usuarios = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                admin.setUsername(rs.getString("nombre_usuarios"));
                admin.setPassword(rs.getString("password_usuarios"));
                admin.setRol(rs.getString("nombre_rol")); // aquí guardamos el rol como texto
            }

        } catch (SQLException e) {
            LOG.error("Error al validar usuario: {}", e.getMessage());
        }

        return admin;
    }



	

}