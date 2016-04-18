package com.example.trianaandaluciaprietogalvan.helloworldsupport.web;

import com.example.trianaandaluciaprietogalvan.helloworldsupport.entities.Paciente;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by trianaandaluciaprietogalvan on 04/04/16.
 */
public interface PacienteService {
    @FormUrlEncoded
    @POST("/ultimo/paciente/login")
    Call<Paciente> loginPaciente(@Field("correo") String correo,@Field("pass") String pass);
    @FormUrlEncoded
    @POST("/ultimo/paciente/verificarCorreo")
    Call<Boolean> verificarCorreo(@Field("correo") String correo);
    @POST("/ultimo/paciente")
    Call<Paciente> insertarPaciente(@Body Paciente paciente);
    @PUT("/ultimo/paciente/{id}")
    Call<String> actualizarPaciente(@Path("id") int idPaciente, @Body Paciente paciente);
}
