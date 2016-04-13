package com.example.trianaandaluciaprietogalvan.helloworldsupport.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.trianaandaluciaprietogalvan.helloworldsupport.R;
import com.example.trianaandaluciaprietogalvan.helloworldsupport.entities.Cardiologo;

import java.util.List;

/**
 * Created by trianaandaluciaprietogalvan on 09/04/16.
 */
public class SpinnerCardiologosAdapter extends ArrayAdapter<Cardiologo> {
    List<Cardiologo> listaCardiologos;
    Context context;

    public SpinnerCardiologosAdapter(Context contexto) {
        super(contexto,-1);
        this.context = contexto;
    }

    public void setListaCardiologos(List<Cardiologo> listaCardiologos) {
        this.listaCardiologos = listaCardiologos;
        notifyDataSetChanged();
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        CardiologoHolder ch;
        if(view == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.cardiologo_item, parent, false);
            ch = new CardiologoHolder();
            ch.nombreCardiologo = (TextView) view.findViewById(R.id.txtCardiologo);
            view.setTag(ch);
        }
        else{
            ch = (CardiologoHolder) view.getTag();
        }

        Cardiologo cardiologo = listaCardiologos.get(position);
        ch.nombreCardiologo.setText(cardiologo.obtenerNombreCompleto());
        return view;
    }



    @Override
    public int getCount() {
        return listaCardiologos == null ? 0 : listaCardiologos.size();
    }

    @Override
    public Cardiologo getItem(int position) {
        return listaCardiologos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listaCardiologos.get(position).idCardiologo;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        CardiologoHolder ch;
        if(view == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.cardiologo_view_spinner, parent, false);
            ch = new CardiologoHolder();
            ch.nombreCardiologo = (TextView) view.findViewById(R.id.txtCardiologo);
            view.setTag(ch);
        }
        else{
            ch = (CardiologoHolder) view.getTag();
        }

        Cardiologo cardiologo = listaCardiologos.get(position);
        ch.nombreCardiologo.setText(cardiologo.obtenerNombreCompleto());
        return view;
    }


    static class CardiologoHolder{
        TextView nombreCardiologo;
    }
}
