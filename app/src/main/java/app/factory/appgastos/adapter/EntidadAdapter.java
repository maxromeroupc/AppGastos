package app.factory.appgastos.adapter;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import app.factory.appgastos.R;
import app.factory.appgastos.datos.GastosDB;
import app.factory.appgastos.datos.MovimientoDbHelper;
import app.factory.appgastos.entidad.Entidad;
import app.factory.appgastos.views.ListEntidadFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EntidadAdapter extends RecyclerView.Adapter<EntidadAdapter.ViewHolder> {

    private List<Entidad> lstEntidad;
    private ListEntidadFragment listEntidadFragment;

    public EntidadAdapter(List<Entidad> lstEntidad, ListEntidadFragment listEntidadFragment){
        this.lstEntidad = lstEntidad;
        this.listEntidadFragment = listEntidadFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.fragment_item_entidad,parent,false );

        return new EntidadAdapter.ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.oEntidad = lstEntidad.get(position);
        holder.txtIdEntidad.setText( String.valueOf( holder.oEntidad.getIdEntidad() ) );
        holder.txtEntidad.setText( holder.oEntidad.getEntidad() );
        holder.txtFechaRegistro.setText( dateToString( holder.oEntidad.getFechaRegistro() ) );
        holder.txtEstado.setText( holder.oEntidad.getEstado() );
        if( holder.oEntidad.getEstado().equalsIgnoreCase("A") ){
            //holder.imgbtnActiveEntidad.setImageResource( R.drawable.check_icon_green_24 );
            holder.imgbtnActiveEntidad.setBackgroundResource(R.drawable.check_icon_green_24);
            //holder.imgbtnActiveEntidad.setBackgroundResource(@drawable/);
        }else{
            //holder.imgbtnActiveEntidad.setImageResource( R.drawable.check_icon_gray_24);
            holder.imgbtnActiveEntidad.setBackgroundResource(R.drawable.check_icon_gray_24);
        }

    }

    @Override
    public int getItemCount() {
        return lstEntidad.size();
    }

    private String dateToString(Date date){
        String strDate = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            strDate = sdf.format(date);
        }catch(Exception e){
            Log.e("Error AddMov", "Error convertir fecha en String. " + e.getMessage());
        }

        return strDate;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView txtIdEntidad, txtEntidad, txtFechaRegistro, txtEstado;
        private Entidad oEntidad;
        private ImageButton imgbtnActiveEntidad;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtIdEntidad = itemView.findViewById(R.id.txtIdEntidad);
            txtEntidad = itemView.findViewById(R.id.txtEntidad);
            txtFechaRegistro = itemView.findViewById(R.id.txtFechaRegistro);
            txtEstado = itemView.findViewById(R.id.txtEstado);
            imgbtnActiveEntidad = itemView.findViewById(R.id.imgbtnActiveEntidad);
            imgbtnActiveEntidad.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.imgbtnActiveEntidad:
                    setearEntidad();
                    break;
            }
        }

        private void setearEntidad() {
            MovimientoDbHelper movimientoDbHelper = new MovimientoDbHelper(itemView.getContext());
            ContentValues values = new ContentValues();
            values.put(GastosDB.GastosColumnsDB.Estado,"A");
            try {
                SQLiteDatabase database = movimientoDbHelper.getWritableDatabase();
                database.update(GastosDB.GastosColumnsDB.TABLE_NAME_ENTIDAD,
                        values,
                        "IdEntidad=?",
                        new String[]{ txtIdEntidad.getText().toString() }
                        );

                ContentValues vals = new ContentValues();
                vals.put(GastosDB.GastosColumnsDB.Estado,"I");
                database.update(GastosDB.GastosColumnsDB.TABLE_NAME_ENTIDAD,
                        vals,
                        "IdEntidad<>?",
                        new String[]{ txtIdEntidad.getText().toString() }
                );
                listEntidadFragment.refreshListEntidad();
                Toast.makeText(itemView.getContext(), "El registro fue habilitado." , Toast.LENGTH_SHORT).show();

            }catch(Exception e) {
                Log.e("Error Edit Entidad","Error editando Entidad. Error: " + e.getMessage()  );
            }
        }




    }



}
