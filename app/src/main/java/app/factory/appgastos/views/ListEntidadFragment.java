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

import app.factory.appgastos.R;
import app.factory.appgastos.adapter.EntidadAdapter;
import app.factory.appgastos.datos.MovimientoDbHelper;
import app.factory.appgastos.entidad.Entidad;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListEntidadFragment extends Fragment implements View.OnClickListener {

    private FloatingActionButton fabAddEntidad;
    private RecyclerView recListEntidad;

    public ListEntidadFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_entidad, container, false);

        recListEntidad = view.findViewById(R.id.recListEntidad);
        fabAddEntidad = view.findViewById(R.id.fabAddEntidad);
        fabAddEntidad.setOnClickListener(this);

        //default Values
        recyListMovimiento();
        return view;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.fabAddEntidad:
                goToAddEntidad();
                break;
        }

    }

    private void recyListMovimiento(){
        recListEntidad.setLayoutManager( new LinearLayoutManager(getContext()) );
        List<Entidad> lstEntidad = listEntidad();
        recListEntidad.setAdapter( new EntidadAdapter( lstEntidad, this) );
    }

    private List<Entidad> listEntidad(){
        List<Entidad> lstEntidad = new ArrayList<>();
        Entidad entidad = null;
        MovimientoDbHelper movimientoDbHelper = new MovimientoDbHelper(getContext());
        SQLiteDatabase database = movimientoDbHelper.getReadableDatabase();
        String sql = "SELECT IdEntidad, Entidad, FechaRegistro, Estado FROM Entidad";

        try {
            Cursor cursor = database.rawQuery( sql, null );

            if(cursor.moveToFirst()){
                do {
                    entidad = new Entidad();
                    entidad.setIdEntidad( cursor.getInt( cursor.getColumnIndex("IdEntidad") ) );
                    entidad.setEntidad( cursor.getString( cursor.getColumnIndex("Entidad") ) );
                    entidad.setFechaRegistro( strToDate( cursor.getString( cursor.getColumnIndex("FechaRegistro") ) ) );
                    entidad.setEstado( cursor.getString( cursor.getColumnIndex("Estado") ) );
                    lstEntidad.add(entidad);
                } while( cursor.moveToNext() );
            }
            database.close();
        }catch( Exception e){
            Log.e("Error","Error listar movimiento. " + e.getMessage());
        }
        return lstEntidad;
    }

    private void goToAddEntidad(){
        AddEntidadFragment addEntidadFragment = new AddEntidadFragment();
        FragmentManager fragmentManager = getFragmentManager() ;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace( R.id.frameMain, addEntidadFragment).commit();
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

    public void refreshListEntidad(){
        recyListMovimiento();
    }

}
