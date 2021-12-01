package pe.edu.usat.hmera.laboratorio.serviciosweb;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import pe.edu.usat.hmera.laboratorio.serviciosweb.adaptador.AdaptadorAnticipo;
import pe.edu.usat.hmera.laboratorio.serviciosweb.adaptador.AdaptadorHistorialAnticipo;
import pe.edu.usat.hmera.laboratorio.serviciosweb.logica.HistorialAnticipo;
import pe.edu.usat.hmera.laboratorio.serviciosweb.logica.Sesion;
import pe.edu.usat.hmera.laboratorio.serviciosweb.util.Helper;


public class HistorialAnticipoFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    AdaptadorHistorialAnticipo adaptadorHistorialAnticipo;
    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;

    public HistorialAnticipoFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Helper.configurarAlmacenamientoCache(this.getContext());
        Helper.habilitarDirectivasInternetX();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_historial_anticipo, container, false);
        recyclerView = view.findViewById(R.id.historialAnticipoRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayoutHistorialAnticipo);
        swipeRefreshLayout.setColorScheme(android.R.color.holo_red_light, android.R.color.holo_green_light, android.R.color.holo_blue_light);
        swipeRefreshLayout.setOnRefreshListener(this);
        listarHistorialAnticipo();
        return view;
    }

    @Override
    public void onRefresh() {

    }

    private class HistorialAnticipoTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean resultado = false;
            try {
                String URL_WS_ANTICIPO = Helper.BASE_URL_WS + "/evaluacion_anticipo/list";
                HashMap<String, String> parametros = new HashMap<>();
                parametros.put("token", Sesion.TOKEN);
                parametros.put("id_usuario", String.valueOf(Sesion.ID));
                parametros.put("id_anticipo", String.valueOf(AdaptadorAnticipo.posicionItemSeleccionado));
                String response = new Helper().requestHttpPost(URL_WS_ANTICIPO,parametros);
                JSONObject result = new JSONObject(response);
                System.out.println(result);
                if (result.getBoolean("ok")) {
                    JSONArray jsonArray = result.getJSONArray("evaluacion_anticipos");
                    HistorialAnticipo.listaHistorial.clear();
                    for (int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonHistorialAnticipo = jsonArray.getJSONObject(i);
                        HistorialAnticipo objAnticipo = new HistorialAnticipo();
                        objAnticipo.setId(jsonHistorialAnticipo.getInt("id_evaluacion"));
                        objAnticipo.setDescripcionAnticipo(jsonHistorialAnticipo.getString("anticipo"));
                        objAnticipo.setEstado(jsonHistorialAnticipo.getString("estado"));
                        objAnticipo.setFechaRegistro(jsonHistorialAnticipo.getString("fecha_registro"));
                        objAnticipo.setMotivo(jsonHistorialAnticipo.getString("motivo"));
                        objAnticipo.setObservacion(jsonHistorialAnticipo.getString("observacion"));
                        objAnticipo.setPersonal(jsonHistorialAnticipo.getString("personal"));
                        objAnticipo.setSede(jsonHistorialAnticipo.getString("sede"));
                        objAnticipo.setTipoPersonal(jsonHistorialAnticipo.getString("tipo_personal"));
                        HistorialAnticipo.listaHistorial.add(objAnticipo);
                    }
                    resultado = true;
                } else {
                    resultado = false;
                }
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
            return resultado;
        }

        @Override
        protected void onPostExecute(Boolean resultado) {
            super.onPostExecute(resultado);
            if (resultado) {
                adaptadorHistorialAnticipo.cargarHistorialAnticipos(HistorialAnticipo.listaHistorial);
            } else {
                Helper.mensajeInformacion(getContext(),"INFORMACION","NO SE ENCONTRARON RESULTADOS! EL ANTICIPO AUN NO HA SIDO EVALUADO");
            }
            progressDialog.dismiss();
        }
    }

    private void listarHistorialAnticipo() {
        progressDialog = new ProgressDialog(this.getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Descargando historial anticipos! Espere por favor.");
        progressDialog.setCancelable(false);
        progressDialog.show();
        adaptadorHistorialAnticipo = new AdaptadorHistorialAnticipo(this.getContext());
        recyclerView.setAdapter(adaptadorHistorialAnticipo);
        new HistorialAnticipoTask().execute();
    }
}