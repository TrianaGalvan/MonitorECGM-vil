 package com.example.trianaandaluciaprietogalvan.helloworldsupport.entities;

 import java.io.Serializable;

 public class Paciente implements Serializable{

    public Integer idPaciente;
    public String nombre;
    public String apellidoPaterno;
    public String apellidoMaterno;
    public Character sexo;
    public Integer edad;
    public String curp;
    public String correo;
    public String telefono;
    public String contrasena;
    public Integer peso;
    public Integer presionSistolica;
    public Integer presionDiastolica;
    public double imc;
    public Integer frecuenciaRespiratoria;
    public Double altura;
    public String fechamodificacion;
    public Cardiologo cardiologo;

    @Override
    public String toString() {
        return "Paciente{" +
                "idPaciente=" + idPaciente +
                ", nombre='" + nombre + '\'' +
                ", apellidoPaterno='" + apellidoPaterno + '\'' +
                ", apellidoMaterno='" + apellidoMaterno + '\'' +
                ", sexo='" + sexo + '\'' +
                ", edad=" + edad +
                ", curp='" + curp + '\'' +
                ", correo='" + correo + '\'' +
                ", telefono='" + telefono + '\'' +
                ", contrasena='" + contrasena + '\'' +
                ", peso=" + peso +
                ", presionSistolica=" + presionSistolica +
                ", presionDiastolica=" + presionDiastolica +
                ", imc=" + imc +
                ", frecuenciaRespiratoria=" + frecuenciaRespiratoria +
                ", altura=" + altura +
                ", fechamodificacion='" + fechamodificacion + '\'' +
                ", cardiologo=" + cardiologo +
                '}';
    }
}
