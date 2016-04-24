package com.example.trianaandaluciaprietogalvan.helloworldsupport;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by trianaandaluciaprietogalvan on 20/04/16.
 */
public class ECGSAdapter extends ArrayAdapter<String> {
    Context context;
    int layoutResourceId;
    List<String> dispositivos = null;

    public ECGSAdapter(Context context, int layoutResourceId) {
        super(context, layoutResourceId);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
    }

    public void setDispositivos(List<String> disp) {
        this.dispositivos = disp;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ECGHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ECGHolder();
            holder.dispositivo = (TextView) row.findViewById(R.id.rowDispositivo);
            row.setTag(holder);
        } else {
            holder = (ECGHolder) row.getTag();
        }

        String nom = dispositivos.get(position);
        holder.dispositivo.setText(nom);
        return row;
    }

    @Override
    public int getCount() {
        if (dispositivos != null) {
            return dispositivos.size();
        } else {
            return 0;
        }
    }

    static class ECGHolder {
        TextView dispositivo;
    }


    @Override
    public String getItem(int position) {
        return this.dispositivos.get(position);
    }
}