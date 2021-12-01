package pe.edu.usat.hmera.laboratorio.serviciosweb;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import pe.edu.usat.hmera.laboratorio.serviciosweb.logica.Rubro;
import pe.edu.usat.hmera.laboratorio.serviciosweb.logica.Sesion;
import pe.edu.usat.hmera.laboratorio.serviciosweb.logica.TipoComprobante;
import pe.edu.usat.hmera.laboratorio.serviciosweb.util.Helper;

public class RegistrarComprobante extends AppCompatActivity implements View.OnClickListener {
    Spinner spRubro,spTipoComprobante;
    EditText txtDescipcionComprobante, txtSerieComprobante,txtCorrelativoComprobante,txtFechaEmision,txtMontoTotal;
    Button btnRegistrarComprobante;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_comprobante);
        spRubro = findViewById(R.id.spRubro);
        spTipoComprobante = findViewById(R.id.spTipoComprobante);
        txtDescipcionComprobante = findViewById(R.id.txtDescipcionComprobante);
        txtSerieComprobante = findViewById(R.id.txtSerieComprobante);
        txtCorrelativoComprobante = findViewById(R.id.txtCorrelativoComprobante);
        txtFechaEmision = findViewById(R.id.txtFechaEmision);
        txtMontoTotal = findViewById(R.id.txtMontoTotal);
        btnRegistrarComprobante = findViewById(R.id.btnRegistrarComprobante);
        btnRegistrarComprobante.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

    }
    public class RubroTask extends AsyncTask<Void, Void, Boolean> {

        private String sede;

        public RubroTask() {
            this.sede = "JAÉN";
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean resultado = false;
            try {
                String URL = Helper.BASE_URL_WS + "/rubro_x_sede/listar";
                HashMap<String, String> parametros = new HashMap<>();
                parametros.put("token", Sesion.TOKEN);
                parametros.put("sede", sede);
                String rubroJSON = new Helper().requestHttpPost(URL, parametros);
                JSONObject jsonObject = new JSONObject(rubroJSON);
                if (jsonObject.getBoolean("status")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    Rubro.listaRubro.clear();
                    for (int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonDatosRubro = jsonArray.getJSONObject(i);
                        Rubro objRubro = new Rubro();
                        objRubro.setId(jsonDatosRubro.getInt("id_rubro"));
                        objRubro.setDescripcion(jsonDatosRubro.getString("descripcion"));
                        objRubro.setMonto(jsonDatosRubro.getDouble("monto"));
                        objRubro.setCalculoxdia(jsonDatosRubro.getString("calculoxdia"));
                        objRubro.setSede(jsonDatosRubro.getString("sede"));
                        Rubro.listaRubro.add(objRubro);
                    }
                    resultado = true;
                }
            } catch (Exception e) {
                Toast.makeText(RegistrarComprobante.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            return resultado;
        }

        @Override
        protected void onPostExecute(Boolean resp) {
            super.onPostExecute(resp);
            if (resp){
                String[] values = new String[Rubro.listaRubro.size()];
                for (int el=0;el<Rubro.listaRubro.size();el++) {
                    values[el] = Rubro.listaRubro.get(el).getDescripcion();
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        getApplicationContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        values
                );
                spRubro.setAdapter(adapter);
            }
        }
    }

    private class TipoComprobanteTask extends AsyncTask<Void,Void,Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean respuesta = false;
            try {
                String URL = Helper.BASE_URL_WS + "/tipo_comprobante/list";
                HashMap<String, String> parametros = new HashMap<>();
                parametros.put("token", Sesion.TOKEN);
                String rubroJSON = new Helper().requestHttpPost(URL, parametros);
                JSONObject jsonObject = new JSONObject(rubroJSON);
                if (jsonObject.getBoolean("status")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    TipoComprobante.listaTC.clear();
                    for (int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonDatos = jsonArray.getJSONObject(i);
                        TipoComprobante objTC = new TipoComprobante();
                        objTC.setId_tipoComprobante(jsonDatos.getInt("id_tipo_comprobante"));
                        objTC.setDescripcion(jsonDatos.getString("descripcion"));
                        TipoComprobante.listaTC.add(objTC);
                    }
                    respuesta = true;
                }

            }catch (Exception e){
                Toast.makeText(RegistrarComprobante.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            return respuesta;
        }

        @Override
        protected void onPostExecute(Boolean respuesta) {
            super.onPostExecute(respuesta);
            if (respuesta){
                String[] tcLista = new String[TipoComprobante.listaTC.size()];
                for (int i=0; i < TipoComprobante.listaTC.size();i++){
                    tcLista[i] = TipoComprobante.listaTC.get(i).getDescripcion();
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        getApplicationContext(),
                        android.R.layout.simple_spinner_dropdown_item,
                        tcLista
                );
                spTipoComprobante.setAdapter(adapter);
            }
        }
    }

}