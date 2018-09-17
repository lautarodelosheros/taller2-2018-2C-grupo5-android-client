package taller2_2018_2c_grupo5.comprame.dominio;

import java.util.ArrayList;

public class Item {
    private String nombre;
    private String descripcion;
    private double precioUnitario;
    private ArrayList<String> fotos = new ArrayList<>();
    private String vendedor;
    private String ubicacionGeografica;
    private ArrayList<MetodoDePago> metodosDePago;
    private ArrayList<String> categorias;

    public Item(String nombre, String descripcion, double precioUnitario,
                String vendedor, String ubicacionGeografica,
                ArrayList<MetodoDePago> metodosDePago, ArrayList<String> categorias) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioUnitario = precioUnitario;
        this.vendedor = vendedor;
        this.ubicacionGeografica = ubicacionGeografica;
        this.metodosDePago = metodosDePago;
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

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }

    public String getUbicacionGeografica() {
        return ubicacionGeografica;
    }

    public void setUbicacionGeografica(String ubicacionGeografica) {
        this.ubicacionGeografica = ubicacionGeografica;
    }

    public void addFoto(String url) {
        this.fotos.add(url);
    }

    public void addMetodoDePago(MetodoDePago metodoDePago) {
        this.metodosDePago.add(metodoDePago);
    }

    public void addCategoria(String categoria) {
        this.categorias.add(categoria);
    }

    public String getFoto(int i) {
        if (i < this.fotos.size())
            return this.fotos.get(i);
        else
            return null;
    }
}
