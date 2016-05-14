package epiphany_soft.wtw.Activities;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import epiphany_soft.wtw.ActivityBase;
import epiphany_soft.wtw.DataBase.DataBaseConnection;
import epiphany_soft.wtw.DataBase.DataBaseContract;
import epiphany_soft.wtw.Fonts.RobotoFont;
import epiphany_soft.wtw.Fonts.SpecialFont;
import epiphany_soft.wtw.Negocio.Genero;
import epiphany_soft.wtw.Negocio.Sesion;
import epiphany_soft.wtw.R;

/**
 * Created by Camilo on 9/04/2016.
 */
public class ActivityModificarUsuario extends ActivityBase {

    private EditText nombre,password, passwordConf;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_usuario);
        setTitle("MODIFICAR USUARIO");

        nombre = (EditText) findViewById(R.id.txtNombreUsuario);
        password = (EditText) findViewById(R.id.txtContrasenia);
        passwordConf = (EditText) findViewById(R.id.txtConfirmarContrasenia);
       // aqui toma el valor del nombre de user coon el que inicio sesiòn
        nombre.setText(Sesion.getInstance().getNombreUsuario());
        this.setSpecialFonts();
    }

    private void setSpecialFonts(){
        TextView nombreLabel=(TextView) findViewById(R.id.lblNombreUsuario);
        nombreLabel.setTypeface(SpecialFont.getInstance(this).getTypeFace());
        TextView passwordLabel=(TextView) findViewById(R.id.lblContrasenia);
        passwordLabel.setTypeface(SpecialFont.getInstance(this).getTypeFace());
        //Los textos
        nombre.setTypeface(RobotoFont.getInstance(this).getTypeFace());
        password.setTypeface(RobotoFont.getInstance(this).getTypeFace());
        passwordConf.setTypeface(RobotoFont.getInstance(this).getTypeFace());
    }

    public  boolean emailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }


    public void onClickActualizarUsuario(View v) {

        if (nombre.getText().toString().trim().equals("")) {
            nombre.setError("Introduzca un nombre de usuario");
            return;
        }
        if (password.getText().toString().equals("")) {
            password.setError("Introduzca una contraseña");
            return;
        }
        if (passwordConf.getText().toString().equals("")) {
            passwordConf.setError("Confirme su contraseña");
            return;
        }
        /*if (!emailValid(password.getText().toString())) {
            nombre.setError("Correo incorrecto");
            return;
        }*/
        if (!password.getText().toString().equals(passwordConf.getText().toString())){
            password.setError("Las contraseñas no coinciden");
            passwordConf.setError("Las contraseñas no coinciden");
            return;
        }

        String name = nombre.getText().toString();
        boolean res= emailValid(name);
        String paswword = password.getText().toString();
       // int idUser=Sesion.getInstance().getIdUsuario();
        DataBaseConnection db = new DataBaseConnection(this.getBaseContext());
      /*Cursor c=db.consultarNombreUser(name);
        if (c.moveToNext()==true){
            createToast("El correo  ya se encuentra registrado");
         } */

        if (res==true) {
            boolean success = db.actualizarUsuario(name, paswword);
            if (success) createToast("El Usuario se Actualizò Exitosamente");
            else createToast("El usuario no se pudo  Actualizar");
       }
       else{
            nombre.setError("Correo incorrecto");
         }
    }



    }


