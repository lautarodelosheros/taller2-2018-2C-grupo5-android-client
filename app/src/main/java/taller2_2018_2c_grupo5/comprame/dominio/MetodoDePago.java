package taller2_2018_2c_grupo5.comprame.dominio;

public class MetodoDePago {
    private String tipo;
    private String marca;
    private int cuotas;

    public MetodoDePago(String tipo, String marca, int cuotas) {
        this.tipo = tipo;
        this.marca = marca;
        this.cuotas = cuotas;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public int getCuotas() {
        return cuotas;
    }

    public void setCuotas(int cuotas) {
        this.cuotas = cuotas;
    }
}
