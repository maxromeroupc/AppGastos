package app.factory.appgastos.views;


import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import app.factory.appgastos.R;
import app.factory.appgastos.datos.GastosDB;
import app.factory.appgastos.datos.MovimientoDbHelper;
import app.factory.appgastos.entidad.Entidad;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddMovimientoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddMovimientoFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public AddMovimientoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddMovimientoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddMovimientoFragment newInstance(String param1, String param2) {
        AddMovimientoFragment fragment = new AddMovimientoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private TextView txtTitleMovimiento;
    private EditText edtxtFechaMovimiento, edtxtImporte, edtxtDescripcionMovimiento;
    private RadioButton rdbIngreso, rdbGasto;
    private Spinner spnEntidad;
    private ImageButton imbtnFechaMovimiento, btnSave, btnCancel;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_movimiento, container, false);

        txtTitleMovimiento = view.findViewById(R.id.txtTitleAddMovimiento);
        edtxtFechaMovimiento = view.findViewById(R.id.edtxtFechaMovimiento);
        imbtnFechaMovimiento = view.findViewById(R.id.imbtnFechaMovimiento);
        edtxtDescripcionMovimiento = view.findViewById(R.id.edtxtDescripcion);
        edtxtImporte = view.findViewById(R.id.edtxtImporte);
        rdbIngreso = view.findViewById(R.id.rdbIngreso);
        rdbGasto = view.findViewById(R.id.rdbGasto);
        spnEntidad = view.findViewById(R.id.spnEntidad);
        btnSave = view.findViewById(R.id.btnSave);
        btnCancel = view.findViewById(R.id.btnCancel);

        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        imbtnFechaMovimiento.setOnClickListener(this);

        setSpnEntidad();
        setDefaultValues();
        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnSave:
                registMovimiento();
                break;

            case R.id.imbtnFechaMovimiento:
                Calendar calendar = Calendar.getInstance();
                int anio, mes, dia;
                anio = calendar.get(Calendar.YEAR);
                mes = calendar.get(Calendar.MONTH);
                dia = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String strAnio, strMes, strDia;
                        strAnio = String.valueOf(year);
                        strMes = formatCadena(month + 1);
                        strDia = formatCadena(dayOfMonth);
                        edtxtFechaMovimiento.setText(strDia + "/" + strMes + "/" + strAnio);
                    }
                }, anio, mes, dia);
                datePickerDialog.show();
                break;
            case R.id.btnCancel:
                goToListMovimiento();
                break;
        }
    }


    //region Procedures and Functions
    private void setDefaultValues() {
        rdbGasto.setChecked(true);
        setDateMovimiento();
    }

    private void setDateMovimiento() {
        Calendar calendar = Calendar.getInstance();
        int anio, mes, dia;
        anio = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH);
        dia = calendar.get(Calendar.DAY_OF_MONTH);
        String strAnio, strMes, strDia;
        strAnio = String.valueOf(anio);
        strMes = formatCadena(mes + 1);
        strDia = formatCadena(dia);
        edtxtFechaMovimiento.setText(strDia + "/" + strMes + "/" + strAnio);
    }

    private void setSpnEntidad() {
        List<Entidad> lstEntidad = new ArrayList<>();
        lstEntidad = listEntidad();
        ArrayAdapter<Entidad> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, lstEntidad);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnEntidad.setAdapter(arrayAdapter);
    }

    private List<Entidad> listEntidad(){
        List<Entidad> lstEntidad = new ArrayList<>();
        Entidad entidad = null;
        MovimientoDbHelper movimientoDbHelper = new MovimientoDbHelper(getContext());
        SQLiteDatabase database = movimientoDbHelper.getReadableDatabase();
        String sql = "SELECT IdEntidad, Entidad, FechaRegistro, Estado FROM Entidad WHERE Estado = 'A'";

        try {
            Cursor cursor = database.rawQuery( sql, null );

            if(cursor.moveToFirst()){
                do {
                    entidad = new Entidad();
                    entidad.setIdEntidad( cursor.getInt( cursor.getColumnIndex("IdEntidad") ) );
                    entidad.setEntidad( cursor.getString( cursor.getColumnIndex("Entidad") ) );
                    lstEntidad.add(entidad);
                } while( cursor.moveToNext() );
            }
            database.close();
        }catch( Exception e){
            Log.e("Error","Error listar movimiento. " + e.getMessage());
        }
        return lstEntidad;
    }

    private void registMovimiento() {
        String msgValidacion = validarRegistro();
        if(msgValidacion.equalsIgnoreCase("")) {
            saveMovimiento();
            goToListMovimiento();
        }else{
            Toast.makeText(getContext(), msgValidacion, Toast.LENGTH_SHORT).show();
        }

    }

    private String validarRegistro(){
        String strMsg = "";
        String fechaMovimiento  ="";
        double importe = 0;

        if( edtxtFechaMovimiento.getText() != null ) {
            fechaMovimiento = edtxtFechaMovimiento.getText().toString();
        }
        if( !edtxtImporte.getText().toString().equalsIgnoreCase("") ) {
            importe = Double.parseDouble(edtxtImporte.getText().toString());
        }
        if(importe <= 0){
            strMsg = "- El importe debe ser mayor que 0. \n";
        }

        if(fechaMovimiento.equalsIgnoreCase("")){
            strMsg += "- La fecha no puede quedar vacía.";
        }

        return strMsg;
    }

    private void saveMovimiento() {
        MovimientoDbHelper movimientoDbHelper = new MovimientoDbHelper(getContext());
        SQLiteDatabase database = movimientoDbHelper.getWritableDatabase();

        int idTipoMovimiento = 0, idEntidad;
        String fechaMovimiento, descripcionMovimiento;
        double importe;

        idEntidad = ((Entidad) spnEntidad.getSelectedItem()).getIdEntidad();
        if (rdbIngreso.isChecked()) {
            idTipoMovimiento = 1;
        } else if (rdbGasto.isChecked()) {
            idTipoMovimiento = 2;
        }

        fechaMovimiento = edtxtFechaMovimiento.getText().toString();
        descripcionMovimiento = edtxtDescripcionMovimiento.getText().toString();
        importe = Double.parseDouble(edtxtImporte.getText().toString());

        ContentValues values = new ContentValues();
        values.put(GastosDB.GastosColumnsDB.IdEntidad, idEntidad);
        values.put(GastosDB.GastosColumnsDB.IdTipoMovimiento, idTipoMovimiento);
        values.put(GastosDB.GastosColumnsDB.FechaMovimiento, fechaMovimiento);
        values.put(GastosDB.GastosColumnsDB.Descripcion,descripcionMovimiento);
        values.put(GastosDB.GastosColumnsDB.Importe, importe);
        database.insert(GastosDB.GastosColumnsDB.TABLE_NAME_MOVIMIENTO, null, values);
        database.close();

    }

    private void goToListMovimiento(){
        ListMovimientoFragment listMovimientoFragment = new ListMovimientoFragment();
        FragmentManager fragmentManager = getFragmentManager() ;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace( R.id.frameMain, listMovimientoFragment).commit();
    }

    private String formatCadena(int val){
        String strVal = "";
        strVal =   "00" + String.valueOf(val) ;
        strVal = strVal.substring( strVal.length() - 2 );
        return strVal;
    }

    private Date strToDate(String strDate){
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            date = sdf.parse(strDate);
        } catch(Exception e){
            Log.e("Error","Error convirtiendo la fecha en Registrar movimiento." + e.getMessage());
        }

        return date;
    }

    //endregion

}
