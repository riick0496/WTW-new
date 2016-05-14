package epiphany_soft.wtw.Negocio;

/**
 * Created by Camilo on 20/04/2016.
 */
public class Programa {

    private int idPrograma;
    private String nombre;
    private String sinopsis;
    private int anioEstreno;
    private String pais;
    private boolean favorito;

    public Programa(){
        idPrograma=-1;
        nombre=null;
        sinopsis=null;
        anioEstreno=-1;
        pais=null;
        favorito=false;
    }

    public int getIdPrograma() {
        return idPrograma;
    }

    public void setIdPrograma(int idPrograma) {
        this.idPrograma = idPrograma;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public int getAnioEstreno() {
        return anioEstreno;
    }

    public void setAnioEstreno(int anioEstreno) {
        this.anioEstreno = anioEstreno;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public boolean isFavorito() {
        return favorito;
    }

    public void setFavorito(boolean favorito) {
        this.favorito = favorito;
    }
}
