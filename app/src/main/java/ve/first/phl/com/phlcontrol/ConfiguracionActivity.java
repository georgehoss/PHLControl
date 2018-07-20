package ve.first.phl.com.phlcontrol;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ve.first.phl.com.phlcontrol.Adapter.CentralAdapter;
import ve.first.phl.com.phlcontrol.Model.Central;
import ve.first.phl.com.phlcontrol.Storage.DbHelper;
import ve.first.phl.com.phlcontrol.Storage.StorageUtils;


public class ConfiguracionActivity extends AppCompatActivity implements
        CentralAdapter.OnNotifyChange, CompoundButton.OnCheckedChangeListener

{
    private final int ADD_CENTRAL = 1;
    private SQLiteDatabase mDb;
    private DbHelper mHelper;
    private CentralAdapter mAdapter;
    ImageButton link;
    String nombre;

    @BindView(R.id.RvCentrales)
    RecyclerView mRv_Centrales;

    @BindView(R.id.SwVibrar) Switch mSwVibrar;
    @BindView(R.id.SwSonido) Switch mSwsonido;
    @BindView(R.id.SWalarma) Switch mSwAlarma;
    @BindView(R.id.SWantis) Switch mSwAntis;


    @OnClick(R.id.agregar_disp) void agregarCentral(){
        Vibrador();
        Intent intent = new Intent(ConfiguracionActivity.this,registro.class);
        intent.putExtra("nuevo",true);
        startActivityForResult(intent,ADD_CENTRAL);
    }

    @OnClick(R.id.btn_volumenup) void volumeUp(){

        Vibrador();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Desea Configurar la Tecla Volumen (+) para:")
                .setItems(R.array.volume, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        StorageUtils.setTeclaVup(ConfiguracionActivity.this,which);
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @OnClick(R.id.btn_volumendw) void volumeDw(){
        Vibrador();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Desea Configurar la Tecla Volumen (-) para:")
                .setItems(R.array.volume, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        StorageUtils.setTeclaDw(ConfiguracionActivity.this,which);
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);
        ButterKnife.bind(this);
        mHelper = new DbHelper(this);
        mDb = mHelper.getReadableDatabase();
        ArrayList<Central> centrals = mHelper.getCentrales(mDb);


        mAdapter = new CentralAdapter(centrals,this);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRv_Centrales.setLayoutManager(mLayoutManager);
        mRv_Centrales.setAdapter(mAdapter);
        cargardatos();
        mSwAntis.setOnCheckedChangeListener(this);
        mSwAlarma.setOnCheckedChangeListener(this);
        mSwVibrar.setOnCheckedChangeListener(this);
        mSwsonido.setOnCheckedChangeListener(this);


        if (centrals.isEmpty())
        {
            {
                Intent intent = new Intent(ConfiguracionActivity.this,registro.class);
                intent.putExtra("nuevo",true);
                startActivityForResult(intent,ADD_CENTRAL);
            }

        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_configuracion, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.AcercaDe:
                Intent config = new Intent(ConfiguracionActivity.this, instrucciones.class);
                startActivity(config);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void User()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Desea Guardar los Cambios?").setTitle("Datos del Programa");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void cargardatos() {

        mSwVibrar.setChecked(StorageUtils.getVibrar(ConfiguracionActivity.this));
        mSwsonido.setChecked(StorageUtils.getSonar(ConfiguracionActivity.this));
        mSwAlarma.setChecked(StorageUtils.getMsjAlarma(ConfiguracionActivity.this));
        mSwAntis.setChecked(StorageUtils.getMsjSecuestro(ConfiguracionActivity.this));
    }


    public void golink(View view) {
        //Uri uriUrl = Uri.parse("http://www.instagram.com/tecnologiaphl");
        //Intent intent = new Intent(Intent.ACTION_VIEW, uriUrl);
       // startActivity(intent);

        Uri uri = Uri.parse("http://www.instagram.com/tecnologiaphl");
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

        likeIng.setPackage("com.instagram.android");

        try {
            startActivity(likeIng);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://instagram.com/tecnologiaphl")));
        }

    }


    private void Vibrador(){
        final Vibrator vibe = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if (StorageUtils.getVibrar(ConfiguracionActivity.this))
            vibe.vibrate(50);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ArrayList<Central> list = mHelper.getCentrales(mDb);
        if (resultCode == RESULT_OK)
        {

            mAdapter.setmCentralList(list);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void noItems() {

    }

    @Override
    public void onItemEdited(int position) {
        String numero = mAdapter.getmCentralList().get(position).getNumero();
        String nombre = mAdapter.getmCentralList().get(position).getNombre();
        Intent intent = new Intent(ConfiguracionActivity.this,registro.class);
        intent.putExtra("nombre",nombre);
        intent.putExtra("numero",numero);
        intent.putExtra("nuevo",false);
        startActivityForResult(intent,ADD_CENTRAL);
    }

    @Override
    public void onItemRemoved(final int position) {

        final String numero = mAdapter.getmCentralList().get(position).getNumero();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Desea Eliminar la central:"+numero)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mHelper.deleteCentral(mDb,mHelper.getCentrales(mDb).get(position));
                        mAdapter.getmCentralList().remove(position);
                        mAdapter.notifyDataSetChanged();
                        StorageUtils.setPrefSpcentral(ConfiguracionActivity.this,100);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @Override
    public void onItemClick(int position) {


    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId())
        {
            case R.id.SwSonido:
                StorageUtils.setSonar(ConfiguracionActivity.this,b);
                break;
            case R.id.SwVibrar:
                StorageUtils.setVibrar(ConfiguracionActivity.this,b);
                break;
            case R.id.SWalarma:
                StorageUtils.setMsjAlarma(ConfiguracionActivity.this,b);
                break;
            case R.id.SWantis:
                StorageUtils.setMsjSecuestro(ConfiguracionActivity.this,b);
                break;
        }
    }
}



//