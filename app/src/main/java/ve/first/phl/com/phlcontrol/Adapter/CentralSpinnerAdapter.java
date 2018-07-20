package ve.first.phl.com.phlcontrol.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import ve.first.phl.com.phlcontrol.Model.Central;
import ve.first.phl.com.phlcontrol.R;
import ve.first.phl.com.phlcontrol.Storage.StorageUtils;

/**
 * Created by George on 29/04/2017.
 */

public class CentralSpinnerAdapter extends ArrayAdapter<Central> {
    Central [] mCentrals;

    public CentralSpinnerAdapter(Context ctx,int resource,Central[] centrals)
    {
        super(ctx,resource,centrals);
        this.mCentrals = centrals;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View v = convertView;
        if (v == null) v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.central_spinner_row,parent,false);
        TextView tvCentral = (TextView) v.findViewById(R.id.tv_sp_central);
        if (position>0)
        {
            Central central = mCentrals[position-1];
            //Central central = mCentrals[mPosition];
            tvCentral.setText(central.getNombre()+ " - "+central.getNumero());
        }
        else
            if (StorageUtils.getSpCentral(parent.getContext())<100)
            {
                Central central = mCentrals[StorageUtils.getSpCentral(parent.getContext())];
                tvCentral.setText(central.getNombre()+ " - "+central.getNumero());
            }
            else
                {
                    tvCentral.setText("- Elige una Central -");
                }
        return v;
    }
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.centrales_dropdown_row, parent, false);

        TextView tvCentral = (TextView) v.findViewById(R.id.tv_central_dropdown);
        if (position > 0) {
            Central c = mCentrals[position - 1];

            tvCentral.setText(c.getNombre()+ " - " + c.getNumero());
        }
        else
            tvCentral.setText("- Elige una Central -");

        return v;
    }

    @Override
    public boolean isEnabled(int position) {
        return position > 0 ? true : false;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public int getCount() {
        return mCentrals.length + 1;
    }

}




