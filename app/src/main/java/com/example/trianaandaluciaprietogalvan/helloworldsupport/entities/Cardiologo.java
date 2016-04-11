package com.example.trianaandaluciaprietogalvan.helloworldsupport.entities;

/**
 * Created by trianaandaluciaprietogalvan on 04/04/16.
 */
public class Cardiologo {

    public Integer idCardiologo;
    public String nombre;
    public String apellidoPaterno;
    public String apellidoMaterno;

    public String obtenerNombreCompleto(){
        return nombre+" "+apellidoPaterno+" "+apellidoMaterno;
    }

    @Override
    public String toString() {
        return "Cardiologo{" +
                "idCardiologo=" + idCardiologo +
                '}';
    }
}
