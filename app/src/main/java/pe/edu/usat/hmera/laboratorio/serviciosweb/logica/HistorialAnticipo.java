package pe.edu.usat.hmera.laboratorio.serviciosweb.logica;

import java.util.ArrayList;

public class HistorialAnticipo {

    public int id;
    public String DescripcionAnticipo;
    public String Estado;
    public String FechaRegistro;
    public String Motivo;
    public String Observacion;
    public String Personal;
    public String Sede;
    public String TipoPersonal;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcionAnticipo() {
        return DescripcionAnticipo;
    }

    public void setDescripcionAnticipo(String descripcionAnticipo) {
        DescripcionAnticipo = descripcionAnticipo;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        Estado = estado;
    }

    public String getFechaRegistro() {
        return FechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        FechaRegistro = fechaRegistro;
    }

    public String getMotivo() {
        return Motivo;
    }

    public void setMotivo(String motivo) {
        Motivo = motivo;
    }

    public String getObservacion() {
        return Observacion;
    }

    public void setObservacion(String observacion) {
        Observacion = observacion;
    }

    public String getPersonal() {
        return Personal;
    }

    public void setPersonal(String personal) {
        Personal = personal;
    }

    public String getSede() {
        return Sede;
    }

    public void setSede(String sede) {
        Sede = sede;
    }

    public String getTipoPersonal() {
        return TipoPersonal;
    }

    public void setTipoPersonal(String tipoPersonal) {
        TipoPersonal = tipoPersonal;
    }

    public static ArrayList<HistorialAnticipo> listaHistorial = new ArrayList<>();

}
