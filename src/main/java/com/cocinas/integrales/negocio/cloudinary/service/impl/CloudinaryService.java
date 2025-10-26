package com.cocinas.integrales.negocio.cloudinary.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService() {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dhhm7xrs7",
                "api_key", "987719573838883",
                "api_secret", "UmRjjWXmJym3jrdq8zjqvksDKhQ"));
    }

    public String subirImagen(MultipartFile archivo) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(archivo.getBytes(),
                ObjectUtils.asMap("folder", "productos"));
        return uploadResult.get("secure_url").toString(); // retorna la URL de la imagen
    }
}
