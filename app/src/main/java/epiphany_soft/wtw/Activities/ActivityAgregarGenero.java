package epiphany_soft.wtw.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import epiphany_soft.wtw.ActivityBase;
import epiphany_soft.wtw.DataBase.DataBaseConnection;
import epiphany_soft.wtw.Fonts.RobotoFont;
import epiphany_soft.wtw.R;

/**
 * Created by Camilo on 27/03/2016.
 */
public class ActivityAgregarGenero extends ActivityBase {

    private EditText txtAgregar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_genero);
        txtAgregar = (EditText) findViewById(R.id.txtAgregarGenero);
        setTitle("AGREGAR GÉNERO");
        setSpecialFonts();
    }

    private void setSpecialFonts(){
        txtAgregar.setTypeface(RobotoFont.getInstance(this).getTypeFace());
    }

    public void onClickAgregarGenero(View v){
        String text = txtAgregar.getText().toString().trim();
        if (text.equals("")){
            txtAgregar.setError("Introduzca un género");
            return;
        }
        //TODO: Revisar si es mejor usar v.getContext()
        DataBaseConnection db=new DataBaseConnection(this.getBaseContext());
        boolean success=db.insertarGenero(text);
        if (success) createToast("Genero creado");
        else createToast("El género ya existe");
    }
}
