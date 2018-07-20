package ve.first.phl.com.phlcontrol;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.net.Uri;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import ve.first.phl.com.phlcontrol.Adapter.CentralSpinnerAdapter;
import ve.first.phl.com.phlcontrol.Model.Central;
import ve.first.phl.com.phlcontrol.Storage.DbHelper;
import ve.first.phl.com.phlcontrol.Storage.StorageUtils;


public class Actividad_Principal extends AppCompatActivity implements View.OnClickListener {

    boolean op1=true,op2=true,op3=true,op4=true,op5=true;
    private final int ADD_CENTRAL = 1;
    int ns=1;
    public String numero_a_llamar;
    private GoogleApiClient client;
    SoundPool soundPool;
    SoundPool.Builder spBuilder;
    AudioAttributes attributes;
    AudioAttributes.Builder aBuilder;
    int soundID;
    private DbHelper helper;
    private SQLiteDatabase db;
    Spinner mSpinner;
    Central[] mCentrals;

    @BindView(R.id.OpenBot) LinearLayout BotonAbrir;
    @BindView(R.id.callBot) LinearLayout BotonLlamar;
    @BindView(R.id.PuertaBot) LinearLayout BotonPuerta;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_principal);
        ButterKnife.bind(this);
        mSpinner = (Spinner) findViewById(R.id.spinner);
        BotonAbrir.setOnClickListener(this);
        BotonLlamar.setOnClickListener(this);
        BotonPuerta.setOnClickListener(this);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        crearSonido();
        cargardatos();
        requestPermission();


        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                if (position>0){
                    StorageUtils.setPrefSpcentral(Actividad_Principal.this,position-1);
                    numero_a_llamar = mCentrals[position-1].getNumero();
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


    }

    private void vibrar (int timev){
        final Vibrator vibe = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if (StorageUtils.getVibrar(this))
        vibe.vibrate(200);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) && op1){
            if (StorageUtils.getTeclaDw(Actividad_Principal.this)==9)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Desea Configurar La Tecla Volumen Bajo para:")
                        .setItems(R.array.volume, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                StorageUtils.setTeclaDw(Actividad_Principal.this,which);
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
            else
            {
                accion(StorageUtils.getTeclaDw(Actividad_Principal.this));
            }

        }

        if ((keyCode == KeyEvent.KEYCODE_VOLUME_UP) && op2){
            if (StorageUtils.getTeclaVup(Actividad_Principal.this)==9)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Desea Configurar La Tecla Volumen Bajo para:")
                        .setItems(R.array.volume, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                StorageUtils.setTeclaVup(Actividad_Principal.this,which);
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
            else
            {
                accion(StorageUtils.getTeclaDw(Actividad_Principal.this));
            }

        }

        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            finish();
        }



    return super.onKeyDown(keyCode, event);
    }


    private void accion(int valor) {

        switch (valor)
        {
            case 0:
                llamada();
                break;
            case 1:
                apertura_pprincipal();
                break;
            case 2:
                apertura_psecundaria();
                break;
            case 3:
                alarma();
                break;
            case 4:
                anti_sec();
                break;
            case 5:
                Toast.makeText(getApplicationContext(), "Puede configurar la tecla como acceso directo en el menú de configuración", Toast.LENGTH_LONG).show();
                break;
        }
    }


    private void apertura_psecundaria() {
        SmsManager sms2 = SmsManager.getDefault();
        if (!TextUtils.isEmpty(numero_a_llamar)) {
            vibrar(100);
            soundPool.play(soundID,1,1,0,0,1);
            sms2.sendTextMessage(numero_a_llamar, null, "#puerta", null, null);
            Toast.makeText(this,"Abriendo 2... Espere 10seg para volver a intentar",Toast.LENGTH_LONG).show();
            Timer timer = new Timer();
            Temporizador tempo = new Temporizador();
            BotonPuerta.setClickable(false);
            op1=false;
            timer.schedule(tempo, 10000);
        }
        else
        {
            agregarNuevoNum();
           // Toast.makeText(this, "Configure Número de la SmartKey-VE", Toast.LENGTH_SHORT).show();
        }
    }

    private void apertura_pprincipal() {
        SmsManager sms = SmsManager.getDefault();

        if (!TextUtils.isEmpty(numero_a_llamar)) {
            vibrar(100);
            soundPool.play(soundID,1,1,0,0,1);
            sms.sendTextMessage(numero_a_llamar, null, "#abrir", null, null);
            Toast.makeText(this,"Abriendo 1... Espere 10seg para volver a intentar o presione el boton de llamar",Toast.LENGTH_LONG).show();
            Timer timer = new Timer();
            op2=false;
            Temporizador tempo = new Temporizador();
            BotonAbrir.setClickable(false);
            timer.schedule(tempo, 10000);
        }
        else {
            agregarNuevoNum();
          //  Toast.makeText(this, "Configure Número de la SmartKey-VE", Toast.LENGTH_SHORT).show();
        }
    }

    private void llamada()
    {
        if (!TextUtils.isEmpty(numero_a_llamar)) {
            vibrar(100);
            soundPool.play(soundID,1,1,0,0,1);
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + numero_a_llamar));
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            if (op3)
            startActivity(intent);

        } else {
            agregarNuevoNum();
            //Toast.makeText(this, "Configure Número de la Central", Toast.LENGTH_SHORT).show();
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
        if (StorageUtils.getSonar(this))
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
        cargardatos();
        crearSonido();

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
            case R.id.config:
                abrirconfiguracion();
                break;
            case R.id.alarma:
                alarma();
                break;
            case R.id.apertura:
                anti_sec();
                break;
            case R.id.instrucciones:
                Intent config = new Intent(Actividad_Principal.this,instrucciones.class);
                config.putExtra("nuevo",true);
                startActivity(config);
                break;
            case R.id.Calificar:
                Uri uriUrl = Uri.parse("https://play.google.com/store/apps/details?id=ve.first.phl.com.phlcontrol");
                Intent intent = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void alarma ()
    {
     if (StorageUtils.getMsjAlarma(this))
     {
         AlertDialog.Builder builder = new AlertDialog.Builder(this);
         // Set the dialog title
         builder.setTitle(R.string.Alarma)
                 .setMultiChoiceItems(R.array.nomostrar, null,
                         new DialogInterface.OnMultiChoiceClickListener() {
                             @Override
                             public void onClick(DialogInterface dialog, int which,
                                                 boolean isChecked) {
                                 StorageUtils.setMsjAlarma(Actividad_Principal.this,isChecked);
                             }
                         })
                 // Set the action buttons
                 .setPositiveButton(R.string.Aceptar, new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int id) {
                         String msj,msjtoast;
                         msj="#Alarma";
                         msjtoast="Mensaje Enviado";
                         if (op4){
                             enviarsms(msj,msjtoast);
                            Timer timer = new Timer();
                            Temporizador tempo = new Temporizador();
                            op4=false;
                            Toast.makeText(getApplicationContext(),"Activando... Espere 10seg para volver a intentar",Toast.LENGTH_LONG).show();
                            timer.schedule(tempo, 10000);
                         }
                     }
                 })
                 .setNegativeButton(R.string.Cancelar, new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int id) {
                         vibrar(100);
                     }
                 });
         AlertDialog dialog = builder.create();
         dialog.show();
     }
        else
     {
         String msj,msjtoast;
         msj="#Alarma";
         msjtoast="Mensaje Enviado";
         if (op4){
             enviarsms(msj,msjtoast);
             Timer timer = new Timer();
             Temporizador tempo = new Temporizador();
             op4=false;
             Toast.makeText(getApplicationContext(),"Activando... Espere 10seg para volver a intentar",Toast.LENGTH_LONG).show();
             timer.schedule(tempo, 10000);}
     }
    }

    public void anti_sec()
    {

        if (StorageUtils.getMsjSecuestro(this))
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            // Set the dialog title
            builder.setTitle(R.string.AntiSecuestro)
                    .setMultiChoiceItems(R.array.nomostrar, null,
                            new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which,
                                                    boolean isChecked) {
                                    StorageUtils.setMsjSecuestro(Actividad_Principal.this,isChecked);
                                }
                            })
                    // Set the action buttons
                    .setPositiveButton(R.string.Aceptar, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            String msj,msjtoast;
                            msj="#Apertura";
                            msjtoast="Mensaje Enviado";
                            if (op5)
                            {
                            enviarsms(msj,msjtoast);
                            Timer timer = new Timer();
                            Temporizador tempo = new Temporizador();
                            op5=false;
                            Toast.makeText(getApplicationContext(),"Activando... Espere 10seg para volver a intentar",Toast.LENGTH_LONG).show();
                            timer.schedule(tempo, 10000);}
                        }
                    })
                    .setNegativeButton(R.string.Cancelar, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            vibrar(100);
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else
        {
            String msj,msjtoast;
            msj="#Apertura";
            msjtoast="Mensaje Enviado";
            if (op5)
            {enviarsms(msj,msjtoast);
            Timer timer = new Timer();
            Temporizador tempo = new Temporizador();
            op5=false;
            Toast.makeText(getApplicationContext(),"Activando... Espere 10seg para volver a intentar",Toast.LENGTH_LONG).show();
            timer.schedule(tempo, 10000);}
        }

    }

    public void enviarsms(String msj, String msjtoast){
        SmsManager sms = SmsManager.getDefault();

        if (!TextUtils.isEmpty(numero_a_llamar)) {
            vibrar(200);
            sms.sendTextMessage(numero_a_llamar, null, msj, null, null);
            Toast.makeText(this,msjtoast,Toast.LENGTH_SHORT).show();
        }
        else {
            agregarNuevoNum();
            //Toast.makeText(this, "Configure Número de la Central", Toast.LENGTH_SHORT).show();
        }
    }


    public void abrirconfiguracion(){
        vibrar(200);
        Intent config = new Intent(Actividad_Principal.this, ConfiguracionActivity.class);
        startActivity(config);

    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.callBot:
                llamada();
                break;
            case R.id.OpenBot:
                apertura_pprincipal();
                break;
            case R.id.PuertaBot:
                apertura_psecundaria();
                break;
        }
    }

    class Temporizador extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    BotonAbrir.setClickable(true);
                    BotonPuerta.setClickable(true);
                    op1=true;
                    op2=true;
                    op3=true;
                    op4=true;
                    op5=true;
                    Toast.makeText(getApplicationContext(),"PHL control listo para Abrir",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void cargardatos() {
        if(helper == null){

            helper = new DbHelper(this);
            db = helper.getWritableDatabase();
        }
        Central[] c = helper.getCentralesArray(db);
        if(c != null && c.length != 0){
            this.mCentrals = c;
            CentralSpinnerAdapter mAdapter = new CentralSpinnerAdapter(this,
                    android.R.layout.simple_spinner_item, mCentrals);
            mSpinner.setAdapter(mAdapter);

        }

        if (c.length==1)
        {numero_a_llamar = mCentrals[0].getNumero();
         StorageUtils.setPrefSpcentral(this,0);
        }


        if (StorageUtils.getSpCentral(Actividad_Principal.this)<100)
        numero_a_llamar = mCentrals[StorageUtils.getSpCentral(Actividad_Principal.this)].getNumero();
    }

    public void requestPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    201);
        }

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    202);
        }

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.VIBRATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.VIBRATE},
                    203);
        }
    }

    private void agregarNuevoNum(){
        Intent intent = new Intent(this,registro.class);
        intent.putExtra("nuevo",true);
        startActivityForResult(intent,ADD_CENTRAL);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            cargardatos();
        }
    }
}


