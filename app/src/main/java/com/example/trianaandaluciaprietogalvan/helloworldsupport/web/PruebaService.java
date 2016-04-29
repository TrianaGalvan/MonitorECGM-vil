package com.example.trianaandaluciaprietogalvan.helloworldsupport.web;

import com.example.trianaandaluciaprietogalvan.helloworldsupport.entities.Prueba;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by trianaandaluciaprietogalvan on 10/04/16.
 */
public interface PruebaService {
    @GET("/ultimo/prueba/correo")
    Call<List<Prueba>> obtenerPruebas(@Query("email") String email);

    @Multipart
    @POST("/ultimo/prueba")
    Call<Prueba> generarPrueba(@Part MultipartBody.Part archivo,@Part("prueba") String prueba);

    @GET("/ultimo/prueba/electrocardiograma/{id}")
    Call<ResponseBody> descargarPrueba(@Path("id") int id);
}
