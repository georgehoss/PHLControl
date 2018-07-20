package ve.first.phl.com.phlcontrol.Storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ve.first.phl.com.phlcontrol.Model.Central;

/**
 * Created by George on 28/04/2017.
 */

public class DbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "phlcontrol.db";

    private static final String SQL_CREATE_CENTRAL =
            "CREATE TABLE " + CentralEntry.TABLE_NAME + " (" +
                    CentralEntry._ID + " INTEGER PRIMARY KEY," +
                    CentralEntry.ID + " TEXT UNIQUE," +
                    CentralEntry.NAME + " TEXT)";

    private static final String SQL_DELETE_CENTRAL =
            "DROP TABLE IF EXISTS " + CentralEntry.TABLE_NAME;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CENTRAL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_CENTRAL);
        onCreate(db);
    }

    public long insertCentral (SQLiteDatabase db, Central central){
        ContentValues values = new ContentValues();
        values.put(CentralEntry.ID,central.getNumero());
        values.put(CentralEntry.NAME,central.getNombre());
        if (!db.isOpen())
            db = this.getWritableDatabase();

        long newRowId = db.insert(CentralEntry.TABLE_NAME, null, values);
        Log.d("Debug Storage", newRowId+"");
        db.close();
        return newRowId;
    }


    public ArrayList<Central> getCentrales(SQLiteDatabase db){

        String projection[] = {
                CentralEntry.ID,CentralEntry.NAME
        };

        String sortOrder = CentralEntry._ID + " ASC";
        if(!db.isOpen()){
            db = this.getWritableDatabase();
        }


        Cursor c = db.query(
                CentralEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        ArrayList<Central> list = new ArrayList<>();

        if (c != null && c.moveToFirst()) {
            do {
                Central central =  new Central(
                        c.getString(c.getColumnIndex(CentralEntry.NAME)),
                        c.getString(c.getColumnIndex(CentralEntry.ID))

                );
                Log.d("PHLdegbug",central.getNombre()+" "+central.getNumero());
                list.add(central);
            }while (c.moveToNext());

            c.close();
        }

        return list;
    }

    public Central[] getCentralesArray(SQLiteDatabase db){

        String projection[] = {
                CentralEntry.ID,CentralEntry.NAME,
        };

        String sortOrder = CentralEntry._ID + " ASC";
        if(!db.isOpen()){
            db = this.getWritableDatabase();
        }


        Cursor c = db.query(
                CentralEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        Central[] list = new Central[c.getCount()];
        Central central;
        int index=0;
        if (c != null && c.moveToFirst()) {
            do {
                central =  new Central(
                        c.getString(c.getColumnIndex(CentralEntry.NAME)),
                        c.getString(c.getColumnIndex(CentralEntry.ID))

                );
                Log.d("PHLdegbug",central.getNombre()+" "+central.getNumero());
                list[index]=central;
                index++;
            }while (c.moveToNext());

            c.close();
        }

        return list;
    }

    public long upadteCentrales(SQLiteDatabase db, Central central, String numero){
        ContentValues values = new ContentValues();
        values.put(CentralEntry.NAME,central.getNombre());
        values.put(CentralEntry.ID,central.getNumero());
        String where = CentralEntry.ID + " = ?";
        String whereArgs[] = {numero + ""};

        if(!db.isOpen()){
            db = this.getWritableDatabase();
        }

        long affectedRows = db.update(
                CentralEntry.TABLE_NAME,
                values,
                where,
                whereArgs);
        db.close();

        return affectedRows;

        }


    public int deleteCentral(SQLiteDatabase db, Central central) {
        String where = CentralEntry.ID + " = ?";
        String[] whereArgs = {central.getNumero()+""};
        if(!db.isOpen()){
            db = this.getWritableDatabase();
        }
        int affectedRows = db.delete(CentralEntry.TABLE_NAME, where, whereArgs);
        db.close();
        return affectedRows;
    }


    public static class CentralEntry implements BaseColumns {
        private static final String TABLE_NAME = "central";
        private static final String ID = "id";
        public static final  String NAME = "name";
    }
}

