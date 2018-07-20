package ve.first.phl.com.phlcontrol.Storage;

import android.content.Context;
import android.content.SharedPreferences;

import java.security.PublicKey;

/**
 * Created by Javier on 28/04/2017.
 */

public class StorageUtils {

    private static final String PREF_NOMBRE ="nombrec1";
    private static final String PREF_NUMERO = "numero";
    private static final String PREF_VOLUP ="volumeup";
    private static final String PREF_VOLDOWN = "volumedw";
    private static final String PREF_VIBRAR ="vibra";
    private static final String PREF_SUENA = "suena";
    private static final String PREF_SECUESTRO ="msjantiss";
    private static final String PREF_ALARMA = "msjalarmaa";
    private static final String PREF_SPCENTRAL = "central";

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences("datosdesistema", Context.MODE_PRIVATE);
    }

    public static String getNumero(Context ctx){
        SharedPreferences pref = getSharedPreferences(ctx);
        return pref.getString(PREF_NUMERO,"");

    }

    public static void setNumero (Context ctx){
        SharedPreferences pref = getSharedPreferences(ctx);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(PREF_NUMERO,"");
        editor.commit();
    }

    public static String getNombre(Context ctx){
        SharedPreferences pref = getSharedPreferences(ctx);
        return pref.getString(PREF_NOMBRE,"");

    }

    public static void setNombre (Context ctx){
        SharedPreferences pref = getSharedPreferences(ctx);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(PREF_NOMBRE,"");
        editor.commit();
    }

    public static int getTeclaVup(Context ctx){
        SharedPreferences pref = getSharedPreferences(ctx);
        return pref.getInt(PREF_VOLUP,9);
    }

    public static void setTeclaVup(Context ctx,int posicion){
        SharedPreferences pref = getSharedPreferences(ctx);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(PREF_VOLUP,posicion);
        editor.commit();

    }

    public static int getTeclaDw(Context ctx){
        SharedPreferences pref = getSharedPreferences(ctx);
        return pref.getInt(PREF_VOLDOWN,9);
    }

    public static void setTeclaDw(Context ctx,int posicion){
        SharedPreferences pref = getSharedPreferences(ctx);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(PREF_VOLDOWN,posicion);
        editor.commit();

    }

    public static int getSpCentral(Context ctx){
        SharedPreferences pref = getSharedPreferences(ctx);
        return pref.getInt(PREF_SPCENTRAL,100);
    }

    public static void setPrefSpcentral(Context ctx,int posicion){
        SharedPreferences pref = getSharedPreferences(ctx);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(PREF_SPCENTRAL,posicion);
        editor.commit();

    }
    public static Boolean getVibrar(Context ctx){
        SharedPreferences pref = getSharedPreferences(ctx);
        return pref.getBoolean(PREF_VIBRAR,true);
    }

    public static void setVibrar(Context ctx,boolean valor){
        SharedPreferences pref = getSharedPreferences(ctx);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(PREF_VIBRAR,valor);
        editor.commit();

    }

    public static Boolean getSonar(Context ctx){
        SharedPreferences pref = getSharedPreferences(ctx);
        return pref.getBoolean(PREF_SUENA,true);
    }


    public static void setSonar(Context ctx,boolean valor){
        SharedPreferences pref = getSharedPreferences(ctx);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(PREF_SUENA,valor);
        editor.commit();

    }

    public static Boolean getMsjAlarma(Context ctx){
        SharedPreferences pref = getSharedPreferences(ctx);
        return pref.getBoolean(PREF_ALARMA,true);
    }


    public static void setMsjAlarma(Context ctx,boolean valor){
        SharedPreferences pref = getSharedPreferences(ctx);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(PREF_ALARMA,valor);
        editor.commit();

    }

    public static Boolean getMsjSecuestro(Context ctx){
        SharedPreferences pref = getSharedPreferences(ctx);
        return pref.getBoolean(PREF_SECUESTRO,true);
    }


    public static void setMsjSecuestro(Context ctx,boolean valor){
        SharedPreferences pref = getSharedPreferences(ctx);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(PREF_SECUESTRO,valor);
        editor.commit();

    }


}
