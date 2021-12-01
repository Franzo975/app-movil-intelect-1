package pe.edu.usat.hmera.laboratorio.serviciosweb.logica;

import org.json.JSONObject;

import java.util.ArrayList;

public class Rubro {

    private int id;
    private String descripcion;
    private String sede;
    private String calculoxdia;
    private double monto;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getSede() {
        return sede;
    }

    public void setSede(String sede) {
        this.sede = sede;
    }

    public String getCalculoxdia() {
        return calculoxdia;
    }

    public void setCalculoxdia(String calculoxdia) {
        this.calculoxdia = calculoxdia;
    }

    public static ArrayList<Rubro> listaRubro = new ArrayList<>();

    public JSONObject getJSONRubro() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id_rubro",this.getId());
            jsonObject.put("calculoxdia",this.getCalculoxdia());
            jsonObject.put("descripcion",this.getDescripcion());
            jsonObject.put("cantidad_total",this.getMonto());
            jsonObject.put("sede",this.getSede());
        }catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
