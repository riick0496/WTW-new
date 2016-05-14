package epiphany_soft.wtw.Negocio;

/**
 * Created by Camilo on 11/04/2016.
 */
public class Emisora {
    private int id;
    private String nombre;

    public Emisora(int id,String nombre){
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
