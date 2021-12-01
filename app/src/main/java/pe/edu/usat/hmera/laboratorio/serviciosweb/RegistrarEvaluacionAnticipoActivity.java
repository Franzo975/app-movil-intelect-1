package pe.edu.usat.hmera.laboratorio.serviciosweb;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import pe.edu.usat.hmera.laboratorio.serviciosweb.adaptador.AdaptadorAnticipo;
import pe.edu.usat.hmera.laboratorio.serviciosweb.logica.Sesion;
import pe.edu.usat.hmera.laboratorio.serviciosweb.util.Helper;

public class RegistrarEvaluacionAnticipoActivity extends AppCompatActivity implements View.OnClickListener {

    Spinner spEstado;
    EditText txtObservacionEvaluacion;
    Button btnRegistrarEvaluacion;
    private static String mensajeRegistro;
    String estados[] = new String[3];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_evaluacion_anticipo);
        this.setTitle("REGISTRAR EVALUACIÓN ANTICIPO");
        spEstado = findViewById(R.id.spEstadoEvaluacion);
        txtObservacionEvaluacion = findViewById(R.id.txtObservacionUser);
        btnRegistrarEvaluacion = findViewById(R.id.btnRegistrarEvaluacion);
        btnRegistrarEvaluacion.setOnClickListener(this);
        estados[0] = "APROBADO";
        estados[1] = "OBSERVADO";
        estados[2] = "RECHAZADO";
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, estados);
        spEstado.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRegistrarEvaluacion:
                registrarEvaluacion();
                break;
        }
    }

    private class RegistrarEvaluacionTask extends AsyncTask<Void, Void, Boolean> {

        private String Observacion;
        private String IdAnticipo;
        private String IdEstado;

        public RegistrarEvaluacionTask(String observacion, String idAnticipo, String idEstado) {
            Observacion = observacion;
            IdAnticipo = idAnticipo;
            IdEstado = idEstado;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean resultado = false;
            try{
                String URL = Helper.BASE_URL_WS + "/anticipo/evaluacion";
                HashMap<String,String> parametros = new HashMap<>();
                parametros.put("token", Sesion.TOKEN);
                parametros.put("observacion",this.Observacion);
                parametros.put("idUsuario",String.valueOf(Sesion.ID));
                parametros.put("idAnticipo",this.IdAnticipo);
                parametros.put("idEstado",this.IdEstado);
                String response = new Helper().requestHttpPost(URL,parametros);
                JSONObject jsonObject = new JSONObject(response);
                System.out.println(jsonObject);
                if (jsonObject.getBoolean("status")) {
                    RegistrarEvaluacionAnticipoActivity.mensajeRegistro = jsonObject.getString("data");
                    resultado = true;
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
                Helper.mensajeInformacion(RegistrarEvaluacionAnticipoActivity.this, "ANTICIPO", RegistrarEvaluacionAnticipoActivity.mensajeRegistro);
                RegistrarEvaluacionAnticipoActivity.this.finish();
            }
        }
    }

    private void registrarEvaluacion() {
        final String Observacion = txtObservacionEvaluacion.getText().toString();
        final String IdAnticipo = String.valueOf(AdaptadorAnticipo.posicionItemSeleccionado);
        final String IdEstado = String.valueOf(spEstado.getSelectedItemPosition()+1);
        System.out.println(IdAnticipo);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("CONFIRMAR");
        builder.setIcon(R.drawable.ic_question);
        builder.setMessage("Desea confirmar la evaluacion?");
        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                RegistrarEvaluacionTask registrarEvaluacionTask = new RegistrarEvaluacionTask(Observacion,IdAnticipo,IdEstado);
                registrarEvaluacionTask.execute();
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