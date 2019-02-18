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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import ve.first.phl.com.phlcontrol.Adapter.CentralHorizontalAdapter;
import ve.first.phl.com.phlcontrol.Adapter.CentralVerticalAdapter;
import ve.first.phl.com.phlcontrol.AddActivity;
import ve.first.phl.com.phlcontrol.MainActivity;
import ve.first.phl.com.phlcontrol.Model.Central;
import ve.first.phl.com.phlcontrol.R;
import ve.first.phl.com.phlcontrol.SplashActivity;
import ve.first.phl.com.phlcontrol.Utils.StorageUtils;
import ve.first.phl.com.phlcontrol.Utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class ControlListFragment extends Fragment implements CentralVerticalAdapter.OnClick {
    private Realm realm;
    private CentralVerticalAdapter mAdapter;
    private RealmResults<Central> mCentrals;
    private MainActivity main;
    @BindView(R.id.ll_header)
    RecyclerView mRvCentrals;

    @OnClick(R.id.tv_footer) void footerClick(){
        Uri uriUrl = Uri.parse("https://www.phl.com.ve");
        Intent intent2 = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(intent2);
    }

    public ControlListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_control, container, false);
        Realm.init(getActivity());
        realm = Realm.getDefaultInstance();
        ButterKnife.bind(this,v);
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
        mAdapter = new CentralVerticalAdapter(mCentrals,getContext(),this);
        mRvCentrals.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        mRvCentrals.setAdapter(mAdapter);
    }

    @Override
    public void onMainClick(int position) {
        StorageUtils.saveCentralPage(getActivity(),position);
        main.onBackPressed();
    }

    @Override
    public void onEditClick(String central) {
        Intent intent = new Intent(getActivity(), AddActivity.class);
        intent.putExtra("central",central);
        startActivity(intent);
    }

    @Override
    public void onCall(int position) {
        main.llamadaPrincipal(getCentral(position).getNumero());
    }

    @Override
    public void onOpen(int position) {
        Central central = getCentral(position);

        if (central.getTimebt1()!=null && !central.getTimebt1().isEmpty())
        {
            if (Utils.isUpToDate(getActivity(),central.getTimebt1(),5))
                abrirPrincipal(central);
            else
                Toast.makeText(getActivity(), "Espere 5 segundos para volver activar.", Toast.LENGTH_SHORT).show();
        }
        else
            abrirPrincipal(central);
    }

    @Override
    public void onOpen2(int position) {
        Central central = getCentral(position);

        if (central.getTimebt2()!=null && !central.getTimebt2().isEmpty())
        {
            if (Utils.isUpToDate(getActivity(),central.getTimebt2(),5))
                abrirSecundario(central);
            else
                Toast.makeText(getActivity(), "Espere 5 segundos para volver activar.", Toast.LENGTH_SHORT).show();
        }
        else
            abrirSecundario(central);
    }

    @Override
    public void onAlarm(int position) {
        Central central = getCentral(position);
        if (central.isBtalarm())
            main.activateAlarm(central.getNumero());
        else
            dialogAlarm(central).show();

    }

    @Override
    public void onEmergency(int position) {
        Central central = getCentral(position);
        if (central.isBtSos())
            main.activateEmergency(central.getNumero());
        else
            dialogEmergency(central).show();
    }

    private void abrirPrincipal(final Central central){
        main.abrirPrincipal(central.getNumero());
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                central.setTimebt1(Utils.getDateString());
                realm.copyToRealmOrUpdate(central);

            }
        });
        Toast.makeText(main, "Activando 1: "+central.getNombre()+". Por favor espere 5 segundos..", Toast.LENGTH_SHORT).show();
    }

    private void abrirSecundario(final Central central){
        main.abrirSecundario(central.getNumero());
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                central.setTimebt2(Utils.getDateString());
                realm.copyToRealmOrUpdate(central);

            }
        });
        Toast.makeText(main, "Activando 2: "+central.getNombre()+". Por favor espere 5 segundos..", Toast.LENGTH_SHORT).show();
    }

    private Central getCentral(int position){
        StorageUtils.saveCentralPage(getActivity(),position);
        return mCentrals.get(position);
    }

    private Dialog dialogAlarm(final Central central){

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("¿Desea activar la Alarma de la Llave GSM: "+ central.getNombre()+"?");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                realm.beginTransaction();
                central.setBtalarm(true);
                realm.commitTransaction();
                main.activateAlarm(central.getNumero());
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setMessage("El modo Alarma debe estar previamente configurado en la Llave GSM para activar la alarma correctamente!");
        return builder.create();
    }

    private Dialog dialogEmergency(final Central central){

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("¿Desea activar la Apertura con Alarma de la Llave GSM: "+ central.getNombre()+"?");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                realm.beginTransaction();
                central.setBtSos(true);
                realm.commitTransaction();
                main.activateEmergency(central.getNumero());
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setMessage("El modo Alarma debe estar previamente configurado en la Llave GSM para activar la alarma correctamente!");
        return builder.create();
    }


}
