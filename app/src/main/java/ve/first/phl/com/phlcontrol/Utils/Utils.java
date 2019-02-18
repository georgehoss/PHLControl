package ve.first.phl.com.phlcontrol.Utils;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ghoss on 31/07/2018.
 */
public class Utils {

    public static String getDateString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd-MM-yy");
        return dateFormat.format(new Date());
    }


    public static boolean isUpToDate (Context context, String timetoreview, int timeseg)  {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd-MM-yy");
        Date horaAct = null;
        try {
            horaAct = dateFormat.parse(Utils.getDateString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date horaEnvio = new Date();
        try {
            horaEnvio.setTime(dateFormat.parse(timetoreview).getTime() + (timeseg*1000));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return horaAct.after(horaEnvio);

    }
}
