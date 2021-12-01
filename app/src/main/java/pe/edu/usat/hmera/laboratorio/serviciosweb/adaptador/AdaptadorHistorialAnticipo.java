package pe.edu.usat.hmera.laboratorio.serviciosweb.adaptador;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import pe.edu.usat.hmera.laboratorio.serviciosweb.R;
import pe.edu.usat.hmera.laboratorio.serviciosweb.logica.HistorialAnticipo;

public class AdaptadorHistorialAnticipo extends RecyclerView.Adapter<AdaptadorHistorialAnticipo.ViewHolder> {

    private Context context;
    public static ArrayList<HistorialAnticipo> listaAnticipo;

    public AdaptadorHistorialAnticipo(Context context) {
        this.context = context;
        this.listaAnticipo = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_historial_anticipo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HistorialAnticipo historialAnticipo = listaAnticipo.get(position);
        //holder.txtDescripcionAnticipo.setText(historialAnticipo.getDescripcionAnticipo());
        //holder.txtEstado.setText("ESTADO: "+historialAnticipo.getEstado());
        //holder.txtFechaRegistro.setText("FECHA REGISTRO: "+historialAnticipo.getFechaRegistro());
        //holder.txtMotivo.setText("MOTIVO: "+historialAnticipo.getMotivo());
        //holder.txtObservacion.setText("OBSERVACION: "+historialAnticipo.getObservacion());
        //holder.txtPersonal.setText("PERSONAL: "+historialAnticipo.getPersonal());
        //holder.txtSede.setText("SEDE: "+historialAnticipo.getSede());
        //holder.txtTipoPersonal.setText("TIPO PERSONAL: "+historialAnticipo.getTipoPersonal());
        holder.txtDescripcionAnticipo.setText(historialAnticipo.getDescripcionAnticipo());
        holder.txtEstado.setText(historialAnticipo.getEstado());
        holder.txtFechaRegistro.setText(historialAnticipo.getFechaRegistro());
        holder.txtMotivo.setText(historialAnticipo.getMotivo());
        holder.txtObservacion.setText(historialAnticipo.getObservacion().toUpperCase(Locale.ROOT));
        holder.txtPersonal.setText(historialAnticipo.getPersonal());
        holder.txtSede.setText(historialAnticipo.getSede());
        holder.txtTipoPersonal.setText(historialAnticipo.getTipoPersonal());
    }

    @Override
    public int getItemCount() {
        return listaAnticipo.size();
    }

    public void cargarHistorialAnticipos(ArrayList<HistorialAnticipo> lista) {
        listaAnticipo = lista;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtDescripcionAnticipo, txtEstado, txtFechaRegistro, txtMotivo, txtObservacion, txtPersonal, txtSede, txtTipoPersonal;

        public ViewHolder(View itemView) {
            super(itemView);
            txtDescripcionAnticipo = itemView.findViewById(R.id.txtDescripcionAnticipo);
            txtEstado = itemView.findViewById(R.id.txtEstado);
            txtFechaRegistro = itemView.findViewById(R.id.txtFechaRegistro);
            txtObservacion = itemView.findViewById(R.id.txtObservacion);
            txtMotivo = itemView.findViewById(R.id.txtMotivo);
            txtPersonal = itemView.findViewById(R.id.txtPersonal);
            txtSede = itemView.findViewById(R.id.txtSede);
            txtTipoPersonal = itemView.findViewById(R.id.txtTipoPersonal);
        }
    }

}
