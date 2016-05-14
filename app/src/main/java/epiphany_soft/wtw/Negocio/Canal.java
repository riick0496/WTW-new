package epiphany_soft.wtw.Negocio;

/**
 * Created by Camilo on 8/04/2016.
 */
public class Canal {
    private static Canal ourInstance = new Canal();
    private String nombreCanal;

    public static Canal getInstance() {
        return ourInstance;
    }

    private Canal() {
        nombreCanal=null;
    }

    public void setNombreCanal(String nombre) {
        ourInstance.nombreCanal = nombre;
    }

    public String getNombreCanal() {
        return ourInstance.nombreCanal;
    }
}
