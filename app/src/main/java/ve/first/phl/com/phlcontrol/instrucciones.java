package ve.first.phl.com.phlcontrol;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class instrucciones extends AppCompatActivity implements View.OnClickListener {
    ImageButton Web,Contacto,Tw,Insta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instrucciones);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Web = (ImageButton) findViewById(R.id.IBweb);
        Contacto = (ImageButton) findViewById(R.id.IBcorreo);
        Tw = (ImageButton) findViewById(R.id.IBtwitter);
        Insta = (ImageButton) findViewById(R.id.IBinstagram);
        Web.setOnClickListener(this);
        Contacto.setOnClickListener(this);
        Tw.setOnClickListener(this);
        Insta.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        final Vibrator vibe = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        switch (v.getId())
        {
            case R.id.IBcorreo:


                vibe.vibrate(200);
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"tecnologiaphl.ve@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "Contacto desde PHL Control");
                i.putExtra(Intent.EXTRA_TEXT   , "Nuestros numeros de tlf: +584245931384,+584265141276,+584145080831,+582515111322");
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(this, "No posee aplicacion para realizar la accion de enviar correo: tecnologiaphl.ve@gmail.com", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.IBtwitter:
                vibe.vibrate(200);
                Uri uri1 = Uri.parse("http://twitter.com/tecnologiaphl");
                Intent twapp = new Intent(Intent.ACTION_VIEW, uri1);

                twapp.setPackage("com.twitter.android");

                try {
                    startActivity(twapp);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://twitter.com/tecnologiaphl")));
                }
                break;
            case R.id.IBinstagram:
                vibe.vibrate(200);
                Uri uri = Uri.parse("http://instagram.com/tecnologiaphl");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.instagram.android");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/tecnologiaphl")));
                }
                break;
            case R.id.IBweb:
                vibe.vibrate(200);
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://phl.com.ve")));
                break;
        }

    }
}
