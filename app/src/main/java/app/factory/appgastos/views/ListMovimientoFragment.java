package app.factory.appgastos.views;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import app.factory.appgastos.R;
import app.factory.appgastos.adapter.MovimientoAdapter;
import app.factory.appgastos.datos.MovimientoDbHelper;
import app.factory.appgastos.entidad.Movimiento;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListMovimientoFragment extends Fragment implements View.OnClickListener {

    private RecyclerView recListMovimiento;
    private FloatingActionButton fabAddMovimiento;
    private TextView txtResumen;

    private double gloTotalImporteIngreso, gloTotalImporteGasto ;
    private int gloCantidad;

    public ListMovimientoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_movimiento, container, false);
        recListMovimiento = view.findViewById(R.id.recListMovimiento);
        fabAddMovimiento = view.findViewById(R.id.fabAddMovimiento);
        txtResumen = view.findViewById(R.id.txtResumen);
        fabAddMovimiento.setOnClickListener(this);
        recyListMovimiento();
        setDefaultValues();
        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id){
            case R.id.fabAddMovimiento:
                goToAddMovimiento();
                break;
        }
    }

    private void setDefaultValues() {
        double saldo = gloTotalImporteIngreso - gloTotalImporteGasto;
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        txtResumen.setText("Cant:" + gloCantidad + " - I:" + decimalFormat.format( gloTotalImporteIngreso )+
                " - G:" + decimalFormat.format(gloTotalImporteGasto )
                + " - S:" + decimalFormat.format(saldo) );
    }

    private void recyListMovimiento(){
        recListMovimiento.setLayoutManager( new LinearLayoutManager(getContext()) );
        List<Movimiento> lstMovimiento = listMovimiento();
        recListMovimiento.setAdapter( new MovimientoAdapter( lstMovimiento, this ) );
    }

    private List<Movimiento> listMovimiento(){
        List<Movimiento> lstMov = new ArrayList<Movimiento>();
        Movimiento mov = null;
        MovimientoDbHelper movimientoDbHelper = new MovimientoDbHelper(getContext());
        SQLiteDatabase database = movimientoDbHelper.getReadableDatabase();
        String sql = "SELECT IdMovimiento, m.IdEntidad, IdTipoMovimiento, FechaMovimiento, m.Descripcion, Importe " +
                "FROM Movimiento m"
                + " INNER JOIN Entidad e ON m.IdEntidad = e.IdEntidad "
                + " WHERE e.Estado = 'A'";

        try {
            Cursor cursor = database.rawQuery( sql, null );

            if(cursor.moveToFirst()){
                do {
                    mov = new Movimiento();
                    mov.setIdMovimiento( cursor.getInt( cursor.getColumnIndex("IdMovimiento") ) );
                    mov.setIdEntidad( cursor.getInt( cursor.getColumnIndex("IdEntidad") ) );
                    mov.setIdTipoMovimiento( cursor.getInt( cursor.getColumnIndex("IdTipoMovimiento") ) );
                    mov.setFechaMovimiento( strToDate( cursor.getString( cursor.getColumnIndex("FechaMovimiento") ) ) );
                    mov.setDescripcion( cursor.getString( cursor.getColumnIndex("Descripcion") )  );
                    mov.setImporte( cursor.getDouble( cursor.getColumnIndex("Importe") ) );

                    gloCantidad += 1;
                    if(mov.getIdTipoMovimiento() == 1) {
                        gloTotalImporteIngreso += mov.getImporte();
                    }else if( mov.getIdTipoMovimiento() == 2) {
                        gloTotalImporteGasto += mov.getImporte();
                    }
                    lstMov.add(mov);
                } while( cursor.moveToNext() );
            }
            database.close();
        }catch( Exception e){
            Log.e("Error","Error listar movimiento. " + e.getMessage());
        }

        return lstMov;
    }

    private Date strToDate(String strDate){
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        try {
            date = sdf.parse( strDate );
        }catch(Exception e){
            Log.e("Error","Error Convirtiendo fecha" + e.getMessage() );
        }
        return date;
    }

    private void goToAddMovimiento(){
        AddMovimientoFragment addMovimientoFragment = new AddMovimientoFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameMain, addMovimientoFragment).commit();
    }

    public void refreshListMovimiento(){
        gloCantidad = 0;
        gloTotalImporteIngreso = 0;
        gloTotalImporteGasto = 0;
        recyListMovimiento();
        setDefaultValues();
    }

}
