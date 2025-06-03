package com.example.lista_contatos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Contato implements Serializable {
    private int id;
    private String name;
    @SerializedName("phone")
    private String numeroCelular;
    @SerializedName("favorite")
    private boolean favorito;

    public Contato(int id, String name, String numeroCelular, boolean favorito) {
        this.id = id;
        this.name = name;
        this.numeroCelular = numeroCelular;
        this.favorito = favorito;
    }
    public Contato(String name, String numeroCelular, boolean favorito) {
        this.name = name;
        this.numeroCelular = numeroCelular;
        this.favorito = favorito;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumeroCelular() {
        return numeroCelular;
    }

    public void setNumeroCelular(String numeroCelular) {
        this.numeroCelular = numeroCelular;
    }

    public boolean isFavorito() {
        return favorito;
    }

    public void setFavorito(boolean favorito) {
        this.favorito = favorito;
    }
}
