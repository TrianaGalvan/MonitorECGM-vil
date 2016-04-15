package com.example.trianaandaluciaprietogalvan.helloworldsupport.web;

import com.example.trianaandaluciaprietogalvan.helloworldsupport.entities.Cardiologo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by trianaandaluciaprietogalvan on 09/04/16.
 */
public interface CardiologoService {
    @GET("/ultimo/cardiologos")
    Call<List<Cardiologo>> obtenerCardiologos();

    @GET("/ultimo/cardiologo/{id}")
    Call<Cardiologo> obtenerCardiologo(@Path("id")Integer idCardiologo);
}
