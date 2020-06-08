package app.factory.appgastos.views;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import app.factory.appgastos.R;
import app.factory.appgastos.datos.GastosDB;
import app.factory.appgastos.datos.MovimientoDbHelper;


public class AddCategoriaFragment extends Fragment implements View.OnClickListener {

    private EditText edtxtCategoria, edtxtFechaRegistro;
    private RadioButton rdbActivo, rdbInactivo;
    private ImageButton imgbtnSave, imgbtnCancel;

    //Manejo de fechas
    Calendar calendar = Calendar.getInstance();
    int anio = calendar.get(Calendar.YEAR);
    int mes = calendar.get(Calendar.MONTH);
    int dia = calendar.get(Calendar.DAY_OF_MONTH);


    public AddCategoriaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_categoria, container, false);
        edtxtCategoria = view.findViewById(R.id.edtxtCategoria);
        edtxtFechaRegistro= view.findViewById(R.id.edtxtFechaRegistro);
        rdbActivo = view.findViewById(R.id.rdbActivo);
        rdbInactivo = view.findViewById(R.id.rdbInactivo);
        imgbtnSave = view.findViewById(R.id.btnSave);
        imgbtnCancel = view.findViewById(R.id.btnCancel);
        imgbtnSave.setOnClickListener(this);
        imgbtnCancel.setOnClickListener(this);
        setDefaultValues();
        return view;
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
        int id = v.getId();
        switch(id){
            case R.id.btnSave:
                registCategoria();
                break;
            case R.id.btnCancel:
                goToListCategoria();
                break;
            case R.id.imbtnFechaRegistro:
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String strAnio, strMes, strDia;
                        strAnio = String.valueOf(year);
                        strMes = formatCadena(month + 1);
                        strDia = formatCadena(dayOfMonth);
                        edtxtFechaRegistro.setText(strDia + "/" + strMes + "/" + strAnio);
                    }
                }, anio, mes, dia);
                datePickerDialog.show();
                break;
        }
    }

    private void setDefaultValues() {
        rdbActivo.setChecked(true);
        setDateRegistro();
    }


    private void setDateRegistro() {
        String strAnio, strMes, strDia;
        strAnio = String.valueOf(anio);
        strMes = formatCadena(mes + 1);
        strDia = formatCadena(dia);
        edtxtFechaRegistro.setText(strDia + "/" + strMes + "/" + strAnio);
    }

    private void registCategoria() {
        String msgValidacion = validarCategoria();
        if(msgValidacion.equalsIgnoreCase("")) {
            try {
                saveCategoria();
            }catch(Exception e){
                Toast.makeText(getContext(), "Erro al registrar la Categoria" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            goToListCategoria();
        }else{
            Toast.makeText(getContext(), msgValidacion, Toast.LENGTH_SHORT).show();
        }
    }

    private String validarCategoria(){
        String strMsg = "";

        if( edtxtCategoria.getText().toString().equalsIgnoreCase("") ) {
            strMsg = "- El nombre de la categoria no puede quedar vacío. \n";
        }
        if( edtxtFechaRegistro.getText().toString().equalsIgnoreCase("") ) {
            strMsg = "- La Fecha de registro no puede quedar vacía.";
        }
        return strMsg;
    }

    private void saveCategoria() {
        MovimientoDbHelper movimientoDbHelper = new MovimientoDbHelper(getContext());
        SQLiteDatabase database = movimientoDbHelper.getWritableDatabase();

        String categoria = edtxtCategoria.getText().toString();
        String fechaRegistro = edtxtFechaRegistro.getText().toString();
        String estado = "A";

        if (rdbActivo.isChecked()) {
            estado = "A";
        } else if (rdbInactivo.isChecked()) {
            estado = "I";
        }

        ContentValues values = new ContentValues();
        values.put(GastosDB.GastosColumnsDB.Categoria, categoria);
        values.put(GastosDB.GastosColumnsDB.FechaRegistro, fechaRegistro);
        values.put(GastosDB.GastosColumnsDB.Estado, estado);
        try {
            database.insert(GastosDB.GastosColumnsDB.TABLE_NAME_CATEGORIA, null, values);
        }catch(Exception ex){
            Log.e("Error Insert categ",ex.getMessage());
        }
        database.close();
    }


    private void goToListCategoria(){
        ListCategoriaFragment listCategoriaFragment = new ListCategoriaFragment();
        FragmentManager fragmentManager = getFragmentManager() ;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace( R.id.frameMain, listCategoriaFragment).commit();
    }

    private String formatCadena(int val){
        String strVal = "";
        strVal =   "00" + String.valueOf(val) ;
        strVal = strVal.substring( strVal.length() - 2 );
        return strVal;
    }

}
