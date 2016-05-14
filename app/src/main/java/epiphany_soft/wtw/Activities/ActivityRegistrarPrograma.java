package epiphany_soft.wtw.Activities;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import epiphany_soft.wtw.ActivityBase;
import epiphany_soft.wtw.DataBase.DataBaseConnection;
import epiphany_soft.wtw.DataBase.DataBaseContract;
import epiphany_soft.wtw.Fonts.RobotoFont;
import epiphany_soft.wtw.Fonts.SpecialFont;
import epiphany_soft.wtw.Negocio.Genero;
import epiphany_soft.wtw.R;

public class ActivityRegistrarPrograma extends ActivityBase{
    private EditText name,sinopsis,anio,pais;
    private Spinner spnGenero;
    private RadioButton pel,ser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_pel_ser);
        setTitle("AGREGAR PROGRAMA");

        pel=(RadioButton)findViewById(R.id.but_pel);
        ser=(RadioButton)findViewById(R.id.but_ser);
        name=(EditText)findViewById(R.id.name_programa);
        sinopsis=(EditText)findViewById(R.id.sin_programa);
        anio =(EditText)findViewById(R.id.txtAnioEstreno);
        pais =(EditText)findViewById(R.id.txtPaisOrigen);
        crearSpinnerGeneros();
        setSpecialFonts();
    }

    private void setSpecialFonts(){
        TextView tipo=(TextView) findViewById(R.id.lblTipoPrograma);
        tipo.setTypeface(SpecialFont.getInstance(this).getTypeFace());
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
        pel.setTypeface(RobotoFont.getInstance(this).getTypeFace());
        ser.setTypeface(RobotoFont.getInstance(this).getTypeFace());
        name.setTypeface(RobotoFont.getInstance(this).getTypeFace());
        sinopsis.setTypeface(RobotoFont.getInstance(this).getTypeFace());
        anio.setTypeface(RobotoFont.getInstance(this).getTypeFace());
        pais.setTypeface(RobotoFont.getInstance(this).getTypeFace());
    }

    public void onClickRegistrar(View v)
    {
        if (name.getText().toString().trim().equals("")) {
            name.setError("Introduzca un nombre");
            return;
        }
        if (anio.getText().toString().trim().equals("")){
            anio.setError("Introduzca un año");
            return;
        }
        String nombre=name.getText().toString().trim();
        String sinopsisS=sinopsis.getText().toString().trim();
        int anioEstreno=Integer.parseInt(anio.getText().toString());
        String paisOrigen=pais.getText().toString().trim();
        int idGen=((Genero)spnGenero.getSelectedItem()).getId();
        if (pel.isChecked()==true) {
            DataBaseConnection db=new DataBaseConnection(this.getBaseContext());
            boolean success=db.insertarPrograma(idGen,nombre,sinopsisS,anioEstreno,paisOrigen);
            if (success){
                int id=db.consultarId_Programa(nombre);
                success=db.insertarPelicula(id);
                if (success) createToast("Película creada");
                else createToast("El programa ya existe");
            }
            else createToast("El programa ya existe");
        }
        else if (ser.isChecked()==true) {
            DataBaseConnection db=new DataBaseConnection(this.getBaseContext());
            boolean success=db.insertarPrograma(idGen,nombre,sinopsisS,anioEstreno,paisOrigen);
            if (success){
                int id=db.consultarId_Programa(nombre);
                success=db.insertarSerie(id);
                if (success) createToast("Serie creada");
                else createToast("El programa ya existe");
            }
            else createToast("El programa ya existe");
        }
    }

    public void crearSpinnerGeneros(){
        spnGenero = (Spinner) findViewById(R.id.spnGenero);
        DataBaseConnection db=new DataBaseConnection(this.getBaseContext());
        Cursor c=db.consultarAllGeneros();
        final ArrayList<Genero> generos = new ArrayList<Genero>();
        while (c.moveToNext()){
            Genero g=
                    new Genero(c.getInt(c.getColumnIndex(DataBaseContract.GeneroContract.COLUMN_NAME_GENERO_ID)),
                            c.getString(c.getColumnIndex(DataBaseContract.GeneroContract.COLUMN_NAME_GENERO_NOMBRE)));
            generos.add(g);
        }
        ArrayAdapter adapter = new ArrayAdapter(this,R.layout.simple_spinner_item,generos){
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
