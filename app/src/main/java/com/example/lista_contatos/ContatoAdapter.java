package com.example.lista_contatos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ContatoAdapter extends BaseAdapter {
    private Context context;
    private List<Contato> contatos;

    public ContatoAdapter(Context context, List<Contato> contatos) {
        this.context = context;
        this.contatos = contatos;
    }

    @Override
    public int getCount(){
        return contatos.size();
    }

    @Override
    public Object getItem(int position) {
        return contatos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context)
                .inflate(R.layout.contato_item, parent, false);

        Contato contato = contatos.get(position);

        TextView txtNome = convertView.findViewById(R.id.txtNome);
        TextView txtNumero = convertView.findViewById(R.id.txtNumero);
        ImageView imvFavoritoN = convertView.findViewById(R.id.imvFavorito);
        ImageView imvFavoritoY = convertView.findViewById(R.id.imvFavorito2);


        txtNome.setText(contato.getName());
        txtNumero.setText(contato.getNumeroCelular());

        if(contato.isFavorito()){
            imvFavoritoY.setVisibility(View.VISIBLE);
        }
        else{
            imvFavoritoN.setVisibility(View.VISIBLE);
        }


        return convertView;
    }

}
