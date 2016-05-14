package epiphany_soft.wtw.Activities.Series;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import epiphany_soft.wtw.ActivityBase;
import epiphany_soft.wtw.DataBase.DataBaseConnection;
import epiphany_soft.wtw.DataBase.DataBaseContract;
import epiphany_soft.wtw.Fonts.RobotoFont;
import epiphany_soft.wtw.Fonts.SpecialFont;
import epiphany_soft.wtw.R;

/**
 * Created by Camilo on 2/04/2016.
 */
public class ActivityActualizarCapitulo extends ActivityBase {

    private EditText nombre,numero;
    private int idSerie, idTemporada,idCapitulo;
    private String nombreSerie,nombreCapitulo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_capitulo);

        nombre = (EditText) findViewById(R.id.txtNombreCapitulo);
        numero = (EditText) findViewById(R.id.txtNumeroCapitulo);

        Bundle b = getIntent().getExtras();
        idSerie = b.getInt(DataBaseContract.CapituloContract.COLUMN_NAME_SERIE_ID);
        idTemporada = b.getInt(DataBaseContract.CapituloContract.COLUMN_NAME_TEMPORADA_ID);
        nombreSerie = b.getString(DataBaseContract.ProgramaContract.COLUMN_NAME_PROGRAMA_NOMBRE);
        idCapitulo = b.getInt(DataBaseContract.CapituloContract.COLUMN_NAME_CAPITULO_ID);
        nombreCapitulo = b.getString(DataBaseContract.CapituloContract.COLUMN_NAME_CAPITULO_NOMBRE);

        numero.setText(Integer.toString(idCapitulo));
        nombre.setText(nombreCapitulo);

        setTitlePersonalizado();
        setSpecialFonts();
    }

    private void setTitlePersonalizado(){
        String numTemp = Integer.toString(idTemporada);
        String numCap = Integer.toString(idCapitulo);
        if (numTemp.length()==1) numTemp = "0"+numTemp;
        if (numCap.length()==1) numCap = "0"+numCap;
        setTitle(nombreSerie + "-T" + numTemp + ": E" + numCap);
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

    public void onClickActualizarCapitulo(View v) {
        if (nombre.getText().toString().trim().equals("")) {
            nombre.setError("Introduzca un nombre");
            return;
        }
        if (numero.getText().toString().trim().equals("")){
            numero.setError("Introduzca un numero");
            return;
        }
        String nombreCap=nombre.getText().toString().trim();
        int numeroCap=Integer.parseInt(numero.getText().toString().trim());
        DataBaseConnection db=new DataBaseConnection(this.getBaseContext());
        boolean success=db.actualizarCapitulo(idCapitulo, numeroCap, nombreCap, idTemporada, idSerie);
        if (success) {
            createToast("Capítulo actualizado");
            ActivityDetalleCapitulo.idCapitulo=numeroCap;
            ActivityDetalleCapitulo.actualizado=true;
            ActivityDetalleTemporada.actualizado=true;
            this.finish();
        }
        else createToast("El número de capítulo ya existe");
    }
}
