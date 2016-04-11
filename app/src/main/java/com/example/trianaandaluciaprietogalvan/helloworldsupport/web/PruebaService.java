package com.example.trianaandaluciaprietogalvan.helloworldsupport.web;

import com.example.trianaandaluciaprietogalvan.helloworldsupport.entities.Prueba;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by trianaandaluciaprietogalvan on 10/04/16.
 */
public interface PruebaService {
    @GET("/ultimo/pruebas")
    Call<List<Prueba>> obtenerPruebas(@Query("email") String email);
}
