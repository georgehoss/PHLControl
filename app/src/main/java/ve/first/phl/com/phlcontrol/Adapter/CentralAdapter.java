package ve.first.phl.com.phlcontrol.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ve.first.phl.com.phlcontrol.Model.Central;
import ve.first.phl.com.phlcontrol.R;

/**
 * Created by George on 29/04/2017.
 */

public class CentralAdapter extends RecyclerView.Adapter<CentralHolder>{

    private ArrayList <Central> mCentralList;
    public Context mContext;
    public OnNotifyChange mOnChange;
    private int mposition;

    public CentralAdapter (ArrayList<Central> list,Context ctx){
        this.mCentralList = list;
        this.mContext = ctx;
        this.mOnChange = (OnNotifyChange) ctx;

    }

    @Override
    public CentralHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final Context context = parent.getContext();
        final View view = LayoutInflater.from(context).inflate(R.layout.centrales_row,parent,false);
        return CentralHolder.newInstance(view);
    }

    @Override
    public void onBindViewHolder(final CentralHolder holder, final int position) {
        Central central = mCentralList.get(position);
        mposition =position;
        holder.setInfo(central.getNombre());
        holder.setNumero(central.getNumero());
        holder.getInfo().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               mOnChange.onItemClick(position);

            }
        });
        holder.clickDelete().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnChange.onItemRemoved(position);
            }
        });
        holder.clickEdit().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnChange.onItemEdited(position);
            }
        });
        Log.d("PHLdegbug adapter",central.getNombre()+": "+central.getNumero());

    }

    @Override
    public int getItemCount() {
        return mCentralList.size();
    }

    public ArrayList<Central> getmCentralList (){
        return mCentralList;
    }

    public void setmCentralList (ArrayList<Central> list){
        this.mCentralList=list;
    }


    public interface OnNotifyChange{
            void noItems();
            void onItemEdited(int position);
            void onItemRemoved(int position);
            void onItemClick (int position);
    }
}
