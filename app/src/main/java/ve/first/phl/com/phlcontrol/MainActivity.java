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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;


import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;
import ve.first.phl.com.phlcontrol.Control.ControlFragment;
import ve.first.phl.com.phlcontrol.Utils.StorageUtils;


public class MainActivity extends AppCompatActivity {
    public static final int MULTIPLE_PERMISSIONS = 10; // code you want.
    String[] permissions= new String[]{
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.CALL_PHONE,
            };

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.actividad_principal);
        ButterKnife.bind(this);
        //setVolumeControlStream(AudioManager.STREAM_MUSIC);
        //crearSonido();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new ControlFragment()).commit();
       // MobileAds.initialize(this,"ca-app-pub-2647255604635326~7118853954");

    }

    private void vibrar(int timev) {
        //final Vibrator vibe = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            //vibe.vibrate(200);
    }

    public void showWarningDialog(String number,String msg){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_advice, null);
        final CheckBox cbShow = view.findViewById(R.id.cb_no_mostrar);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(true);

        cbShow.setOnCheckedChangeListener((buttonView, isChecked) -> StorageUtils.saveNoShow(MainActivity.this,isChecked));

        if (number!=null && msg!=null) {
            view.findViewById(R.id.bt_accept).setOnClickListener(v -> {
                dialog.dismiss();
                sendSms(number, msg);
            });
        }
        else
            view.findViewById(R.id.bt_accept).setVisibility(View.GONE);

        view.findViewById(R.id.bt_download).setOnClickListener(v -> {
            Uri uriUrl = Uri.parse("https://drive.google.com/open?id=1AckHQswTa3PMqGD9T5K9B2gxdmTDFR7f");
            Intent intent2 = new Intent(Intent.ACTION_VIEW, uriUrl);
            startActivity(intent2);
            dialog.dismiss();
        });

        dialog.show();


    }

    public void sendSms(String number,String msj){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) // At least KitKat
        {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setData(Uri.parse("smsto:"));
            intent.putExtra("sms_body", msj);
            try {
                startActivity(intent);
            }
            catch (Exception e)
            {
                Toast.makeText(this, "Tu dispositivo no es compatible, te invitamos a descargar la version de mensajes automáticos", Toast.LENGTH_LONG).show();
                showWarningDialog(null,null);
            }
        }
        else // For early versions, do what worked for you before.
        {
            Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
            smsIntent.setType("vnd.android-dir/mms-sms");
            smsIntent.putExtra("address",number);
            smsIntent.putExtra("sms_body",msj);
            startActivity(smsIntent);
        }
    }


    public void abrirSecundario(String numero) {
        //SmsManager sms2 = SmsManager.getDefault();
        String msj="#puerta";
        if (!TextUtils.isEmpty(numero)) {
            vibrar(100);
            //if (StorageUtils.notToShowDialgo(this))
            if (StorageUtils.notToShowDialgo(this))
                showWarningDialog(numero,msj);
            else
                showWarningDialog(numero,msj);

            //sms2.sendTextMessage(numero, null, "#puerta", null, null);
        }

    }

    public void abrirPrincipal(String numero) {
        //SmsManager sms = SmsManager.getDefault();
        String msj="#Abrir";

        if (!TextUtils.isEmpty(numero)) {
            vibrar(100);
            if (StorageUtils.notToShowDialgo(this))
                sendSms(numero,msj);
            else
                showWarningDialog(numero,msj);

            //sms.sendTextMessage(numero, null, "#abrir", null, null);
        }

    }

    public void activateAlarm(String numero) {
        //SmsManager sms = SmsManager.getDefault();
        String msj= "#ALARM";

        if (!TextUtils.isEmpty(numero)) {
            vibrar(50);
            //sms.sendTextMessage(numero, null, "#ALARM", null, null);
            if (StorageUtils.notToShowDialgo(this))
                sendSms(numero,msj);
            else
                showWarningDialog(numero,msj);

            vibrar(50);
        }

    }

    public void activateEmergency(String numero) {
        //SmsManager sms = SmsManager.getDefault();
        String msj = "#APERTURA";

        if (!TextUtils.isEmpty(numero)) {
            vibrar(50);
            //sms.sendTextMessage(numero, null, "#APERTURA", null, null);
            if (StorageUtils.notToShowDialgo(this))
                sendSms(numero,msj);
            else
                showWarningDialog(numero,msj);

            vibrar(50);
        }

    }

    public void llamadaPrincipal(String numero) {
        if (!TextUtils.isEmpty(numero)) {
            //vibrar(100);
            //soundPool.play(soundID,1,1,0,0,1);
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + numero));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(intent);
        }
    }



    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //crearSonido();
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
            case R.id.m_nuevo:
                Intent intent = new Intent(this, AddActivity.class);
                intent.putExtra("new","new");
                startActivity(intent);
                break;
            case R.id.m_alarma:
                dialogAlarm().show();
                break;
            case R.id.m_calificar:
                Uri uriUrl = Uri.parse("https://play.google.com/store/apps/details?id=ve.first.phl.com.phlcontrol");
                Intent intent2 = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(intent2);
            case R.id.m_compartir:
                Intent share = new Intent(android.content.Intent.ACTION_SEND);
                share.setType("text/plain");
                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

                // Add data to the intent, the receiving app will decide
                // what to do with it.
                share.putExtra(Intent.EXTRA_SUBJECT, "PHL Control");
                share.putExtra(Intent.EXTRA_TEXT, "Descarga PHL Control directamente de la Google Play\n\n https://play.google.com/store/apps/details?id=ve.first.phl.com.phlcontrol");

                startActivity(Intent.createChooser(share, "PHL Control"));
                break;
            case R.id.m_descargar:
                showWarningDialog(null,null);
                break;

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


