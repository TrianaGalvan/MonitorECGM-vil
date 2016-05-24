package com.example.trianaandaluciaprietogalvan.helloworldsupport.web;

import com.example.trianaandaluciaprietogalvan.helloworldsupport.entities.Cardiologo;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.entities.Paciente;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.entities.Prueba;

import java.io.IOException;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by trianaandaluciaprietogalvan on 04/04/16.
 */
public class ServicioWeb {
    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://monitor-ecg046.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();


    public static void loginPaciente(String correo, String pass,String token, Callback<Paciente> respuesta){
        PacienteService pacienteService = retrofit.create(PacienteService.class);
        Call<Paciente> call = pacienteService.loginPaciente(correo, pass,token);
        call.enqueue(respuesta);
    }

    public static void verfificarCorreo(String correo,Callback<Boolean> respuesta){
        PacienteService pacienteService = retrofit.create(PacienteService.class);
        Call<Boolean> call = pacienteService.verificarCorreo(correo);
        call.enqueue(respuesta);
    }

    public static void obtenerCardiologos(Callback<List<Cardiologo>> respuesta){
        CardiologoService cardiologoService = retrofit.create(CardiologoService.class);
        Call<List<Cardiologo>> call = cardiologoService.obtenerCardiologos();
        call.enqueue(respuesta);
    }

    public static void insertarPaciente(Paciente paciente,Callback<Paciente> respuesta) {
        PacienteService pacienteService = retrofit.create(PacienteService.class);
        Call<Paciente> call = pacienteService.insertarPaciente(paciente);
        call.enqueue(respuesta);
    }

    public static Response<List<Prueba>> obtenerPruebas(String correo) throws IOException {
        PruebaService pruebaService = retrofit.create(PruebaService.class);
        Call<List<Prueba>> call = pruebaService.obtenerPruebas(correo);
        return call.execute();
    }

    public static void obtenerCardiologo(Cardiologo car,Callback<Cardiologo> callback) {
        CardiologoService cardiologoService = retrofit.create(CardiologoService.class);
        Call<Cardiologo> call = cardiologoService.obtenerCardiologo(car.idCardiologo);
        call.enqueue(callback);
    }

    public static Response<String> actualizarPaciente(Paciente paciente) throws IOException {
        PacienteService pacienteService = retrofit.create(PacienteService.class);
        Call<String> call = pacienteService.actualizarPaciente(paciente.idPaciente, paciente);
        return call.execute();
    }


    public static Response<Prueba> crearPrueba(MultipartBody.Part archivo,String prueba) throws IOException {
        PruebaService pruebaService = retrofit.create(PruebaService.class);
        Call<Prueba> call = pruebaService.generarPrueba(archivo, prueba);
        return call.execute();
    }

    public static Response<ResponseBody> descargarPrueba(int id) throws IOException {
        PruebaService pruebaService = retrofit.create(PruebaService.class);
        Call<ResponseBody> call = pruebaService.descargarPrueba(id);
        return call.execute();
    }
}
