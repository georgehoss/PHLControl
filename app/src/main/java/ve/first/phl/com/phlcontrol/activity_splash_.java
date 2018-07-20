package ve.first.phl.com.phlcontrol;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import ve.first.phl.com.phlcontrol.Model.Central;
import ve.first.phl.com.phlcontrol.Storage.DbHelper;

import static android.R.attr.delay;
import static java.lang.Thread.sleep;

public class activity_splash_ extends AppCompatActivity {

    private DbHelper helper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_);
        if(helper == null){

            helper = new DbHelper(this);
            db = helper.getWritableDatabase();
        }
        Central[] c = helper.getCentralesArray(db);
        if (c.length>0)
        {

            Timer timer = new Timer();
            Temporizador1 tempo = new Temporizador1();
            timer.schedule(tempo, 1000);
        }
        else{
        Timer timer = new Timer();
        Temporizador1 tempo = new Temporizador1();
        timer.schedule(tempo, 2000);}
    }

    class Temporizador1 extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Intent config = new Intent(activity_splash_.this, Actividad_Principal.class);
                    startActivity(config);
                }
            });
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
