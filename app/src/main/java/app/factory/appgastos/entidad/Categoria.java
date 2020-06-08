package app.factory.appgastos.entidad;

import androidx.annotation.NonNull;

public class Categoria {

    private int idCategoria;
    private String categoria;
    private String estado;

    private double MontoMovimientoCategoria;

    public double getMontoMovimientoCategoria() {
        return MontoMovimientoCategoria;
    }

    public void setMontoMovimientoCategoria(double montoMovimientoCategoria) {
        MontoMovimientoCategoria = montoMovimientoCategoria;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @NonNull
    @Override
    public String toString() {
        return this.categoria;
    }
}
