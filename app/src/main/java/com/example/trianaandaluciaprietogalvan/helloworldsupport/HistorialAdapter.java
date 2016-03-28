package com.example.trianaandaluciaprietogalvan.helloworldsupport;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by trianaandaluciaprietogalvan on 01/03/16.
 */
public class HistorialAdapter extends ArrayAdapter<HistorialItem> {
    Context context;
    int layoutResourceId;
    HistorialItem data[] = null;

    public HistorialAdapter(Context context, int layoutResourceId, HistorialItem[] data) {
        super(context,layoutResourceId,data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
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

        HistorialItem item = data[position];
        holder.txtFecha.setText(item.fecha);
        String status = item.status;
        if(status.equals("Pendiente")){
            holder.txtStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.pendiente));
            holder.txtStatus.setPadding(18, 3, 17, 3);
        }else if (status.equals("Revisado")){
            holder.txtStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.revisado));
            holder.txtStatus.setPadding(20, 3, 20, 3);
        }else if (status.equals("No revisado")){
            holder.txtStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.no_revisado));
            holder.txtStatus.setPadding(8,3,8,3);
        }
        holder.txtStatus.setText(item.status);

        return row;
    }

    static class HistorialHolder
    {
        TextView txtFecha;
        TextView txtStatus;
    }
}
