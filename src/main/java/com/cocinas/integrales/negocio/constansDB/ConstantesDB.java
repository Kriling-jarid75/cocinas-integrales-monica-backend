package com.cocinas.integrales.negocio.constansDB;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConstantesDB {

	/* SP DE PRODUCTOS */
	
	busqueda_por_id_producto("{CALL sp_busqueda_producto_por_id(?)}"),
	
	consultar_usuario_contraseña("{CALL sp_consulta_admin(?)}"),

	consultar_todos_produtos("{CALL sp_consulta_todos_productos()}"),
	
	registro_productos("{CALL sp_registro_productos(?, ?, ?, ?)}"),
	
	editar_producto("{CALL sp_actualizar_productos(?, ?, ?, ?, ?)}"),
	
	eliminar_producto("{CALL sp_eliminar_id_producto(?)}"),
	
	
	
	/* SP DE CATEGORIAS */
	busqueda_por_id_categoria("{CALL sp_busqueda_categoria_por_id(?)}"),
	
	consulta_todas_categorias("{CALL sp_consulta_de_categorias()}"),
	
	consultar_producto_por_categoria("{CALL sp_consulta_productos_por_categoria(?)}"),
	
	registrar_categoria("{CALL sp_registro_categorias(?)}"),
	
	editar_categoria("{CALL sp_editar_categorias(?,?)}"),
	
	eliminar_categoria("{CALL sp_eliminar_id_categoria(?)}");
	

	// Variable de instancia para almacenar el valor asociado
	private final String query;

}
