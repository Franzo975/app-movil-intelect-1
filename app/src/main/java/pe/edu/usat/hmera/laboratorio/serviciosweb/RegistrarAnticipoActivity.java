package pe.edu.usat.hmera.laboratorio.serviciosweb;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import pe.edu.usat.hmera.laboratorio.serviciosweb.logica.Rubro;
import pe.edu.usat.hmera.laboratorio.serviciosweb.logica.Sede;
import pe.edu.usat.hmera.laboratorio.serviciosweb.logica.Motivo;
import pe.edu.usat.hmera.laboratorio.serviciosweb.logica.Sesion;
import pe.edu.usat.hmera.laboratorio.serviciosweb.util.Helper;
import pe.edu.usat.hmera.laboratorio.serviciosweb.util.Pickers;

public class RegistrarAnticipoActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    Spinner spSedes, spMotivos;
    EditText txtFechaInicio, txtFechaFin;
    ImageView imgFechaInicio, imgFechaFin;
    TextView txtPasajesTerrestres, txtAlimentacion, txtHotel, txtMovilidad,txtDescripcion;
    ArrayList<Rubro> lista;
    Button btnRegistrarAnticipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_anticipo);
        this.setTitle("REGISTRAR ANTICIPO");
        lista = new ArrayList<>();
        spSedes = findViewById(R.id.spSede);
        spSedes.setOnItemSelectedListener(this);
        spMotivos = findViewById(R.id.spMotivo);
        txtFechaFin = findViewById(R.id.txtFechaFin);
        txtFechaInicio = findViewById(R.id.txtFechaInicio);
        imgFechaInicio = findViewById(R.id.imgFechaInicio);
        imgFechaInicio.setOnClickListener(this);
        imgFechaFin = findViewById(R.id.imgFechaFin);
        imgFechaFin.setOnClickListener(this);
        txtAlimentacion = findViewById(R.id.txtAlimentacion);
        txtHotel = findViewById(R.id.txtHotel);
        txtMovilidad = findViewById(R.id.txtMovilidad);
        txtPasajesTerrestres = findViewById(R.id.txtPasajeTerrestre);
        txtDescripcion = findViewById(R.id.txtDescripcion);
        btnRegistrarAnticipo = findViewById(R.id.btnRegistrarAnticipo);
        btnRegistrarAnticipo.setOnClickListener(this);
        new SedeTask().execute();
        new MotivoTask().execute();

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgFechaInicio:
                Pickers.obtenerFecha(this,txtFechaInicio);
                break;
            case R.id.imgFechaFin:
                Pickers.obtenerFecha(this,txtFechaFin);
                break;
            case R.id.btnRegistrarAnticipo:
                RegistrarAnticipo();
                break;
        }
    }



    public class SedeTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean resultado = false;
            try {
                String URL = Helper.BASE_URL_WS + "/sede/listar";
                HashMap<String, String> parametros = new HashMap<>();
                parametros.put("token", Sesion.TOKEN);
                String sedeJSON = new Helper().requestHttpPost(URL, parametros);
                JSONObject jsonObject = new JSONObject(sedeJSON);
                if (jsonObject.getBoolean("status")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    Sede.listaSede.clear();
                    for (int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonDatosSede = jsonArray.getJSONObject(i);
                        if (!jsonDatosSede.getString("nombre").equalsIgnoreCase("CHICLAYO")) {
                            Sede objSede = new Sede();
                            objSede.setId(jsonDatosSede.getInt("id_sede"));
                            objSede.setDescripcion(jsonDatosSede.getString("nombre"));
                            Sede.listaSede.add(objSede);
                        }
                    }
                    resultado = true;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return resultado;
        }

        @Override
        protected void onPostExecute(Boolean resultado) {
            super.onPostExecute(resultado);
            if (resultado) {
                String sede[] = new String[Sede.listaSede.size()];
                for (int i=0;i<Sede.listaSede.size();i++) {
                    sede[i] = Sede.listaSede.get(i).getDescripcion();
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, sede);
                spSedes.setAdapter(adapter);
            }
        }
    }

    public class MotivoTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean resultado = false;
            try {
                String URL = Helper.BASE_URL_WS + "/motivo/listar";
                HashMap<String, String> parametros = new HashMap<>();
                parametros.put("token", Sesion.TOKEN);
                String motivoJSON = new Helper().requestHttpPost(URL, parametros);
                JSONObject jsonObject = new JSONObject(motivoJSON);
                if (jsonObject.getBoolean("status")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    Motivo.listaMotivo.clear();
                    for (int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonDatosMotivo = jsonArray.getJSONObject(i);
                        Motivo objMotivo = new Motivo();
                        objMotivo.setId(jsonDatosMotivo.getInt("id_motivo"));
                        objMotivo.setDescripcion(jsonDatosMotivo.getString("descripcion"));
                        Motivo.listaMotivo.add(objMotivo);
                    }
                    resultado = true;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return resultado;
        }

        @Override
        protected void onPostExecute(Boolean resultado) {
            super.onPostExecute(resultado);
            if (resultado) {
                String motivo[] = new String[Motivo.listaMotivo.size()];
                for (int i=0;i<Motivo.listaMotivo.size();i++) {
                    motivo[i] = Motivo.listaMotivo.get(i).getDescripcion();
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, motivo);
                spMotivos.setAdapter(adapter);
            }
        }
    }

    public class RubroTask extends AsyncTask<Void, Void, Boolean> {

        private String sede;

        public RubroTask(String sede) {
            this.sede = sede;
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
                System.out.println(e.getMessage());
            }
            return resultado;
        }

        @Override
        protected void onPostExecute(Boolean resultado) {
            super.onPostExecute(resultado);
            if (resultado) {
                Double[] values = new Double[Rubro.listaRubro.size()];
                for (int el=0;el<Rubro.listaRubro.size();el++) {
                    values[el] = Rubro.listaRubro.get(el).getMonto();
                    lista.add(Rubro.listaRubro.get(el));
                }
                txtPasajesTerrestres.setText("PASAJES TERRESTRES: S/. "+values[0]);
                txtAlimentacion.setText("ALIMENTACIÓN: S/. "+values[1]);
                txtHotel.setText("HOTEL: S/. "+values[2]);
                txtMovilidad.setText("MOVILIDAD: S/."+values[3]);
            }
        }
    }

    private static String mensajeAnticipoRegistrado;

    public class RegistrarAnticipoTask extends AsyncTask<Void, Void, Boolean> {
        private String descripcion;
        private String fechaInicio;
        private String fechaFin;
        private String idUsuario;
        private String idMotivo;
        private String idSede;
        private String rubros;

        public RegistrarAnticipoTask(String descripcion, String fechaInicio, String fechaFin, String idUsuario, String idMotivo, String idSede, String rubros) {
            this.descripcion = descripcion;
            this.fechaInicio = fechaInicio;
            this.fechaFin = fechaFin;
            this.idUsuario = idUsuario;
            this.idMotivo = idMotivo;
            this.idSede = idSede;
            this.rubros = rubros;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean resultado = false;
            try{
                String URL = Helper.BASE_URL_WS + "/anticipo/registrar";
                HashMap<String, String> parametros = new HashMap<>();
                parametros.put("token", Sesion.TOKEN);
                parametros.put("descripcion", this.descripcion);
                parametros.put("fechaInicio", this.fechaInicio);
                parametros.put("fechaFin", this.fechaFin);
                parametros.put("idUsuario", this.idUsuario);
                parametros.put("idMotivo", this.idMotivo);
                parametros.put("idSede", this.idSede);
                parametros.put("rubros", this.rubros);
                String response = new Helper().requestHttpPost(URL, parametros);
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getBoolean("status")) {
                    RegistrarAnticipoActivity.mensajeAnticipoRegistrado = jsonObject.getString("data");
                    resultado = true;
                } else {
                    resultado = false;
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
            return resultado;
        }

        @Override
        protected void onPostExecute(Boolean resultado) {
            super.onPostExecute(resultado);
            if (resultado) {
                Helper.mensajeInformacion(RegistrarAnticipoActivity.this, "ANTICIPO", RegistrarAnticipoActivity.mensajeAnticipoRegistrado);
                RegistrarAnticipoActivity.this.finish();
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        switch (adapterView.getId()) {
            case R.id.spSede:
                new RubroTask(spSedes.getSelectedItem().toString()).execute();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    private void RegistrarAnticipo() {
        JSONArray jsonArray = new JSONArray();
        for (Rubro rubro: Rubro.listaRubro) {
            jsonArray.put(rubro.getJSONRubro());
        }
        final String descripcion = txtDescripcion.getText().toString();
        final String fechaInicio = Helper.formatearDMA_to_AMD(this.txtFechaInicio.getText().toString());
        final String fechaFin = Helper.formatearDMA_to_AMD(this.txtFechaFin.getText().toString());
        final String idUsuario = String.valueOf(Sesion.ID);
        final String idMotivo = String.valueOf(Motivo.listaMotivo.get(spMotivos.getSelectedItemPosition()).getId());
        final String idSede = String.valueOf(Sede.listaSede.get(spSedes.getSelectedItemPosition()).getId());
        final String rubros = jsonArray.toString();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("CONFIRMAR");
        builder.setIcon(R.drawable.ic_question);
        builder.setMessage("Desea confirmar el anticipo?");
        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                RegistrarAnticipoTask registrarAnticipoTask = new RegistrarAnticipoTask(descripcion,fechaInicio,fechaFin,idUsuario,idMotivo,idSede,rubros);
                registrarAnticipoTask.execute();
            }
        });
        builder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

}