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

public class ActivityAgregarTemporada extends ActivityBase {

    private EditText numeroDeTemporada;
    private int idSerie;
    String nombreSerie;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_temporada);

       numeroDeTemporada = (EditText) findViewById(R.id.txtNumeroTemporada);

         Bundle b = getIntent().getExtras();
        idSerie = b.getInt(DataBaseContract.TemporadaContract.COLUMN_NAME_PROGRAMA_ID);
        nombreSerie = b.getString(DataBaseContract.ProgramaContract.COLUMN_NAME_PROGRAMA_NOMBRE);
        setTitle(nombreSerie);

        setSpecialFonts();

    }

    private void setSpecialFonts(){
        TextView numeroLabel=(TextView) findViewById(R.id.lblNumeroTemporada);
        numeroLabel.setTypeface(SpecialFont.getInstance(this).getTypeFace());
        numeroDeTemporada.setTypeface(RobotoFont.getInstance(this).getTypeFace());
    }

    public void onClickAgregarTemporada(View v) {

        if (numeroDeTemporada.getText().toString().trim().equals("")) {
            numeroDeTemporada.setError("Introduzca el numero de la temporada");
            return;
        }

        int numeroTemporada=Integer.parseInt(numeroDeTemporada.getText().toString());
        DataBaseConnection db=new DataBaseConnection(this.getBaseContext());
        boolean success=db.insertarTemporada(idSerie, numeroTemporada);

        if (success) {
            createToast("Temporada creada con exito");
            ActivityDetalleSerie.actualizado=true;
            this.finish();
        }
        else createToast("la Temporada ya se encuentra registrada");
    }
}
