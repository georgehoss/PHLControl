package ve.first.phl.com.phlcontrol.Notifications;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import ve.first.phl.com.phlcontrol.Actividad_Principal;
import ve.first.phl.com.phlcontrol.R;
import ve.first.phl.com.phlcontrol.Storage.StorageUtils;

/**
 * Created by George on 02/05/2017.
 */

public final class NotificationsUtils {

    private static final String PREF_SECUESTRO ="msjantiss";
    private static final String PREF_ALARMA = "msjalarmaa";
    static dialogOnClick onClickB;

    public static void AvisoPregunta (final Context ctx, Boolean mostrar, @NonNull final String pref)
    {

        final Dialog builder = new Dialog(ctx);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.setContentView(R.layout.activity_aviso);
        builder.setCancelable(false);
        final Button aceptar = (Button) builder.findViewById(R.id.btn_aceptar_aviso);
        final Button cancelar = (Button) builder.findViewById(R.id.btn_cancelar_aviso);
        final CheckBox nomostrar = (CheckBox) builder.findViewById(R.id.cb_no_mostrar);

        if (!mostrar)
            nomostrar.setVisibility(View.GONE);

        nomostrar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (pref.equals(PREF_ALARMA))
                StorageUtils.setMsjAlarma(ctx,b);
                else
                    if (pref.equals(PREF_SECUESTRO))
                        StorageUtils.setMsjSecuestro(ctx,b);
            }
        });

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pref.equals(PREF_ALARMA))
                    onClickB.onAceptar(true);

                builder.dismiss();
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.dismiss();
            }
        });

        builder.show();


    }

    public static interface dialogOnClick {
        public void onAceptar(Boolean btrue);
        public void onCancelar();

    }


}
