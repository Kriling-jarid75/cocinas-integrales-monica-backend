package com.cocinas.integrales.negocio.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Utils {

	private static final Logger LOG = LoggerFactory.getLogger(Utils.class);

	public Date dateFormat(String date) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		LOG.info("mostramos la fecha completa o formateada " + format);
		
		return format.parse(date);
		
		
	
		
	}
	
	
	
}
