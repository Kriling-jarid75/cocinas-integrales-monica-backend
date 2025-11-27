package com.cocinas.integrales.negocio.cloudinary.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
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

    public Map<String, String> subirImagen(MultipartFile archivo) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(archivo.getBytes(),
                ObjectUtils.asMap("folder", "productos"));
        
        String url = uploadResult.get("secure_url").toString();
        String publicId = uploadResult.get("public_id").toString();
        
        //enviamos ambos valores al service de productos
        Map<String, String> resultado = new HashMap<>();
        resultado.put("url", url);
        resultado.put("public_id", publicId);
       
        return resultado; 
    }
    
    
 // ✅ Método para eliminar una imagen
//    public boolean eliminarImagen(String publicId) {
//        try {
//            Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
//            return "ok".equals(result.get("result")); // devuelve true si se eliminó correctamente
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
    
    public boolean deteleImageCloudinary(String idImagen) throws IOException {
    	 Map result = cloudinary.uploader().destroy(idImagen, ObjectUtils.emptyMap());
    	 
    	 System.out.println("--------------- mostramos la respuesta ----------------------" + result);
    	 
    	 
       return "ok".equals(result.get("result")); // devuelve true si se eliminó correctamente
    }
}
