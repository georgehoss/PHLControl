package ve.first.phl.com.phlcontrol.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;
import ve.first.phl.com.phlcontrol.Model.Central;
import ve.first.phl.com.phlcontrol.R;

/**
 * Created by ghoss on 31/07/2018.
 */
public class CentralHorizontalAdapter extends RecyclerView.Adapter<CentralHorizontalAdapter.CentralViewHolder> {

    private RealmResults<Central> centrals;
    private Context context;
    private OnClick onClick;

    public CentralHorizontalAdapter(RealmResults<Central> centrals, Context context, OnClick onClick) {
        this.centrals = centrals;
        this.context = context;
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public CentralViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.central_row, parent, false);
        return new CentralViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CentralViewHolder holder, int position) {
        final Central central = centrals.get(position);
        holder.mTvNumber.setText(central.getNumero());
        holder.mTvTitle.setText(central.getNombre());
        holder.mBtAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onAddClick();
            }
        });
        final String numero = central.getNumero();
        holder.mBtEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onEditClick(numero);
            }
        });
        holder.mBtList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onListClick();
            }
        });
        holder.mBtCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onCall(central);
            }
        });
        holder.mBtPrincipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onOpen(central);
            }
        });
        holder.mBtSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onOpen2(central);
            }
        });
        holder.mIvLogo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onClick.onAlarm(central);
                return false;
            }
        });
        holder.mBtPrincipal.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onClick.onEmergency(central);
                return false;
            }
        });

        if (central.isBtSec())
            holder.mBtSecond.setVisibility(View.GONE);

        if (central.getNamebt1()!=null && !central.getNamebt1().isEmpty())
            holder.mBtPrincipal.setText(central.getNamebt1());

        if (central.getNamebt2()!=null && !central.getNamebt2().isEmpty())
            holder.mBtSecond.setText(central.getNamebt2());
    }

    @Override
    public int getItemCount() {
       if (centrals!=null && centrals.size()>0)
           return centrals.size();
       else
           return 0;
    }



    class CentralViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.iv_logo) ImageView mIvLogo;
        @BindView(R.id.tv_title) TextView mTvTitle;
        @BindView(R.id.tv_number) TextView mTvNumber;
        @BindView(R.id.bt_list) Button mBtList;
        @BindView(R.id.bt_edit) Button mBtEdit;
        @BindView(R.id.bt_add) Button mBtAdd;
        @BindView(R.id.bt_call) Button mBtCall;
        @BindView(R.id.bt_principal) Button mBtPrincipal;
        @BindView(R.id.bt_second) Button mBtSecond;

        CentralViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface OnClick{
        void onListClick();
        void onAddClick();
        void onEditClick(String central);
        void onCall(Central central);
        void onOpen(Central central);
        void onOpen2(Central central);
        void onAlarm(Central central);
        void onEmergency(Central central);
    }
}