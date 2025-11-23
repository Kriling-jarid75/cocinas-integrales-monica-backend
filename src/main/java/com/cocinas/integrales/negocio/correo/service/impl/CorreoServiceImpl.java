package com.cocinas.integrales.negocio.correo.service.impl;

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

	public CorreoServiceImpl(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
		this.mailSender = mailSender;
		this.templateEngine = templateEngine;
	}

	public void enviarCorreo(CorreoRequest request) throws MessagingException {

		// Obtener la fecha
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		String dateTime = LocalDateTime.now().format(formato);

		String asuntoTexto = request.getAsuntoCliente().getEtiqueta();

		// Detectar si hay correo válido
		boolean clienteTieneCorreo = esCorreoValido(request.getCorreo_telefono_Cliente());
		String Correo_O_Telefono = clienteTieneCorreo ? request.getCorreo_telefono_Cliente() : request.getCorreo_telefono_Cliente();

		// Pasar variables al template
		Context context = new Context();
		context.setVariable("nombre", request.getNombreCliente());
		context.setVariable("correo", Correo_O_Telefono);
		context.setVariable("asunto", asuntoTexto);
		context.setVariable("mensaje", request.getMensajeCliente());
		context.setVariable("fecha", dateTime);

		// ===== 1. Correo para el administrador =====
		String htmlAdmin = templateEngine.process("cotizacion-email", context);

		MimeMessage messageAdmin = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(messageAdmin, true, "UTF-8");

		helper.setTo(correoFrom);
		helper.setFrom(clienteTieneCorreo ? request.getCorreo_telefono_Cliente() : correoFrom);
		helper.setSubject("Nueva solicitud de cotización");
		helper.setText(htmlAdmin, true);
		helper.addInline("image1", new ClassPathResource("img/Cocinas_Integrales_Monica.png"));

		// Enviar correo al admin SIEMPRE
		mailSender.send(messageAdmin);

		// ===== 2. Correo para confirmación del cliente =====
		if (clienteTieneCorreo) {
			String htmlConfirmacionCliente = templateEngine.process("confirmacion-email", context);

			MimeMessage messageCliente = mailSender.createMimeMessage();
			MimeMessageHelper helper2 = new MimeMessageHelper(messageCliente, true, "UTF-8");

			helper2.setTo(request.getCorreo_telefono_Cliente());
			helper2.setFrom(correoFrom);
			helper2.setSubject("Hemos recibido tu solicitud ✔️");
			helper2.setText(htmlConfirmacionCliente, true);
			helper2.addInline("image1", new ClassPathResource("img/Cocinas_Integrales_Monica.png"));

			mailSender.send(messageCliente);
		}
	}

	private boolean esCorreoValido(String correo) {
		if (correo == null || correo.isBlank())
			return false;
		return correo.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
	}

}
