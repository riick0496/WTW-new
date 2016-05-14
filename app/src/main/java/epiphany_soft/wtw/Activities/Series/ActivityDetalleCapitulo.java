package epiphany_soft.wtw.Activities.Series;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.TextView;

import epiphany_soft.wtw.ActivityBase;
import epiphany_soft.wtw.DataBase.DataBaseConnection;
import epiphany_soft.wtw.Fonts.RobotoFont;
import epiphany_soft.wtw.Fonts.SpecialFont;
import epiphany_soft.wtw.Negocio.Sesion;
import epiphany_soft.wtw.R;

import static epiphany_soft.wtw.DataBase.DataBaseContract.CapituloContract;
import static epiphany_soft.wtw.DataBase.DataBaseContract.ProgramaContract;

/**
 * Created by Camilo on 2/04/2016.
 */
public class ActivityDetalleCapitulo extends ActivityBase {
    private TextView nombre,numero;
    private int idSerie, idTemporada;
    private String nombreSerie,nombreCapitulo;

    public static int idCapitulo;
    public static boolean actualizado=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_capitulo);

        nombre = (TextView) findViewById(R.id.txtNombreCapitulo);
        numero = (TextView) findViewById(R.id.txtNumeroCapitulo);

        Bundle b = getIntent().getExtras();
        idSerie = b.getInt(CapituloContract.COLUMN_NAME_SERIE_ID);
        idTemporada = b.getInt(CapituloContract.COLUMN_NAME_TEMPORADA_ID);
        nombreSerie = b.getString(ProgramaContract.COLUMN_NAME_PROGRAMA_NOMBRE);

        //Solo debe obtenerse el idCapitulo de ésta forma la primera vez que se crea
        if (savedInstanceState==null) idCapitulo = b.getInt(CapituloContract.COLUMN_NAME_CAPITULO_ID);
        nombreCapitulo = b.getString(CapituloContract.COLUMN_NAME_CAPITULO_NOMBRE);

        setTitle(nombreSerie + ": Temporada " + Integer.toString(idTemporada));

        setInfoCapitulo();
        setTitlePersonalizado();
        setSpecialFonts();
    }

    private void setTitlePersonalizado(){
        String numTemp = Integer.toString(idTemporada);
        String numCap = Integer.toString(idCapitulo);
        if (numTemp.length()==1) numTemp = "0"+numTemp;
        if (numCap.length()==1) numCap = "0"+numCap;
        setTitle(nombreSerie + "-T" + numTemp +": E"+numCap);
    }

    private void setInfoCapitulo(){
        DataBaseConnection db=new DataBaseConnection(this.getBaseContext());
        Cursor c=db.consultarInfoCapitulo(idTemporada, idSerie, idCapitulo);
        c.moveToNext();
        idCapitulo = c.getInt(c.getColumnIndex(CapituloContract.COLUMN_NAME_CAPITULO_ID));
        nombreCapitulo = c.getString(c.getColumnIndex(CapituloContract.COLUMN_NAME_CAPITULO_NOMBRE));

        numero.setText(Integer.toString(idCapitulo));
        nombre.setText(nombreCapitulo);
    }

    private void setSpecialFonts(){
        TextView nombreLabel=(TextView) findViewById(R.id.lblNombreCapitulo);
        nombreLabel.setTypeface(SpecialFont.getInstance(this).getTypeFace());
        TextView numeroLabel=(TextView) findViewById(R.id.lblNumeroCapitulo);
        numeroLabel.setTypeface(SpecialFont.getInstance(this).getTypeFace());
        //Los textos
        nombre.setTypeface(RobotoFont.getInstance(this).getTypeFace());
        numero.setTypeface(RobotoFont.getInstance(this).getTypeFace());
    }

    @Override
    public void onResume(){
        super.onResume();
        if (actualizado) this.recreate();
        actualizado=false;
    }

    public void onClickActualizarCapitulo(View v){
        Intent i = new Intent(this, ActivityActualizarCapitulo.class);
        Bundle b = new Bundle();
        b.putInt(CapituloContract.COLUMN_NAME_TEMPORADA_ID, idTemporada);
        b.putInt(CapituloContract.COLUMN_NAME_SERIE_ID,idSerie);
        b.putString(ProgramaContract.COLUMN_NAME_PROGRAMA_NOMBRE, nombreSerie);
        b.putInt(CapituloContract.COLUMN_NAME_CAPITULO_ID, idCapitulo);
        b.putString(CapituloContract.COLUMN_NAME_CAPITULO_NOMBRE, nombreCapitulo);
        i.putExtras(b);
        startActivity(i);
    }

    protected void hideWhenNoSession(){
        if (!Sesion.getInstance().isActiva()){
            FloatingActionButton b = (FloatingActionButton) findViewById(R.id.fab);
            hide(b);
        }
    }

    protected void showWhenSession(){
        if (Sesion.getInstance().isActiva()){
            FloatingActionButton b = (FloatingActionButton) findViewById(R.id.fab);
            show(b);
        }
    }
}
