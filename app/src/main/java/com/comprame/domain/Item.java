package com.comprame.domain;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class Item implements Serializable {
    private String id;
    private String nombre;
    private String descripcion;
    private double precio_unitario;
    @SerializedName("foto_urls")
    private ArrayList<String> foto_urls = new ArrayList<>();
    private String vendedor;
    private String ubicacion_geografica;
    @SerializedName("metodos_pago")
    private ArrayList<String> metodos_pago;
    @SerializedName("categorias")
    private String categorias;

    public Item(String nombre, String descripcion, double precio_unitario,
                String vendedor, String ubicacion_geografica,
                ArrayList<String> metodos_pago, String categorias) {

        this.id = UUID.randomUUID().toString();
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio_unitario = precio_unitario;
        this.vendedor = vendedor;
        this.ubicacion_geografica = ubicacion_geografica;
        this.metodos_pago = metodos_pago;
        this.categorias = categorias;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio_unitario() {
        return precio_unitario;
    }

    public void setPrecio_unitario(double precio_unitario) {
        this.precio_unitario = precio_unitario;
    }

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }

    public String getUbicacion_geografica() {
        return ubicacion_geografica;
    }

    public void setUbicacion_geografica(String ubicacion_geografica) {
        this.ubicacion_geografica = ubicacion_geografica;
    }

    public void addFoto(String url) {
        this.foto_urls.add(url);
    }

    public String getFoto(int i) {
        if (i < this.foto_urls.size())
            return this.foto_urls.get(i);
        else
            return null;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public ArrayList<String> getMetodos_pago() {
        return metodos_pago;
    }

    public void setMetodos_pago(ArrayList<String> metodos_pago) {
        this.metodos_pago = metodos_pago;
    }

    public String getCategorias() {
        return categorias;
    }

    public void setCategorias(String categorias) {
        this.categorias = categorias;
    }
}
