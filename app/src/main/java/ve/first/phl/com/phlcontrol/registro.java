package ve.first.phl.com.phlcontrol;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ve.first.phl.com.phlcontrol.Model.Central;
import ve.first.phl.com.phlcontrol.Storage.DbHelper;
import ve.first.phl.com.phlcontrol.Storage.StorageUtils;

public class registro extends AppCompatActivity {

    private SQLiteDatabase mDb;
    private DbHelper mHelper;
    private int estado;
    private Boolean nuevo;
    private String  mnumero;

    @BindView(R.id.etnombrecentral)
    EditText mEt_nombre;
    @BindView(R.id.etnumcentral)
    EditText mEt_numero;

    @OnClick (R.id.btguardar) void guardar ()
    {
        String nombre = mEt_nombre.getText().toString();
        String numero = mEt_numero.getText().toString();
        Central central = new Central(nombre,numero);

        if (!numero.isEmpty() && !nombre.isEmpty())
        {
            if (numero.length()>=6)
            {
                saveCentral(central,nuevo);
                Intent intent = getIntent();
                setResult(RESULT_OK,intent);
                StorageUtils.setNombre(registro.this);
                StorageUtils.setNumero(registro.this);
                finish();
            }
            else
                mEt_numero.setError("El número debe tener más de 5 digitos");
        }

        if(numero.isEmpty())
            mEt_numero.setError("El numero no puede estar vacío");
        if(nombre.isEmpty())
            mEt_nombre.setError("El nombre no puede estar vacío");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        ButterKnife.bind(this);
        nuevo = getIntent().getExtras().getBoolean("nuevo");
        mHelper = new DbHelper(registro.this);
        mDb = mHelper.getWritableDatabase();

        String numero = StorageUtils.getNumero(this);
        if (!numero.isEmpty())
        {
            mEt_numero.setText(numero);
            String nombre = StorageUtils.getNombre(this);
            mEt_nombre.setText(nombre);
        }

        if (!nuevo)
        {
            mnumero =getIntent().getExtras().getString("numero");
            mEt_numero.setText(mnumero);
            mEt_nombre.setText(getIntent().getExtras().getString("nombre"));
        }


    }

    private void saveCentral (Central central,  Boolean nuevo)
    {
        if (nuevo) mHelper.insertCentral(mDb,central);

        else mHelper.upadteCentrales(mDb,central,mnumero);

    }
}
