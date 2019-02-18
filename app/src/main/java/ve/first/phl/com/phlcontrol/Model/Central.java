package ve.first.phl.com.phlcontrol.Model;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Central extends RealmObject {
    @PrimaryKey
    private String numero;
    private String nombre;
    private String timebt1;
    private String timebt2;
    private String namebt1;
    private String namebt2;
    private int iconbt1;
    private int iconbt2;
    private boolean btSos;
    private boolean btalarm;
    private boolean btSec;

    public Central() {
    }

    public Central(String nombre, String numero){
        this.nombre=nombre;
        this.numero=numero;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNamebt1() {
        return namebt1;
    }

    public void setNamebt1(String namebt1) {
        this.namebt1 = namebt1;
    }

    public String getNamebt2() {
        return namebt2;
    }

    public void setNamebt2(String namebt2) {
        this.namebt2 = namebt2;
    }


    public boolean isBtSos() {
        return btSos;
    }

    public void setBtSos(boolean btSos) {
        this.btSos = btSos;
    }

    public boolean isBtalarm() {
        return btalarm;
    }

    public void setBtalarm(boolean btalarm) {
        this.btalarm = btalarm;
    }

    public String getTimebt1() {
        return timebt1;
    }

    public void setTimebt1(String timebt1) {
        this.timebt1 = timebt1;
    }

    public String getTimebt2() {
        return timebt2;
    }

    public void setTimebt2(String timebt2) {
        this.timebt2 = timebt2;
    }

    public int getIconbt1() {
        return iconbt1;
    }

    public void setIconbt1(int iconbt1) {
        this.iconbt1 = iconbt1;
    }

    public int getIconbt2() {
        return iconbt2;
    }

    public void setIconbt2(int iconbt2) {
        this.iconbt2 = iconbt2;
    }

    public boolean isBtSec() {
        return btSec;
    }

    public void setBtSec(boolean btSec) {
        this.btSec = btSec;
    }
}
