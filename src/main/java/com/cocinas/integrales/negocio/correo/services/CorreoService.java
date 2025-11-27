package com.cocinas.integrales.negocio.correo.services;

import com.cocinas.integrales.negocio.correo.model.CorreoRequest;

import jakarta.mail.MessagingException;

public interface CorreoService {

	void enviarCorreo(CorreoRequest request) throws MessagingException;

	boolean esCorreoValido(String correo);

}
