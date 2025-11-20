package com.cocinas.integrales.negocio.correo.service.impl;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.cocinas.integrales.negocio.correo.model.CorreoRequest;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class CorreoServiceImpl {


	@Value("${app.mail.from}")
    private String correoFrom;

	private final JavaMailSender mailSender;
	private final SpringTemplateEngine templateEngine;
	
	public CorreoServiceImpl(JavaMailSender mailSender,SpringTemplateEngine templateEngine) {
		this.mailSender = mailSender;
		this.templateEngine = templateEngine;
	}

	public void enviarCorreo(CorreoRequest request) throws MessagingException {
		 
	    Context context = new Context();
	    context.setVariable("nombre", request.getNombreCliente());
	    context.setVariable("correo", request.getCorreoCliente());
	    context.setVariable("asunto", request.getAsuntoCliente());
	    context.setVariable("mensaje", request.getMensajeCliente());
	 
	    String html = templateEngine.process("cotizacion-email", context);
	 
	    MimeMessage message = mailSender.createMimeMessage();
	    MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
	 
	    helper.setTo(request.getCorreoCliente());
	    helper.setFrom("ventas@cocinasmonica.com");
	    helper.setSubject("Nueva solicitud de cotización");
	    helper.setText(html, true);
	 
	    mailSender.send(message);
	}
}
