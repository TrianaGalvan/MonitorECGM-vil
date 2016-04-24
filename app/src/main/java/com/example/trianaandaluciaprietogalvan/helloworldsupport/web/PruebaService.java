package com.example.trianaandaluciaprietogalvan.helloworldsupport.web;

import com.example.trianaandaluciaprietogalvan.helloworldsupport.entities.Prueba;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by trianaandaluciaprietogalvan on 10/04/16.
 */
public interface PruebaService {
    @GET("/prueba/correo")
    Call<List<Prueba>> obtenerPruebas(@Query("email") String email);


    Call<Prueba> generarPrueba(@Part MultipartBody.Part prueba);
}
