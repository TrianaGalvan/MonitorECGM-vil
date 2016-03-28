package com.example.trianaandaluciaprietogalvan.helloworldsupport;

/**
 * Created by trianaandaluciaprietogalvan on 01/03/16.
 */
public class HistorialItem {
    public String fecha;
    public String status;
    public HistorialItem(){
        super();
    }

    public HistorialItem(String fecha, String status) {
        super();
        this.fecha = fecha;
        this.status = status;
    }

    public String getFecha(){
        return this.fecha;
    }

    public String getStatus(){
        return this.status;
    }
}
