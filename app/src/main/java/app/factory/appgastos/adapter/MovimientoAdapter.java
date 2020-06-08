package app.factory.appgastos.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import app.factory.appgastos.R;
import app.factory.appgastos.datos.GastosDB;
import app.factory.appgastos.datos.MovimientoDbHelper;
import app.factory.appgastos.entidad.Movimiento;
import app.factory.appgastos.views.ListMovimientoFragment;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MovimientoAdapter extends RecyclerView.Adapter<MovimientoAdapter.ViewHolder> {

    private List<Movimiento> lstMovimiento;
    private ListMovimientoFragment listMovimientoFragment;

    public MovimientoAdapter(List<Movimiento> lstMovimiento, ListMovimientoFragment listMovimientoFragment) {
        this.lstMovimiento = lstMovimiento;
        this.listMovimientoFragment = listMovimientoFragment;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.fragment_item_movimiento,parent,false );

        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.oMovimiento = lstMovimiento.get(position);
        holder.txtIdMovimiento.setText( String.valueOf( lstMovimiento.get(position).getIdMovimiento() ));
        holder.txtTipoMovimiento.setText( String.valueOf( lstMovimiento.get(position).getIdTipoMovimiento() ) );
        holder.txtFechaMovimiento.setText( dateToString( lstMovimiento.get(position).getFechaMovimiento()) );
        holder.txtImporte.setText( decimalToString( lstMovimiento.get(position).getImporte() ) );
        holder.txtDescripcion.setText( String.valueOf( lstMovimiento.get(position).getDescripcion() ) );
        holder.txtCategoria.setText( lstMovimiento.get(position).getCategoria()  );
        if ( lstMovimiento.get(position).getIdTipoMovimiento() == 1 ) {
            holder.imgIngresoGasto.setImageResource(R.drawable.add_green_16);
            holder.imgIngresoGasto.setBackgroundResource(R.drawable.add_green_16);
        }else{
            holder.imgIngresoGasto.setImageResource(R.drawable.minus_red_16);
            holder.imgIngresoGasto.setBackgroundResource(R.drawable.minus_red_16);
        }
    }

    @Override
    public int getItemCount() {
        return lstMovimiento.size();
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

    private String decimalToString(double val){
        String strVal = "";
        DecimalFormat decimalFormat = new DecimalFormat("#,###.00");
        strVal = decimalFormat.format(val);
        return strVal;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public Movimiento oMovimiento;
        private TextView txtIdMovimiento, txtTipoMovimiento, txtFechaMovimiento,
                txtImporte, txtDescripcion, txtCategoria;
        private ImageButton imbtnDeleteMov, imbtnEditMov;
        private ImageView imgIngresoGasto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtIdMovimiento = itemView.findViewById(R.id.txtIdMovimiento);
            txtTipoMovimiento = itemView.findViewById(R.id.txtTipoMovimiento);
            txtFechaMovimiento = itemView.findViewById(R.id.txtFechaMovimiento);
            txtImporte = itemView.findViewById(R.id.txtImporte);
            txtDescripcion = itemView.findViewById(R.id.txtDescripcion);
            txtCategoria = itemView.findViewById(R.id.txtCategoria);
            imbtnDeleteMov = itemView.findViewById(R.id.imbtnDeleteMov);
            imbtnEditMov = itemView.findViewById(R.id.imbtnEditMov);
            imgIngresoGasto = itemView.findViewById(R.id.imgIngresoGasto);
            imbtnDeleteMov.setOnClickListener(this);
            imbtnEditMov.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch(id){
                case R.id.imbtnDeleteMov:
                    AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                    builder.setTitle("Confirmación")
                            .setMessage("¿Seguro que desea eliminar el movimiento?")
                            .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteMovimiento();
                                }
                            })
                            .setNegativeButton("NO",null);
                    AlertDialog alertDel = builder.create();
                    alertDel.show();

                    break;
                case R.id.imbtnEditMov:
                    goToEditMovimiento();
                    break;
            }

        }

        private void goToEditMovimiento(){
            listMovimientoFragment.goToEditMovimiento( Integer.parseInt( txtIdMovimiento.getText().toString() )    );
        }

        private void deleteMovimiento() {
            MovimientoDbHelper movimientoDbHelper = new MovimientoDbHelper(itemView.getContext());
            try {
                SQLiteDatabase database = movimientoDbHelper.getWritableDatabase();
                database.delete(GastosDB.GastosColumnsDB.TABLE_NAME_MOVIMIENTO, "IdMovimiento=?", new String[]{txtIdMovimiento.getText().toString()});
                listMovimientoFragment.refreshListMovimiento();
                Toast.makeText(itemView.getContext(), "El registro " + txtIdMovimiento.getText().toString() +" fue eliminado." , Toast.LENGTH_SHORT).show();
            }catch(Exception e) {
                Log.e("Error List Mov","Error eliminando movimiento. Error: " + e.getMessage()  );
            }
        }
    }


}
