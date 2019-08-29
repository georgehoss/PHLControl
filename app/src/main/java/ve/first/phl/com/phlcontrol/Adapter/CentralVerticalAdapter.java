package ve.first.phl.com.phlcontrol.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;
import ve.first.phl.com.phlcontrol.Model.Central;
import ve.first.phl.com.phlcontrol.R;

/**
 * Created by ghoss on 08/08/2018.
 */
public class CentralVerticalAdapter extends RecyclerView.Adapter<CentralVerticalAdapter.CentralViewHolder> {
    private RealmResults<Central> centrals;
    private Context context;
    private OnClick onClick;

    public CentralVerticalAdapter(RealmResults<Central> centrals, Context context, OnClick onClick) {
        this.centrals = centrals;
        this.context = context;
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public CentralViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.central_list_row, parent, false);
        return new CentralViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CentralViewHolder holder, final int position) {
        final Central central = centrals.get(position);
        holder.mTvNumber.setText(central.getNumero());
        holder.mTvTitle.setText(central.getNombre());

        final String numero = central.getNumero();
        final int pos = position;


        holder.mCvMain.setOnClickListener(v -> onClick.onMainClick(pos));

        holder.mBtEdit.setOnClickListener(v -> onClick.onEditClick(numero));

        holder.mBtCall.setOnClickListener(v -> onClick.onCall(pos));
        holder.mBtPrincipal.setOnClickListener(v -> onClick.onOpen(pos));
        holder.mBtSecond.setOnClickListener(v -> onClick.onOpen2(pos));
        holder.mCvMain.setOnLongClickListener(v -> {
            onClick.onAlarm(pos);
            return false;
        });
        holder.mBtPrincipal.setOnLongClickListener(v -> {
            onClick.onEmergency(pos);
            return false;
        });

        if (central.isBtSec())
            holder.mBtSecond.setEnabled(false);

    }

    @Override
    public int getItemCount() {
        if (centrals!=null && centrals.size()>0)
            return centrals.size();
        else
            return 0;
    }

    class CentralViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.tv_title) TextView mTvTitle;
        @BindView(R.id.tv_number) TextView mTvNumber;
        @BindView(R.id.bt_edit) ImageButton mBtEdit;
        @BindView(R.id.bt_call) ImageButton mBtCall;
        @BindView(R.id.bt_1) ImageButton mBtPrincipal;
        @BindView(R.id.bt_2) ImageButton mBtSecond;
        @BindView(R.id.cv_main) CardView mCvMain;

        CentralViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface OnClick{
        void onMainClick(int position);
        void onEditClick(String central);
        void onCall(int position);
        void onOpen(int position);
        void onOpen2(int position);
        void onAlarm(int position);
        void onEmergency(int position);
    }
}
