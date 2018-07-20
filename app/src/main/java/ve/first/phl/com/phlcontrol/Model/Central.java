package ve.first.phl.com.phlcontrol.Model;

/**
 * Created by Javier on 28/04/2017.
 */

public class Central {
    private String nombre;
    private String numero;

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
}
