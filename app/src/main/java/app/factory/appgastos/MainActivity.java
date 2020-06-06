package app.factory.appgastos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import app.factory.appgastos.R;

import app.factory.appgastos.views.ListEntidadFragment;
import app.factory.appgastos.views.ListMovimientoFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView btnNavMenu;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        btnNavMenu = findViewById(R.id.btnNavMenu);
        btnNavMenu.setOnNavigationItemSelectedListener(this);

        goToListMovimiento();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int idItem = menuItem.getItemId();
        switch(idItem){
            case R.id.ListMovientos:
                goToListMovimiento();
                break;
            case R.id.ListEntidad:
                goToListEntidad();
                break;
        }
        return true;
    }


    private void goToListMovimiento(){
        ListMovimientoFragment listMovimientoFragment = new ListMovimientoFragment();
        FragmentManager fragmentManager = getSupportFragmentManager() ;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace( R.id.frameMain, listMovimientoFragment).commit();
    }

    private void goToListEntidad(){
        ListEntidadFragment listEntidadFragment = new ListEntidadFragment();
        FragmentManager fragmentManager = getSupportFragmentManager() ;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace( R.id.frameMain, listEntidadFragment).commit();
    }


}
