package ve.first.phl.com.phlcontrol;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
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
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;
import ve.first.phl.com.phlcontrol.Control.ControlFragment;
import ve.first.phl.com.phlcontrol.Control.ControlListFragment;
import ve.first.phl.com.phlcontrol.Utils.StorageUtils;




public class MainActivity extends AppCompatActivity {
    public static final int MULTIPLE_PERMISSIONS = 10; // code you want.
    String[] permissions= new String[]{
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.CALL_PHONE
           // Manifest.permission.SEND_SMS
            };

    @BindView(R.id.bottom_navigation) BottomNavigationView mBtNavigation;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //setVolumeControlStream(AudioManager.STREAM_MUSIC);
        //crearSonido();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new ControlFragment()).commit();
        MobileAds.initialize(this,"ca-app-pub-2647255604635326~1427373068");

        mBtNavigation.setSelectedItemId(R.id.action_remote);

        mBtNavigation.setOnNavigationItemSelectedListener(menuItem -> {



            switch (menuItem.getItemId())
            {
                case R.id.action_list:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new ControlListFragment()).commit();
                    return true;
                case R.id.action_remote:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new ControlFragment()).commit();
                    return true;
                case R.id.action_add:
                    Intent intent = new Intent(MainActivity.this, AddActivity.class);
                    intent.putExtra("new","new");
                    startActivity(intent);
                    break;
            }
            return false;
        });

    }

    private void startVibration() {
        final Vibrator vibe = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibe.vibrate(200);
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
        ///*

            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null));
                intent.putExtra("sms_body",msj);
                startActivity(intent);
            }
            catch (Exception e)
            {
                Toast.makeText(this, "Tu dispositivo no es compatible, te invitamos a descargar la version de mensajes automáticos", Toast.LENGTH_LONG).show();
                showWarningDialog(null,null);
            }
        //*/
        /*SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(number, null, msj, null, null);
        //*/
    }


    public void openSecundary(String numero) {
        String msj="#puerta";
        if (!TextUtils.isEmpty(numero)) {
            startVibration();

            //sendSms(numero,msj);

            if (!StorageUtils.notToShowDialgo(this))
                showWarningDialog(numero,msj);
            else
                sendSms(numero,msj);
            //*/

        }

    }

    public void openPrincipal(String numero) {
        String msj="#Abrir";

        if (!TextUtils.isEmpty(numero)) {
            startVibration();
            //sendSms(numero,msj);

            if (!StorageUtils.notToShowDialgo(this))
                showWarningDialog(numero,msj);
            else
                sendSms(numero,msj);
            //*/

        }

    }

    public void activateAlarm(String numero) {

        String msj= "#ALARM";

        if (!TextUtils.isEmpty(numero)) {
            startVibration();
            //sendSms(numero,msj);

            if (!StorageUtils.notToShowDialgo(this))
                showWarningDialog(numero,msj);
            else
                sendSms(numero,msj);
            //*/


            startVibration();
        }

    }

    public void activateEmergency(String numero) {
        String msj = "#APERTURA";

        if (!TextUtils.isEmpty(numero)) {
            startVibration();
            //sendSms(numero,msj);

            if (!StorageUtils.notToShowDialgo(this))
                showWarningDialog(numero,msj);
            else
                sendSms(numero,msj);
            //*/

            startVibration();
        }

    }

    public void callToPrincipal(String numero) {
        if (!TextUtils.isEmpty(numero)) {
            startVibration();
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
                break;
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
        builder.setPositiveButton("Aceptar", (dialog, which) -> {
        });
        builder.setMessage("Para Activar la Alarma de la Llave GSM mantenga presionado el logo de Tecnología PHL por más de dos segundos." +
                "\r\nSi mantiene presionado el Botón Principal por más de 2 segundos se activará la Alarma y la salida principal de la LLave GSM al mismo tiempo." +
                "\r\nEl modo Alarma debe esta previamente configurado en la Llave GSM");
        return builder.create();
    }

    private void checkPermissions() {
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
        }
    }

/*

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissionsList[], @NonNull int[] grantResults) {
        if (requestCode == MULTIPLE_PERMISSIONS) {
            if (grantResults.length > 0) {
                int i = 0;
                for (String ignored : permissionsList) {
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {

                    }
                    i++;
                }

            }
        }
    }
*/



}


