package pe.edu.usat.hmera.laboratorio.serviciosweb.adaptador;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import pe.edu.usat.hmera.laboratorio.serviciosweb.AnticipoFragment;
import pe.edu.usat.hmera.laboratorio.serviciosweb.HistorialAnticipoFragment;
import pe.edu.usat.hmera.laboratorio.serviciosweb.R;
import pe.edu.usat.hmera.laboratorio.serviciosweb.RegistrarAnticipoActivity;
import pe.edu.usat.hmera.laboratorio.serviciosweb.RegistrarEvaluacionAnticipoActivity;
import pe.edu.usat.hmera.laboratorio.serviciosweb.logica.Anticipo;
import pe.edu.usat.hmera.laboratorio.serviciosweb.logica.Sesion;

public class AdaptadorAnticipo extends RecyclerView.Adapter<AdaptadorAnticipo.ViewHolder> {

    private Context context;
    public static ArrayList<Anticipo> listaAnticipo;
    public static int posicionItemSeleccionado;

    public AdaptadorAnticipo(Context context) {
        this.context = context;
        this.listaAnticipo = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_anticipo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Anticipo anticipo = listaAnticipo.get(position);
        holder.txtDescripcionAnticipo.setText(anticipo.getDescripcion().toUpperCase(Locale.ROOT));
        holder.txtFechaInicio.setText("DE: "+anticipo.getFechaInicio());
        holder.txtFechaFin.setText("HASTA: "+anticipo.getFechaFin());
        holder.txtEstadoAnticipo.setText(anticipo.getEstado());
        holder.txtTotal.setText("S/."+anticipo.getTotal());
    }

    @Override
    public int getItemCount() {
        return listaAnticipo.size();
    }

    public void cargarAnticipos(ArrayList<Anticipo> lista) {
        listaAnticipo = lista;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtDescripcionAnticipo, txtFechaInicio, txtFechaFin, txtTotal, txtEstadoAnticipo;

        public ViewHolder(View itemView) {
            super(itemView);
            txtDescripcionAnticipo = itemView.findViewById(R.id.txtDescripcionAnticipo);
            txtFechaInicio = itemView.findViewById(R.id.txtFechaInicio);
            txtFechaFin = itemView.findViewById(R.id.txtFechaFin);
            txtTotal = itemView.findViewById(R.id.txtTotal);
            txtEstadoAnticipo = itemView.findViewById(R.id.txtEstadoAnticipo);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            posicionItemSeleccionado = Anticipo.listaAnticipo.get(this.getAdapterPosition()).getId();
            if (Sesion.ID != 2) {
                Intent intent = new Intent(context, RegistrarEvaluacionAnticipoActivity.class);
                context.startActivity(intent);
            } else {
                AppCompatActivity appCompatActivity = (AppCompatActivity) view.getContext();
                HistorialAnticipoFragment historialAnticipoFragment = new HistorialAnticipoFragment();
                appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor,historialAnticipoFragment).addToBackStack(null).commit();
            }


        }
    }

}
