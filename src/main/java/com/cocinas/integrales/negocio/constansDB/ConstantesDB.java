package com.cocinas.integrales.negocio.constansDB;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConstantesDB {

	/* SP DE PRODUCTOS */
	
	consultar_usuario_contraseña("{CALL sp_consulta_admin(?)}"),

	consultar_todos_produtos(""),
	registro_productos("{CALL sp_registro_productos(?, ?, ?, ?)}"),
	editar_producto(""),
	eliminar_producto(""),
	
	
	/* SP DE CATEGORIAS */
	
	consulta_todas_categorias("{CALL sp_consulta_de_categorias()}"),
	
	consultar_producto_por_categoria("{CALL sp_consulta_productos_por_categoria(?)}"),
	
	registrar_categoria("{CALL sp_registro_categorias(?)}"),
	editar_categoria(""),
	eliminar_categoria(""),
	
	
	;
	

	// Variable de instancia para almacenar el valor asociado
	private final String query;

}
