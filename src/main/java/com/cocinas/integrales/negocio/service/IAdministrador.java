package com.cocinas.integrales.negocio.service;



import com.cocinas.integrales.negocio.model.Administrador;

public interface IAdministrador {

	Administrador findByUsername(String username);



}
