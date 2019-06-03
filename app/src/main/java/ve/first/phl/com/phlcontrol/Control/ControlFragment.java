package ve.first.phl.com.phlcontrol.Control;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import ve.first.phl.com.phlcontrol.Adapter.CentralHorizontalAdapter;
import ve.first.phl.com.phlcontrol.AddActivity;
import ve.first.phl.com.phlcontrol.MainActivity;
import ve.first.phl.com.phlcontrol.Model.Central;
import ve.first.phl.com.phlcontrol.R;
import ve.first.phl.com.phlcontrol.SplashActivity;
import ve.first.phl.com.phlcontrol.Utils.StorageUtils;
import ve.first.phl.com.phlcontrol.Utils.Utils;


public class ControlFragment extends Fragment implements CentralHorizontalAdapter.OnClick {

    private Realm realm;
    private CentralHorizontalAdapter mAdapter;
    private RealmResults<Central> mCentrals;
    private MainActivity main;


    public ControlFragment() {
        // Required empty public constructor
    }



    @BindView(R.id.ll_header) RecyclerView mRvCentrals;

    @OnClick(R.id.tv_footer) void footerClick(){
        Uri uriUrl = Uri.parse("https://www.phl.com.ve");
        Intent intent2 = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(intent2);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_control, container, false);

        Realm.init(getActivity());
        realm = Realm.getDefaultInstance();
        ButterKnife.bind(this,v);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mRvCentrals);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        main = (MainActivity) getActivity();
        mCentrals = realm.where(Central.class).findAll();
        if (mCentrals.size()==0 && getActivity()!=null) {
            startActivity(new Intent(getActivity(), SplashActivity.class));
            getActivity().finish();
        }


        mAdapter = new CentralHorizontalAdapter(mCentrals,getActivity(),this);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mRvCentrals.setLayoutManager(horizontalLayoutManager);
        mRvCentrals.setAdapter(mAdapter);
        if (StorageUtils.getCentralPage(getContext())!= 0 && StorageUtils.getCentralPage(getActivity())< mCentrals.size())
        mRvCentrals.smoothScrollToPosition(StorageUtils.getCentralPage(getContext()));

    }

    @Override
    public void onResume() {
        super.onResume();
        if (StorageUtils.getCentralPage(getContext())!= 0 && StorageUtils.getCentralPage(getActivity())< mCentrals.size())
            mRvCentrals.smoothScrollToPosition(StorageUtils.getCentralPage(getContext()));
    }

    @Override
    public void onListClick() {
        main.getSupportFragmentManager().beginTransaction().replace(R.id.container, new ControlListFragment()).addToBackStack(null).commit();
    }

    @Override
    public void onAddClick() {
        Intent intent = new Intent(getActivity(), AddActivity.class);
        intent.putExtra("new","new");
        startActivity(intent);
    }

    @Override
    public void onEditClick(String central) {
        Intent intent = new Intent(getActivity(), AddActivity.class);
        intent.putExtra("central",central);
        startActivity(intent);
    }

    @Override
    public void onCall(Central central) {
        main.llamadaPrincipal(getCentral().getNumero());
    }

    @Override
    public void onOpen(Central central) {

        abrirPrincipal();
        /*if (central.getTimebt1()!=null && !central.getTimebt1().isEmpty())
        {

            if (Utils.isUpToDate(getActivity(),central.getTimebt1(),5))
                abrirPrincipal();
            else
                Toast.makeText(getActivity(), "Espere 5 segundos para volver activar.", Toast.LENGTH_SHORT).show();
        }
        else
            abrirPrincipal();
                            */

    }

    @Override
    public void onOpen2(Central central) {
        abrirSecundario();
            /*
        if (central.getTimebt2()!=null && !central.getTimebt2().isEmpty())
        {

            if (Utils.isUpToDate(getActivity(),central.getTimebt2(),5))
                abrirSecundario();
            else
                Toast.makeText(getActivity(), "Espere 5 segundos para volver activar.", Toast.LENGTH_SHORT).show();
              }
        else
            abrirSecundario();
              */


    }

    @Override
    public void onAlarm(Central central) {
        if (central.isBtalarm())
            main.activateAlarm(central.getNumero());
        else
            dialogAlarm(central).show();

    }

    @Override
    public void onEmergency(Central central) {
        if (central.isBtSos())
            main.activateEmergency(central.getNumero());
        else
            dialogEmergency(central).show();

    }

    private void abrirPrincipal(){
        main.abrirPrincipal(getCentral().getNumero());
        realm.executeTransaction(realm -> {
            Central central = getCentral();
            central.setTimebt1(Utils.getDateString());
            realm.copyToRealmOrUpdate(central);
           // mCentrals = realm.where(Central.class).findAll();
        });
        //Toast.makeText(main, "Activando 1: "+getCentral().getNombre()+". Por favor espere 5 segundos..", Toast.LENGTH_SHORT).show();
    }

    private void abrirSecundario(){
        main.abrirSecundario(getCentral().getNumero());
        realm.executeTransaction(realm -> {
            Central central = getCentral();
            central.setTimebt2(Utils.getDateString());
            realm.copyToRealmOrUpdate(central);
           // mCentrals = realm.where(Central.class).findAll();
        });
       // Toast.makeText(main, "Activando 2: "+getCentral().getNombre()+". Por favor espere 5 segundos..", Toast.LENGTH_SHORT).show();
    }

    private Central getCentral(){
        LinearLayoutManager myLayoutManager = (LinearLayoutManager) mRvCentrals.getLayoutManager();
        int scrollPosition = myLayoutManager.findFirstVisibleItemPosition();
        StorageUtils.saveCentralPage(getActivity(),scrollPosition);
        return mCentrals.get(scrollPosition);
    }

    private Dialog dialogAlarm(final Central central){

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("¿Desea activar la Alarma de la Llave GSM: "+ central.getNombre()+ "?");
        builder.setPositiveButton("Aceptar", (dialog, which) -> {
            realm.beginTransaction();
            central.setBtalarm(true);
            realm.commitTransaction();
            main.activateAlarm(central.getNumero());
        });
        builder.setNegativeButton("Cancelar", (dialog, which) -> {

        });
        builder.setMessage("El modo Alarma debe estar previamente configurado en la Llave GSM para activar la alarma correctamente!");
        return builder.create();
    }

    private Dialog dialogEmergency(final Central central){

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("¿Desea activar la Apertura con Alarma de la Llave GSM: "+ central.getNombre()+"?");
        builder.setPositiveButton("Aceptar", (dialog, which) -> {
            realm.beginTransaction();
            central.setBtSos(true);
            realm.commitTransaction();
            main.activateEmergency(central.getNumero());
        });
        builder.setNegativeButton("Cancelar", (dialog, which) -> {

        });
        builder.setMessage("El modo Alarma debe estar previamente configurado en la Llave GSM para activar la alarma correctamente!");
        return builder.create();
    }

}
