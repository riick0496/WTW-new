package epiphany_soft.wtw.Negocio;

/**
 * Created by Camilo on 27/03/2016.
 */
public class Genero {
    private int id;
    private String nombre;

    public Genero(int id,String nombre){
        this.id=id;
        this.nombre=nombre;
    }

    public int getId(){
        return id;
    }

    @Override
    public String toString(){
        return nombre;
    }
}
