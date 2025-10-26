package com.cocinas.integrales.negocio.service;

import com.cocinas.integrales.negocio.model.GenericResponse;

public interface IAuthService {
    GenericResponse<String> authenticate(String username, String password);
}
