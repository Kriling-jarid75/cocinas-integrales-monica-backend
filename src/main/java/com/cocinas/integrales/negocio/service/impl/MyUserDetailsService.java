package com.cocinas.integrales.negocio.service.impl;



import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;

import com.cocinas.integrales.negocio.model.Administrador;
import com.cocinas.integrales.negocio.service.IAdministrador;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;



@Service
public class MyUserDetailsService implements UserDetailsService {
	
	private static final Logger LOG = LoggerFactory.getLogger(MyUserDetailsService.class);
    private final IAdministrador administradorDao;
    

    public MyUserDetailsService(IAdministrador administradorDao) {
        this.administradorDao = administradorDao;
        
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Administrador admin = administradorDao.findByUsername(username);

        if (admin.getUsername() == null || admin.getUsername().isEmpty()) {
            LOG.info("Usuario no encontrado");
            throw new UsernameNotFoundException("Usuario no encontrado");
        }

        // Convertimos el rol en autoridad de Spring Security
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(admin.getRol().toUpperCase())); // Ej: "ADMIN"

        return new User(admin.getUsername(), admin.getPassword(), authorities);
    }

    
}
