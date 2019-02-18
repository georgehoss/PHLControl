package ve.first.phl.com.phlcontrol;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import ve.first.phl.com.phlcontrol.Model.Central;

public class AddActivity extends AppCompatActivity {

    private static final int PICK_CONTACT = 101;
    private Realm realm;
    private Central central;
    private boolean edit=false;

    @BindView(R.id.et_name) EditText mEtName;
    @BindView(R.id.et_numero)EditText mEtNumber;
    @BindView(R.id.bt_contacts) Button mBtContacts;
    @BindView(R.id.ll_edit) LinearLayout mLlEdit;
    @BindView(R.id.et_bt1) EditText mEtBt1;
    @BindView(R.id.et_bt2) EditText mEtBt2;
    @BindView(R.id.sw_bt2) Switch mSwBt2;
    @BindView(R.id.tv_info) TextView mTvInfo;
    @BindView(R.id.til_3) TextInputLayout mTil3;


    @OnClick (R.id.bt_contacts) void onPickContact(){
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT);

    }

    @OnClick (R.id.fb_add) void onSave(){
        if (validate(mEtNumber.getText().toString(),mEtName.getText().toString()))
            saveNumber(mEtNumber.getText().toString(),mEtName.getText().toString());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        ButterKnife.bind(this);
        Realm.init(this);
        realm = Realm.getDefaultInstance();
       if (getIntent().getExtras()!=null && getIntent().getExtras().get("central")!=null)
       {
           central = realm.where(Central.class).equalTo("numero",getIntent().getExtras().getString("central")).findFirst();
           if (central!=null)
           {
               mEtName.setText(central.getNombre());
               if (central.getNumero()!=null && !central.getNumero().isEmpty()){
                   mEtNumber.setText(central.getNumero());
                   mEtNumber.setEnabled(false);
                   edit = true;
                   mBtContacts.setVisibility(View.GONE);
                   mLlEdit.setVisibility(View.VISIBLE);
                   mSwBt2.setChecked(central.isBtSec());
                   mTvInfo.setVisibility(View.GONE);
                   if (central.getNamebt1()!=null && !central.getNamebt1().isEmpty())
                       mEtBt1.setText(central.getNamebt1());
                   else
                       mEtBt1.setText(getString(R.string.principal));
                   if (central.getNamebt2()!=null && !central.getNamebt2().isEmpty())
                       mEtBt2.setText(central.getNamebt2());
                   else
                       mEtBt2.setText(getString(R.string.secundario));
                   mSwBt2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                       @Override
                       public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                           if (!isChecked)
                               mTil3.setVisibility(View.VISIBLE);
                           else
                               mTil3.setVisibility(View.GONE);
                       }
                   });
           }
           }
           else
               central = new Central();
       }
       else central = new Central();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        if (edit){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_configuracion, menu);}
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.eliminar:
                dialogDelete(central).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case PICK_CONTACT :
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c =  getContentResolver().query(contactData, null, null, null, null);
                    if (c!=null && c.moveToFirst()) {
                        String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        mEtName.setText(name);

                        String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        if ( hasPhone.equalsIgnoreCase("1"))
                            hasPhone = "true";
                        else
                            hasPhone = "false" ;

                        if (Boolean.parseBoolean(hasPhone))
                        {
                            String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId,null, null);
                            while (phones.moveToNext())
                            {
                               String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                               mEtNumber.setText(phoneNumber);
                            }
                            phones.close();
                        }

                    }
                }
                break;
        }
    }

    private boolean validate(String number, String name)
    {
        if (number.isEmpty())
        {
            mEtNumber.setError("Introduzca el número telefónico de la Llave GSM");
            mEtNumber.requestFocus();
            return false;
        }

        if (number.length()<5)
        {
            mEtNumber.setError("El número debe ser mayor de 5 dígitos");
            mEtNumber.requestFocus();
            return false;
        }

        if (name.isEmpty()){
            mEtName.setError("El nombre no puede estar vacío");
            mEtName.requestFocus();
            return false;
        }

            return true;
    }

    private void saveNumber(final String number, final String name) {

        if (edit)
        {
            realm.beginTransaction();
            central.setNombre(name);
            central.setNamebt1(mEtBt1.getText().toString());
            central.setNamebt2(mEtBt2.getText().toString());
            central.setBtSec(mSwBt2.isChecked());
            realm.commitTransaction();
            finish();
        }
        else {
            final Central copy = central;
            copy.setNombre(name);
            copy.setNumero(number);


            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealmOrUpdate(copy);
                    if (getIntent().getExtras()==null)
                    startActivity(new Intent(AddActivity.this, MainActivity.class));
                    finish();
                }
            });
        }
    }

    private Dialog dialogDelete(final Central central){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¿Desea Eliminar el control de la Llave GSM: "+ central.getNombre()+ " ?");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                realm.beginTransaction();
                central.deleteFromRealm();
                realm.commitTransaction();
                finish();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setMessage("Toda la información y configuración del número "+ central.getNumero()+" sera eliminada y no podrá ser recuperada.");
        return builder.create();
    }


}
