package epiphany_soft.wtw.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import epiphany_soft.wtw.DataBase.DataBaseConnection;
import epiphany_soft.wtw.Fonts.RobotoFont;
import epiphany_soft.wtw.Negocio.Sesion;
import epiphany_soft.wtw.R;

import static epiphany_soft.wtw.DataBase.DataBaseContract.UsuarioContract;

/**
 * Created by Camilo on 7/04/2016.
 */
public class ActivityInicioSesion extends AppCompatActivity {
    private EditText txtNombre, txtPassword;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);
        //this.getSupportActionBar().hide();

        txtNombre = (EditText) findViewById(R.id.txtNombreUsuario);
        txtPassword = (EditText) findViewById(R.id.txtContrasenia);
        this.setSpecialFonts();
    }

    private void setSpecialFonts(){
        //Los textos
        txtNombre.setTypeface(RobotoFont.getInstance(this).getTypeFace());
        txtPassword.setTypeface(RobotoFont.getInstance(this).getTypeFace());

    }

    public void onClickInicioSesion(View v){
        String nombre = txtNombre.getText().toString();
        String password = txtPassword.getText().toString();
        if (nombre.equals("")){
            txtNombre.setError("Introduzca un nombre de usuario");
            return;
        }
        if (password.equals("")){
            txtPassword.setError("Introduzca una password");
            return;
        }
        DataBaseConnection db=new DataBaseConnection(this.getBaseContext());
        Cursor c =db.consultarSesion(nombre,password);
        if (c!=null && c.getCount()==1) {
            c.moveToNext();
            Sesion s=Sesion.getInstance();
            s.setActiva(true);
            String nombreCur = c.getString(c.getColumnIndex(UsuarioContract.COLUMN_NAME_USUARIO_NOMBRE));
            int idCur = c.getInt(c.getColumnIndex(UsuarioContract.COLUMN_NAME_USUARIO_ID));
            s.setNombreUsuario(nombreCur);
            s.setIdUsuario(idCur);
            saveUserInfo(idCur,nombreCur,true);
            this.finish();
        }
        else createToast("Usuario o password incorrecto");
    }

    private void saveUserInfo(int idCur, String nombreCur, boolean isActiva){
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.file_user_info),MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.key_id_usuario_session),idCur);
        editor.putString(getString(R.string.key_nombre_usuario_session),nombreCur);
        editor.putBoolean(getString(R.string.key_active_session),isActiva);
        editor.commit();
    }

    public void onClickRegistrarUsuario(View v){
        Intent i = new Intent(this.getBaseContext(), ActivityAgregarUsuario.class);
        this.startActivity(i);
    }

    public void createToast(String message){
        Context context = getApplicationContext();
        CharSequence text = message;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.TOP,0,350);
        toast.show();
    }
}
