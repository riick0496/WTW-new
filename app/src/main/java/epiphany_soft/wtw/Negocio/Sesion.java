package epiphany_soft.wtw.Negocio;

/**
 * Created by Camilo on 8/04/2016.
 */
public class Sesion {
    private static Sesion ourInstance = new Sesion();
    private String nombreUsuario;
    private int idUsuario;
    private boolean isActiva;//Indica si la sesión está activa

    public static Sesion getInstance() {
        return ourInstance;
    }

    private Sesion() {
        nombreUsuario=null;
        idUsuario=-1;
        isActiva=false;
    }

    public void setNombreUsuario(String nombre){
        ourInstance.nombreUsuario=nombre;
    }
    public void setIdUsuario(int id){
        ourInstance.idUsuario=id;
    }
    public void setActiva(boolean activa){
        ourInstance.isActiva=activa;
    }
    public boolean isActiva(){
        return isActiva;
    }
    //Regresa a la instancia a sus valores por defecto
    public void refresh(){
        ourInstance = new Sesion();
    }
    public String getNombreUsuario(){
        return nombreUsuario;
    }
    public int getIdUsuario(){
        return idUsuario;
    }
}
