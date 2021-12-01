package pe.edu.usat.hmera.laboratorio.serviciosweb.logica;

import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class TipoComprobante {
    private int id_tipoComprobante;
    private String descripcion;

    public int getId_tipoComprobante() {
        return id_tipoComprobante;
    }

    public void setId_tipoComprobante(int id_tipoComprobante) {
        this.id_tipoComprobante = id_tipoComprobante;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public static ArrayList<TipoComprobante> listaTC = new ArrayList<>();
}
