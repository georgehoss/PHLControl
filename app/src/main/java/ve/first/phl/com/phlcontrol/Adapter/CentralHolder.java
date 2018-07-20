package ve.first.phl.com.phlcontrol.Adapter;

import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import ve.first.phl.com.phlcontrol.Model.Central;
import ve.first.phl.com.phlcontrol.R;

/**
 * Created by George on 29/04/2017.
 */

public class CentralHolder extends RecyclerView.ViewHolder {

    private final TextView info;
    private final TextView numero;
    private final ImageButton editar;
    private final ImageButton delete;

    public CentralHolder(View itemView, TextView info,TextView numero, ImageButton edit, ImageButton delet) {
        super(itemView);
        this.info= info;
        this.numero=numero;
        this.editar = edit;
        this.delete = delet;
    }

    public static CentralHolder newInstance(View parent){
        TextView informacion = (TextView) parent.findViewById(R.id.tv_central);
        TextView numero = (TextView) parent.findViewById(R.id.tv_numero);
        ImageButton delete = (ImageButton) parent.findViewById(R.id.btn_delete);
        ImageButton edit = (ImageButton) parent.findViewById(R.id.btn_edit);


        return new CentralHolder(parent,informacion,numero,edit,delete);
    }

    public TextView getInfo(){
        return info;
    }

    public void setInfo(CharSequence text){
        info.setText(text);
    }

    public TextView getNumero(){
        return numero;
    }

    public void setNumero(CharSequence text){
        numero.setText(text);
    }

    public ImageButton clickDelete (){
        return delete;
    }

    public ImageButton clickEdit(){
        return editar;
    }


}


///sdf sdfsdf dsfsdfnmbmnbmnbnbnbmnmnmnmnbmbmnmnmmmmnbmnbmmm