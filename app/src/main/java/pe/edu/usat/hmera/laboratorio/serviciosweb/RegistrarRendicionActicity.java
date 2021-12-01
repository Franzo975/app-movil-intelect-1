package pe.edu.usat.hmera.laboratorio.serviciosweb;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import pe.edu.usat.hmera.laboratorio.serviciosweb.logica.Anticipo;
import pe.edu.usat.hmera.laboratorio.serviciosweb.logica.Sesion;
import pe.edu.usat.hmera.laboratorio.serviciosweb.util.Helper;

public class RegistrarRendicionActicity extends AppCompatActivity implements View.OnClickListener {
    Spinner spAnticipo;
    TextView txtDocente, txtGastoAlimentacion, txtGastoHotel, txtGastoMovilidadInterna,txtGastoPasajesTerrestres,txtDevolucion,txtFaltante;
    Button btnAgregarComprobante,btnRegistrarRendicion;
    RecyclerView comprobanteRecyclerVew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("REGISTRAR RENDICION ANTICIPO");
        setContentView(R.layout.activity_registrar_rendicion_activity);
        spAnticipo = findViewById(R.id.spAnticipo);

        txtDocente = findViewById(R.id.txtDocente);
        txtGastoAlimentacion = findViewById(R.id.txtGastoAlimentacion);
        txtGastoHotel = findViewById(R.id.txtGastoHotel);
        txtGastoMovilidadInterna = findViewById(R.id.txtGastoMovilidadInterna);
        txtGastoPasajesTerrestres = findViewById(R.id.txtGastoPasajesTerrestres);
        txtDevolucion = findViewById(R.id.txtDevolucion);
        txtFaltante = findViewById(R.id.txtFaltante);

        btnAgregarComprobante = findViewById(R.id.btnAgregarComprobante);
        btnAgregarComprobante.setOnClickListener(this);
        btnRegistrarRendicion = findViewById(R.id.btnRegistrarRendicion);
        btnRegistrarRendicion.setOnClickListener(this);

        comprobanteRecyclerVew = findViewById(R.id.comprobanteRecyclerVew);
        comprobanteRecyclerVew.setHasFixedSize(true);
        comprobanteRecyclerVew.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnAgregarComprobante:
                Intent intent = new Intent(this,RegistrarComprobante.class);
                startActivity(intent);
                break;
            case R.id.btnRegistrarRendicion:
                registrarRendicion();
        }
    }
    private void registrarRendicion(){

    }
    private class AnticipoTask extends AsyncTask<Void,Void, Boolean>{
        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean respuesta = false;
            try {
                String URL_WS_ANTICIPO = Helper.BASE_URL_WS + "/anticipo/list";
                HashMap<String, String> parametros = new HashMap<>();
                parametros.put("token", Sesion.TOKEN);
                parametros.put("id_usuario", String.valueOf(Sesion.ID));
                String response = new Helper().requestHttpPost(URL_WS_ANTICIPO,parametros);
                JSONObject result = new JSONObject(response);
                if (result.getBoolean("ok")) {
                    JSONArray jsonArray = result.getJSONArray("anticipos");
                    Anticipo.listaAnticipo.clear();
                    for (int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonAnticipo = jsonArray.getJSONObject(i);
                        Anticipo objAnticipo = new Anticipo();
                        objAnticipo.setId(jsonAnticipo.getInt("id_anticipo"));
                        objAnticipo.setDescripcion(jsonAnticipo.getString("descripcion"));
                        objAnticipo.setEstado(jsonAnticipo.getString("estado"));
                        objAnticipo.setFechaInicio(jsonAnticipo.getString("fecha_inicio"));
                        objAnticipo.setFechaFin(jsonAnticipo.getString("fecha_fin"));
                        objAnticipo.setMotivo(jsonAnticipo.getString("motivo"));
                        objAnticipo.setUsuario(jsonAnticipo.getString("docente"));
                        objAnticipo.setSede(jsonAnticipo.getString("sede"));
                        objAnticipo.setTotal(jsonAnticipo.getInt("total"));
                        Anticipo.listaAnticipo.add(objAnticipo);
                    }
                    respuesta = true;
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        return respuesta;}

        @Override
        protected void onPostExecute(Boolean respuesta) {
            super.onPostExecute(respuesta);
            if (respuesta){
                String[] anticipo = new String[Anticipo.listaAnticipo.size()];
                for (int i=0;i < Anticipo.listaAnticipo.size();i++){
                    Anticipo a = Anticipo.listaAnticipo.get(i);
                    if (a.getEstado().equalsIgnoreCase("APROBADO")) {
                        anticipo[i] = a.getId() + " - " + a.getMotivo() +" - "+a.getFechaInicio()+ " - "+a.getFechaFin();
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        getApplicationContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        anticipo
                );
                spAnticipo.setAdapter(adapter);
            }
        }
    }
}