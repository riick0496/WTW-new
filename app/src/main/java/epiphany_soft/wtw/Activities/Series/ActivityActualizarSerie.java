package epiphany_soft.wtw.Activities.Series;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import epiphany_soft.wtw.ActivityBase;
import epiphany_soft.wtw.DataBase.DataBaseConnection;
import epiphany_soft.wtw.DataBase.DataBaseContract;
import epiphany_soft.wtw.DataBase.DataBaseContract.ProgramaContract;
import epiphany_soft.wtw.Fonts.RobotoFont;
import epiphany_soft.wtw.Fonts.SpecialFont;
import epiphany_soft.wtw.Negocio.Genero;
import epiphany_soft.wtw.R;


public class ActivityActualizarSerie extends ActivityBase {
    private EditText name,sinopsis,anio,pais;
    private Spinner spnGenero;
    String nombreSerie,sinopsisText,generoText,paisText;
    int anioText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_serie);
        setTitle("ACTUALIZAR SERIE");

        name=(EditText)findViewById(R.id.txtNombreSerie);
        name.setKeyListener(null);
        sinopsis=(EditText)findViewById(R.id.txtSinopsis);
        anio =(EditText)findViewById(R.id.txtAnioEstreno);
        pais =(EditText)findViewById(R.id.txtPaisOrigen);

        Bundle b = getIntent().getExtras();
        nombreSerie = b.getString(ProgramaContract.COLUMN_NAME_PROGRAMA_NOMBRE);
        sinopsisText = b.getString(ProgramaContract.COLUMN_NAME_PROGRAMA_SINOPSIS);
        generoText = b.getString(DataBaseContract.GeneroContract.COLUMN_NAME_GENERO_NOMBRE);
        anioText = b.getInt(ProgramaContract.COLUMN_NAME_PROGRAMA_ANIO_ESTRENO);
        paisText = b.getString(ProgramaContract.COLUMN_NAME_PROGRAMA_PAIS_ORIGEN);

        sinopsis.setText(sinopsisText);
        name.setText(nombreSerie);
        anio.setText(Integer.toString(anioText));
        pais.setText(paisText);
        crearSpinnerGeneros();
        setSpecialFonts();
    }

    private void setSpecialFonts(){
        TextView nombre=(TextView) findViewById(R.id.lblNombrePrograma);
        nombre.setTypeface(SpecialFont.getInstance(this).getTypeFace());
        TextView sinopsisLabel=(TextView) findViewById(R.id.lblSinopsis);
        sinopsisLabel.setTypeface(SpecialFont.getInstance(this).getTypeFace());
        TextView genero=(TextView) findViewById(R.id.lblGenero);
        genero.setTypeface(SpecialFont.getInstance(this).getTypeFace());
        TextView lblAnio=(TextView) findViewById(R.id.lblAnioEstreno);
        lblAnio.setTypeface(SpecialFont.getInstance(this).getTypeFace());
        TextView lblPais=(TextView) findViewById(R.id.lblPaisOrigen);
        lblPais.setTypeface(SpecialFont.getInstance(this).getTypeFace());
        //Los textos
        name.setTypeface(RobotoFont.getInstance(this).getTypeFace());
        sinopsis.setTypeface(RobotoFont.getInstance(this).getTypeFace());
        anio.setTypeface(RobotoFont.getInstance(this).getTypeFace());
        pais.setTypeface(RobotoFont.getInstance(this).getTypeFace());
    }

    public void onClickActualizarSerie(View v){

        if (anio.getText().toString().trim().equals("")){
            anio.setError("Introduzca un año");
            return;
        }
        String sinopsisS = sinopsis.getText().toString().trim();
        String paisS=pais.getText().toString().trim();
        int anioS=Integer.parseInt(anio.getText().toString());
        int idGen=((Genero)spnGenero.getSelectedItem()).getId();
        DataBaseConnection db=new DataBaseConnection(this.getBaseContext());
        boolean success=db.actualizarPrograma(idGen, nombreSerie, sinopsisS, anioS, paisS);
        if (success) {
            createToast("Serie actualizada");
            ActivityDetalleSerie.actualizado=true;
            this.finish();
        }
        else createToast("Ocurrió un error");
    }

    public void crearSpinnerGeneros(){
        spnGenero = (Spinner) findViewById(R.id.spnGeneroAct);
        DataBaseConnection db=new DataBaseConnection(this.getBaseContext());
        Cursor c=db.consultarAllGeneros();
        final ArrayList<Genero> generos = new ArrayList<Genero>();
        while (c.moveToNext()){
            Genero g=
                    new Genero(c.getInt(c.getColumnIndex(DataBaseContract.GeneroContract.COLUMN_NAME_GENERO_ID)),
                            c.getString(c.getColumnIndex(DataBaseContract.GeneroContract.COLUMN_NAME_GENERO_NOMBRE)));
            if (c.getString(c.getColumnIndex(DataBaseContract.GeneroContract.COLUMN_NAME_GENERO_NOMBRE)).equals(generoText)){
                generos.add(0,g);
            } else generos.add(g);

        }
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.simple_spinner_item,generos){
            public View getView(int position,View convertView, ViewGroup parent){
                View v = super.getView(position, convertView, parent);

                if (position <= generos.size()) {
                    ((TextView) v).setTypeface(RobotoFont.getInstance(this.getContext()).getTypeFace());
                }

                return v;
            }
            public View getDropDownView(int position, View convertView, ViewGroup parent) {

                LayoutInflater inflater = getLayoutInflater();
                View row = inflater.inflate(R.layout.simple_spinner_item, parent,
                        false);
                TextView make = (TextView) row.findViewById(R.id.text1);
                make.setTypeface(RobotoFont.getInstance(this.getContext()).getTypeFace());
                make.setText( generos.get(position).toString());
                return make;
            }
        };
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spnGenero.setAdapter(adapter);
    }
}
