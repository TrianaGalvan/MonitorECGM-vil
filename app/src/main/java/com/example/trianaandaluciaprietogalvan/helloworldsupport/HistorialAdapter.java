package com.example.trianaandaluciaprietogalvan.helloworldsupport;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.trianaandaluciaprietogalvan.helloworldsupport.entities.Prueba;

import java.util.List;

/**
 * Created by trianaandaluciaprietogalvan on 01/03/16.
 */
public class HistorialAdapter extends ArrayAdapter<Prueba> {
    Context context;
    int layoutResourceId;
    List<Prueba> pruebas= null;

    public HistorialAdapter(Context context, int layoutResourceId) {
        super(context,layoutResourceId);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
    }

    public void setPruebas(List<Prueba> pruebas) {
        this.pruebas = pruebas;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        HistorialHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new HistorialHolder();
            holder.txtFecha = (TextView)row.findViewById(R.id.txtFecha);
            holder.txtStatus = (TextView)row.findViewById(R.id.txtStatus);
            row.setTag(holder);
        }
        else
        {
            holder = (HistorialHolder)row.getTag();
        }

        Prueba pr = pruebas.get(position);
        holder.txtFecha.setText(pr.fecha);
        int status = pr.reporte.estatus;
        if(status == 1){
            holder.txtStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.pendiente));
            holder.txtStatus.setPadding(18, 3, 17, 3);
        }else if (status == 0){
            holder.txtStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.revisado));
            holder.txtStatus.setPadding(20, 3, 20, 3);
        }else if (status == 2){
            holder.txtStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.no_revisado));
            holder.txtStatus.setPadding(8,3,8,3);
        }
        holder.txtStatus.setText(obtenerEstatusCadena(status));

        return row;
    }

    @Override
    public int getCount() {
        if(pruebas != null){
            return pruebas.size();
        }else{
            return 0;
        }
    }

    @Override
    public long getItemId(int position) {
        return pruebas.get(position).idPrueba;
    }

    static class HistorialHolder
    {
        TextView txtFecha;
        TextView txtStatus;
    }

    public String obtenerEstatusCadena(int status){
        if(status == 0){
            return "Revisado";
        }else if(status == 1){
            return "Pendiente";
        }else if (status == 2){
            return "No revisado";
        }else{
            return "";
        }
    }
}
