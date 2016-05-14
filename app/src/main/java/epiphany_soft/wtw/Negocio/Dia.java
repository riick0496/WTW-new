package epiphany_soft.wtw.Negocio;

/**
 * Created by Usuario on 01/05/2016.
 */
public class Dia {

    private Integer id;
    private boolean isChecked;
    private String nombre;

    public Dia() {
    }



    public Dia(Integer id, boolean isChecked, String nombre) {
        this.id = id;
        this.isChecked = isChecked;
        this.nombre = nombre;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Dia(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString(){
        return nombre;
    }
}
