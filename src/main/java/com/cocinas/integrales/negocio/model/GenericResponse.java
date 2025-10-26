package com.cocinas.integrales.negocio.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GenericResponse<T> {

	private int code;
	private String message;
	private T data;
}
