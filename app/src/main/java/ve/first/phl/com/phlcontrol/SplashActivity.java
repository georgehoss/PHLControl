package ve.first.phl.com.phlcontrol;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmResults;
import ve.first.phl.com.phlcontrol.Model.Central;


public class SplashActivity extends AppCompatActivity {
    public static final int MULTIPLE_PERMISSIONS = 10; // code you want.
    private Realm realm;
    String[] permissions= new String[]{
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.CALL_PHONE};
            //Manifest.permission.SEND_SMS};

    @BindView(R.id.bt_accept) Button mBtAccept;
    @BindView(R.id.bt_info) Button mBtInfo;
    @BindView(R.id.bt_permisions) Button mBtContinue;
    @BindView(R.id.tv_deber) TextView mTvDeber;
    @BindView(R.id.tv_info) TextView mTvInfo;


    @OnClick (R.id.bt_accept) void onAccept(){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            mBtAccept.setVisibility(View.GONE);
            mBtInfo.setVisibility(View.GONE);
            if (!checkPermission()) {
                mBtContinue.setVisibility(View.VISIBLE);
                mTvDeber.setVisibility(View.GONE);
                mTvInfo.setText(getString(R.string.tap_to_continue));
            }
            else
            {
                launchAddActivity();
            }

        }
        else
        {
            launchAddActivity();
        }
    }

    @OnClick (R.id.bt_info) void onInfo(){

            Uri uriUrl = Uri.parse("https://www.phl.com.ve");
            Intent intent2 = new Intent(Intent.ACTION_VIEW, uriUrl);
            startActivity(intent2);

    }

    @OnClick (R.id.bt_permisions) void onPermissions(){
        if (checkPermissions())
            startActivity(new Intent(this,MainActivity.class));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash_);
        ButterKnife.bind(this);
        Realm.init(this);
        realm = Realm.getDefaultInstance();
        RealmResults<Central> centrals = realm.where(Central.class).findAll();
        if (centrals.size()>0)
            launchMainActivity();

    }


    @Override
    protected void onPause() {
        super.onPause();

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

    private  boolean checkPermission() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p:permissions) {
            result = ContextCompat.checkSelfPermission(this,p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        return listPermissionsNeeded.isEmpty();
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
                    if (garanted) {
                        launchAddActivity();
                    }
                    else
                        Toast.makeText(this, "Con los permisos denegados no es posible utilizar PHL Control, presiona continuar para aceptar los permisos!", Toast.LENGTH_LONG).show();

                }

            }
        }
    }

    private void launchMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void launchAddActivity() {
        startActivity(new Intent(this, AddActivity.class));
        finish();
    }
}
