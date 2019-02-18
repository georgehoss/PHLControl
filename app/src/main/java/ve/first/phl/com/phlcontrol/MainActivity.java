package ve.first.phl.com.phlcontrol;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;
import ve.first.phl.com.phlcontrol.Control.ControlFragment;


public class MainActivity extends AppCompatActivity {
    public static final int MULTIPLE_PERMISSIONS = 10; // code you want.
    SoundPool soundPool;
    SoundPool.Builder spBuilder;
    AudioAttributes attributes;
    AudioAttributes.Builder aBuilder;
    int soundID;
    String[] permissions= new String[]{
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.SEND_SMS};

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.actividad_principal);
        ButterKnife.bind(this);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        crearSonido();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new ControlFragment()).commit();


    }

    private void vibrar(int timev) {
        final Vibrator vibe = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibe.vibrate(200);
    }




    public void abrirSecundario(String numero) {
        SmsManager sms2 = SmsManager.getDefault();
        if (!TextUtils.isEmpty(numero)) {
            vibrar(100);
            sms2.sendTextMessage(numero, null, "#puerta", null, null);
        }

    }

    public void abrirPrincipal(String numero) {
        SmsManager sms = SmsManager.getDefault();

        if (!TextUtils.isEmpty(numero)) {
            vibrar(100);
            sms.sendTextMessage(numero, null, "#abrir", null, null);
        }

    }

    public void activateAlarm(String numero) {
        SmsManager sms = SmsManager.getDefault();

        if (!TextUtils.isEmpty(numero)) {
            vibrar(50);
            sms.sendTextMessage(numero, null, "#ALARM", null, null);
            vibrar(50);
        }

    }

    public void activateEmergency(String numero) {
        SmsManager sms = SmsManager.getDefault();

        if (!TextUtils.isEmpty(numero)) {
            vibrar(50);
            sms.sendTextMessage(numero, null, "#APERTURA", null, null);
            vibrar(50);
        }

    }

    public void llamadaPrincipal(String numero) {
        if (!TextUtils.isEmpty(numero)) {
            vibrar(100);
            soundPool.play(soundID,1,1,0,0,1);
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + numero));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(intent);
        }
    }


    private void crearSonido() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            aBuilder = new AudioAttributes.Builder();
            aBuilder.setUsage(AudioAttributes.USAGE_MEDIA);
            aBuilder.setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION);
            attributes=aBuilder.build();

            spBuilder = new SoundPool.Builder();
            spBuilder.setAudioAttributes(attributes);
            soundPool = spBuilder.build();}
        else
        {
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC,0);
        }

        soundID = soundPool.load(this,R.raw.opentone,1);
    }

    @Override
    protected void onPause() {
        super.onPause();
        soundPool.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        crearSonido();
        checkPermissions();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.nuevo:
                Intent intent = new Intent(this, AddActivity.class);
                intent.putExtra("new","new");
                startActivity(intent);
                break;
            case R.id.alarma:
                dialogAlarm().show();
                break;
            case R.id.Calificar:
                Uri uriUrl = Uri.parse("https://play.google.com/store/apps/details?id=ve.first.phl.com.phlcontrol");
                Intent intent2 = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(intent2);
        }
        return super.onOptionsItemSelected(item);
    }

    private Dialog dialogAlarm(){

        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Alarma o Botón de Pánico");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setMessage("Para Activar la Alarma de la Llave GSM mantenga presionado el logo de Tecnología PHL por más de dos segundos." +
                "\r\nSi mantiene presionado el Botón Principal por más de 2 segundos se activará la Alarma y la salida principal de la LLave GSM al mismo tiempo." +
                "\r\nEl modo Alarma debe esta previamente configurado en la Llave GSM");
        return builder.create();
    }

    private  boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p:permissions) {
            result = ContextCompat.checkSelfPermission(this,p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),MULTIPLE_PERMISSIONS );
            return false;
        }
        return true;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissionsList[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS:{
                if (grantResults.length > 0) {
                    boolean garanted=true;
                    int i=0;
                    for (String per : permissionsList) {
                        if(grantResults[i] == PackageManager.PERMISSION_DENIED){
                            garanted=false;

                        }
                        i++;
                    }

                }

            }
        }
    }




}


