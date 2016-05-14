package epiphany_soft.wtw.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import epiphany_soft.wtw.ActivityBase;
import epiphany_soft.wtw.DataBase.DataBaseConnection;
import epiphany_soft.wtw.Fonts.RobotoFont;
import epiphany_soft.wtw.R;


public class ActivityAgregarUsuario extends ActivityBase {

    private EditText txtNombreU;
    private EditText txtContraseniaU;
    private EditText TxtConfirmarContraseniaU;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_usuario);
        txtNombreU = (EditText) findViewById(R.id.TxtNombreUser);
        txtContraseniaU= (EditText) findViewById(R.id.TxtContrasenia);
        TxtConfirmarContraseniaU=(EditText) findViewById(R.id.TxtConfirmarContrania);
        setTitle("AGREGAR USUARIO");
        setSpecialFonts();
    }

   private void setSpecialFonts(){
        txtNombreU.setTypeface(RobotoFont.getInstance(this).getTypeFace());
        txtContraseniaU.setTypeface(RobotoFont.getInstance(this).getTypeFace());
        TxtConfirmarContraseniaU.setTypeface(RobotoFont.getInstance(this).getTypeFace());
    }


    public  boolean EmailValid(String email) {
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

    public void onClickAgregarUsuarioNuevo(View v){

        if (txtNombreU.getText().toString().equals("")) {
            createToast("Introduzca su correo electrònico ");
            return;
        }
        if (txtContraseniaU.getText().toString().equals("")){
            createToast("Introduzca su contraseña");
            return;
        }
        if (TxtConfirmarContraseniaU.getText().toString().equals("")){
            createToast("Por favor Confirme su contrasenia");
            return;
        }
        String name = txtNombreU.getText().toString();
        boolean res= EmailValid(name);
        String paswword = txtContraseniaU.getText().toString();
        String paswwordnew = TxtConfirmarContraseniaU.getText().toString();
        if (res==true){
            if (paswword.equals(paswwordnew)){
                DataBaseConnection db = new DataBaseConnection(this.getBaseContext());
                boolean success = db.AgregarUsuario(name, paswword);
                if (success) createToast("El Usuario se Creo Exitosamente");
                else createToast("El Usuario ya se Encuentra Registrado.");
            }
            else {
                txtContraseniaU.setError("Las contraseñas no coinciden");
                TxtConfirmarContraseniaU.setError("Las contraseñas no coinciden");
               // createToast("Por favor verifique su contrasenia");
                return;
                
            }

        }
        else{
           // createToast("Por favor verifique su Correo Electrònico ");
            txtNombreU.setError("Correo incorrecto");
            return;

        }

    }
}
