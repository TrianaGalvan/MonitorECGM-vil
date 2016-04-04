package com.example.trianaandaluciaprietogalvan.helloworldsupport.web;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by trianaandaluciaprietogalvan on 04/04/16.
 */
public class ServicioWeb {
    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://192.168.0.5:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();


    public static void loginPaciente(String correo, String pass, Callback<Boolean> respuesta){
        PacienteService pacienteService = retrofit.create(PacienteService.class);
        Call<Boolean> call = pacienteService.loginPaciente(correo,pass);
        call.enqueue(respuesta);
    }


}
