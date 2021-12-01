package pe.edu.usat.hmera.laboratorio.serviciosweb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

import java.util.ArrayList;
import java.util.HashMap;

import pe.edu.usat.hmera.laboratorio.serviciosweb.adaptador.AdaptadorAnticipo;
import pe.edu.usat.hmera.laboratorio.serviciosweb.logica.Anticipo;
import pe.edu.usat.hmera.laboratorio.serviciosweb.logica.Sesion;
import pe.edu.usat.hmera.laboratorio.serviciosweb.util.Helper;


public class AnticipoFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    ArrayList<Anticipo> listaAnticiposTemporal,listaEstadoAnticipo;
    AdaptadorAnticipo adaptadorAnticipo;
    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    FloatingActionButton btnRegistrarAnticipo;
    SwipeRefreshLayout swipeRefreshLayout;

    public AnticipoFragment() {
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
        View view = inflater.inflate(R.layout.fragment_anticipo, container, false);
        recyclerView = view.findViewById(R.id.anticipoRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        if (Sesion.ID == 2) {
            btnRegistrarAnticipo = view.findViewById(R.id.btnRegistrarAnticipo);
            btnRegistrarAnticipo.setVisibility(View.VISIBLE);
            btnRegistrarAnticipo.setOnClickListener(this);
        }

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayoutCatalogoProducto);
        swipeRefreshLayout.setColorScheme(android.R.color.holo_red_light, android.R.color.holo_green_light, android.R.color.holo_blue_light);
        swipeRefreshLayout.setOnRefreshListener(this);
        listarAnticipos();
        return view;
    }

    @Override
    public void onRefresh() {
        this.listarAnticipos();
        this.swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRegistrarAnticipo:
                int estadosRestrictores = 0;
                for (Anticipo a : AdaptadorAnticipo.listaAnticipo){
                    if (a.getEstado().equalsIgnoreCase("Registrado") || a.getEstado().equalsIgnoreCase("Observado") || a.getEstado().equalsIgnoreCase("Rendido") ) {
                        estadosRestrictores++;
                    }
                }
                if (estadosRestrictores > 0)
                    Helper.mensajeError(this.getActivity(),"Atención","Usted no puede registrar otro anticipo debido a que tiene uno en proceso");
                else{
                    Intent intent = new Intent(this.getActivity(), RegistrarAnticipoActivity.class);
                    startActivity(intent);
                }

        }
    }

    private class AnticipoTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean resultado = false;
            try {
                String URL_WS_ANTICIPO = Helper.BASE_URL_WS + "/anticipo/list";
                HashMap<String, String> parametros = new HashMap<>();
                parametros.put("token", Sesion.TOKEN);
                parametros.put("id_usuario", String.valueOf(Sesion.ID));
                String response = new Helper().requestHttpPost(URL_WS_ANTICIPO,parametros);
                JSONObject result = new JSONObject(response);
                if (result.getBoolean("ok")) {
                    JSONArray jsonArray = result.getJSONArray("anticipos");
                    listaAnticiposTemporal.clear();
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
                        listaAnticiposTemporal.add(objAnticipo);
                        Anticipo.listaAnticipo.add(objAnticipo);
                    }
                    resultado = true;
                } else {
                    resultado = false;
                }
            } catch (JSONException jsonException) {

            }
            return resultado;
        }

        @Override
        protected void onPostExecute(Boolean resultado) {
            super.onPostExecute(resultado);
            if (resultado) {
                adaptadorAnticipo.cargarAnticipos(listaAnticiposTemporal);
                progressDialog.dismiss();
            }
        }
    }

    private void listarAnticipos() {
        progressDialog = new ProgressDialog(this.getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Descargando anticipos! Espere por favor.");
        progressDialog.setCancelable(false);
        progressDialog.show();
        listaAnticiposTemporal = new ArrayList<>();
        adaptadorAnticipo = new AdaptadorAnticipo(this.getContext());
        recyclerView.setAdapter(adaptadorAnticipo);
        new AnticipoTask().execute();
    }

}