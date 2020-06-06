package app.factory.appgastos.datos;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MovimientoDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MovimientoIE.db";

    public MovimientoDbHelper(@Nullable Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //CREAR LA TABLA MOVIMIENTO
        db.execSQL("CREATE TABLE "+ GastosDB.GastosColumnsDB.TABLE_NAME_MOVIMIENTO + " ("
                + GastosDB.GastosColumnsDB.IdMovimiento + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + GastosDB.GastosColumnsDB.IdEntidad + " INTEGER NOT NULL,"
                + GastosDB.GastosColumnsDB.FechaMovimiento + " DATETIME NOT NULL," //"YYYY-MM-DD HH:MM:SS.SSS"
                + GastosDB.GastosColumnsDB.IdTipoMovimiento + " VARCHAR(1) NOT NULL,"
                + GastosDB.GastosColumnsDB.Descripcion + " VARCHAR(100) NOT NULL,"
                + GastosDB.GastosColumnsDB.Importe + " DECIMAL NOT NULL)"
        );

        //CREAR LA TABLA ENTIDAD
        db.execSQL("CREATE TABLE "+ GastosDB.GastosColumnsDB.TABLE_NAME_ENTIDAD + " ("
                + GastosDB.GastosColumnsDB.IdEntidad + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + GastosDB.GastosColumnsDB.Entidad + " VARCHAR(100) NOT NULL,"
                + GastosDB.GastosColumnsDB.FechaRegistro + " DATETIME NOT NULL," //"YYYY-MM-DD HH:MM:SS.SSS"
                + GastosDB.GastosColumnsDB.Estado + " CHAR(1) NOT NULL)"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
