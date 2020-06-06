package app.factory.appgastos.entidad;

import androidx.annotation.NonNull;

import java.util.Date;

public class Entidad {
    private int idEntidad;
    private String entidad;
    private Date fechaRegistro;
    private String estado;

    public int getIdEntidad() {
        return idEntidad;
    }

    public void setIdEntidad(int idEntidad) {
        this.idEntidad = idEntidad;
    }

    public String getEntidad() {
        return entidad;
    }

    public void setEntidad(String entidad) {
        this.entidad = entidad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    @NonNull
    @Override
    public String toString() {
        return entidad;
    }
}
