package app.factory.appgastos.views;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import app.factory.appgastos.R;
import app.factory.appgastos.adapter.CategoriaAdapter;
import app.factory.appgastos.datos.MovimientoDbHelper;
import app.factory.appgastos.entidad.Categoria;


public class ListCategoriaFragment extends Fragment implements View.OnClickListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private FloatingActionButton fabAddCategoria;
    private RecyclerView recListCategoria;
    private TextView txtResumen;
    private double gloTotalImporteIngreso, gloTotalImporteGasto ;

    public ListCategoriaFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ListCategoriaFragment newInstance(int columnCount) {
        ListCategoriaFragment fragment = new ListCategoriaFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_categoria, container, false);
        fabAddCategoria = view.findViewById(R.id.fabAddCategoria);
        fabAddCategoria.setOnClickListener(this);
        recListCategoria = view.findViewById(R.id.recListCategoria);
        txtResumen = view.findViewById(R.id.txtResumen);
        // Set the adapter

        setRecViewCategoria();

        setDefaultValues();
        return view;
    }

    private void setDefaultValues() {
        double saldo = gloTotalImporteIngreso + gloTotalImporteGasto;
        txtResumen.setText("I:" + formatMonto( gloTotalImporteIngreso )+
                " - G:" + formatMonto(gloTotalImporteGasto )
                + " - S:" + formatMonto(saldo) );
    }

    private String formatMonto(double monto){
        DecimalFormat decimalFormat = new DecimalFormat("#,###.00");
        return decimalFormat.format(monto);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fabAddCategoria:
                goToAddCategoria();
                break;
        }
    }


    private void setRecViewCategoria() {
        List<Categoria> lstCategoria = listarCategoria();
        recListCategoria.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recListCategoria.setAdapter(new CategoriaAdapter(lstCategoria, this));

    }

    private List<Categoria> listarCategoria(){
        List<Categoria> lstCategoria = new ArrayList<>();
        Categoria oCategoria = null;
        MovimientoDbHelper movimientoDbHelper = new MovimientoDbHelper(this.getContext());
        SQLiteDatabase database = movimientoDbHelper.getReadableDatabase();
        try {

            Cursor curCateg = database.rawQuery("SELECT c.IdCategoria, c.Categoria,( SELECT ifnull(SUM( CASE WHEN IdTipoMovimiento = 1 THEN Importe ELSE Importe*-1 END ),0) " +
                            " FROM Movimiento m " +
                            "INNER JOIN Entidad e ON e.IdEntidad= m.IdEntidad and e.Estado = 'A'" +
                            "WHERE m.IdCategoria = c.IdCategoria " +
                            ") AS MontoMovimientoCategoria " +
                            " FROM Categoria c  " +
                            " WHERE c.Estado = 'A' " +
                            " GROUP BY c.IdCategoria, c.Categoria"
                    , null);

            if ( curCateg.moveToFirst() ) {
                do {
                    oCategoria = new Categoria();
                    oCategoria.setIdCategoria( curCateg.getInt( curCateg.getColumnIndex("IdCategoria") )    );
                    oCategoria.setCategoria(curCateg.getString( curCateg.getColumnIndex("Categoria") ));
                    oCategoria.setMontoMovimientoCategoria(curCateg.getDouble( curCateg.getColumnIndex("MontoMovimientoCategoria") ));
                    if( oCategoria.getMontoMovimientoCategoria() > 0){
                        gloTotalImporteIngreso += oCategoria.getMontoMovimientoCategoria();
                    }else{
                        gloTotalImporteGasto += oCategoria.getMontoMovimientoCategoria();
                    }

                    lstCategoria.add(oCategoria);
                } while (curCateg.moveToNext());
            }
        }catch(Exception ex){
            Log.e("Error List categoria",ex.getMessage().toString());
        }

        return lstCategoria;
    }

    private void goToAddCategoria() {
        AddCategoriaFragment addCategoriaFragment = new AddCategoriaFragment();
        FragmentManager fragmentManager = getFragmentManager() ;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace( R.id.frameMain, addCategoriaFragment).commit();
    }
}
