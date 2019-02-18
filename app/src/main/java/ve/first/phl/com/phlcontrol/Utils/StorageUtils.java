package ve.first.phl.com.phlcontrol.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ghoss on 31/07/2018.
 */
public class StorageUtils {
    private static SharedPreferences getSharedPref(Context context) {
        return context.getSharedPreferences("pref", Context.MODE_PRIVATE);
    }

    public static void saveCentralPage(Context context, int position){
        SharedPreferences pref = getSharedPref(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("central_page",position);
        editor.commit();
    }

    public static int getCentralPage(Context context){
        return getSharedPref(context).getInt("central_page",0);
    }
}
