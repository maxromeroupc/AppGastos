package app.factory.appgastos.views;


import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import app.factory.appgastos.R;
import app.factory.appgastos.datos.GastosDB;
import app.factory.appgastos.datos.MovimientoDbHelper;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddEntidadFragment extends Fragment implements View.OnClickListener {

    private TextView edtxtEntidad, edtxtFechaRegistro;
    private RadioButton rdbActivo, rdbInactivo;
    private ImageButton imbtnFechaRegistro, btnSave, btnCancel;

    //Manejo de fechas
    Calendar calendar = Calendar.getInstance();
    int anio = calendar.get(Calendar.YEAR);
    int mes = calendar.get(Calendar.MONTH);
    int dia = calendar.get(Calendar.DAY_OF_MONTH);

    public AddEntidadFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view = inflater.inflate(R.layout.fragment_add_entidad, container, false);
        edtxtEntidad = view.findViewById(R.id.edtxtEntidad);
        edtxtFechaRegistro = view.findViewById(R.id.edtxtFechaRegistro);
        rdbActivo = view.findViewById(R.id.rdbActivo);
        rdbInactivo = view.findViewById(R.id.rdbInactivo);
        btnSave = view.findViewById(R.id.btnSave);
        btnCancel = view.findViewById(R.id.btnCancel);
        imbtnFechaRegistro = view.findViewById(R.id.imbtnFechaRegistro);

        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        imbtnFechaRegistro.setOnClickListener(this);

        setDefaultValues();
        return view;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id){
            case R.id.btnSave:
                registEntidad();
                break;
            case R.id.btnCancel:
                goToListEntidad();
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

    private void registEntidad() {
        String msgValidacion = validarEntidad();
        if(msgValidacion.equalsIgnoreCase("")) {
            try {
                saveEntidad();
            }catch(Exception e){
                Toast.makeText(getContext(), "Erro al registrar la Entidad" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            goToListEntidad();
        }else{
            Toast.makeText(getContext(), msgValidacion, Toast.LENGTH_SHORT).show();
        }
    }

    private String validarEntidad(){
        String strMsg = "";

        if( edtxtEntidad.getText().toString().equalsIgnoreCase("") ) {
            strMsg = "- El nombre de la Entidad no puede quedar vacío. \n";
        }
        if( edtxtFechaRegistro.getText().toString().equalsIgnoreCase("") ) {
            strMsg = "- La Fecha de registro no puede quedar vacía.";
        }
        return strMsg;
    }

    private void saveEntidad() {
        MovimientoDbHelper movimientoDbHelper = new MovimientoDbHelper(getContext());
        SQLiteDatabase database = movimientoDbHelper.getWritableDatabase();

        String entidad = edtxtEntidad.getText().toString();
        String fechaRegistro = edtxtFechaRegistro.getText().toString();
        String estado = "A";

        if (rdbActivo.isChecked()) {
            estado = "A";
        } else if (rdbInactivo.isChecked()) {
            estado = "I";
        }

        ContentValues values = new ContentValues();
        values.put(GastosDB.GastosColumnsDB.Entidad, entidad);
        values.put(GastosDB.GastosColumnsDB.FechaRegistro, fechaRegistro);
        values.put(GastosDB.GastosColumnsDB.Estado, estado);
        database.insert(GastosDB.GastosColumnsDB.TABLE_NAME_ENTIDAD, null, values);
        database.close();

    }

    private void goToListEntidad(){
        ListEntidadFragment listEntidadFragment = new ListEntidadFragment();
        FragmentManager fragmentManager = getFragmentManager() ;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace( R.id.frameMain, listEntidadFragment).commit();
    }

    private String formatCadena(int val){
        String strVal = "";
        strVal =   "00" + String.valueOf(val) ;
        strVal = strVal.substring( strVal.length() - 2 );
        return strVal;
    }

}
