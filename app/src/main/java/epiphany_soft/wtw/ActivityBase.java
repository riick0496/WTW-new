package epiphany_soft.wtw;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Camilo on 2/04/2016.
 */
public abstract class ActivityBase extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void createToast(String message){
        Context context = getApplicationContext();
        CharSequence text = message;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.TOP, 0, 350);
        toast.show();
    }

    @Override
    /**Esta funcion sirve para poner el botón de regresar a la anterior actividad
     *
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //hago un case por si en un futuro agrego mas opciones
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    /**
     * Patrón plantilla!!!
     */
    public void onResume(){
        super.onResume();
        hideWhenNoSession();
        showWhenSession();
    }

    protected void hideWhenNoSession(){}

    protected void showWhenSession(){}

    protected void hide(View v){
        v.setVisibility(View.GONE);
    }

    protected void show(View v){
        v.setVisibility(View.VISIBLE);
    }
}
