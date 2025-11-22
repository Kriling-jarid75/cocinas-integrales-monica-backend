package com.cocinas.integrales.negocio.correo.service.impl;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
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
		
		//Obtener la fecha
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		String dateTime = LocalDateTime.now().format(formato);
		

		String asuntoTexto = request.getAsuntoCliente().getEtiqueta(); // ← el texto como "Toma de medidas"
		

		
		
	    Context context = new Context();
	    context.setVariable("nombre", request.getNombreCliente());
	    context.setVariable("correo", request.getCorreoCliente());
	    context.setVariable("asunto", asuntoTexto);
	    context.setVariable("mensaje", request.getMensajeCliente());
	    context.setVariable("fecha", dateTime);
	 
	 // ====== 1. HTML para el administrador ======
	    String htmlAdmin = templateEngine.process("cotizacion-email", context);
	 
	    MimeMessage messageAdmin = mailSender.createMimeMessage();
	    MimeMessageHelper helper = new MimeMessageHelper(messageAdmin, true, "UTF-8");
	 
	    helper.setTo(correoFrom);
	    helper.setFrom(request.getCorreoCliente());
	    helper.setSubject("Nueva solicitud de cotización");
	    helper.setText(htmlAdmin, true);
	    helper.addInline("image1", new ClassPathResource("img/Cocinas_Integrales_Monica.png"));
	    
	 
	 
	    
	    
	 // ====== 2. HTML para confirmación del cliente ======

	    String htmlConfirmacionCliente = templateEngine.process("confirmacion-email", context);
	     
	    MimeMessage messageCliente = mailSender.createMimeMessage();
	    MimeMessageHelper helper2 = new MimeMessageHelper(messageCliente, true, "UTF-8");
	     
	    // AQUÍ CAMBIOS IMPORTANTES:
	    helper2.setTo(request.getCorreoCliente());  // ← Ahora sí, se envía al cliente
	    helper2.setFrom(correoFrom);                // ← Tu correo oficial
	    helper2.setSubject("Hemos recibido tu solicitud ✔️");
	    helper2.setText(htmlConfirmacionCliente, true);
	    helper2.addInline("image1", new ClassPathResource("img/Cocinas_Integrales_Monica.png"));
	 
	    // ====== Envío ======
	    mailSender.send(messageAdmin);
	    mailSender.send(messageCliente);
	    
	    
	    //Flujo final correcto
	    //📩 1. Correo que llega al administrador
	    //To: correoFrom
	    //From: correo del cliente
	    //Contenido: datos que mandó el cliente
	    
	    
	    //📩 2. Correo que llega al cliente (copia)
	    //To: correo del cliente
	    //From: tu correo oficial (correoFrom)
	    //Contenido: mensaje “Gracias, recibimos tu solicitud”
	    
	    
	    
	    
	}
}
