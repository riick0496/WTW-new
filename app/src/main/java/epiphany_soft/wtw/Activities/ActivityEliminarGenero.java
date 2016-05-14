package epiphany_soft.wtw.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import epiphany_soft.wtw.ActivityBase;
import epiphany_soft.wtw.DataBase.DataBaseConnection;
import epiphany_soft.wtw.DataBase.DataBaseContract;
import epiphany_soft.wtw.Fonts.RobotoFont;
import epiphany_soft.wtw.Negocio.Genero;
import epiphany_soft.wtw.R;

/**
 * Created by Camilo on 27/03/2016.
 */
public class ActivityEliminarGenero extends ActivityBase {
    private Spinner spnGenero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar_genero);
        setTitle("ELIMINAR GÉNERO");
        crearSpinnerGenerosNoUsados();
        if (spnGenero.getCount()<1){
            Button b = (Button) findViewById(R.id.btnEliminarGenero);
            b.setTextColor(Color.GRAY);
            b.setEnabled(false);
        }
    }

    public void onClickBtnEliminarGenero(View v){
        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogoConfirmacion dialogo = new DialogoConfirmacion();
        dialogo.show(fragmentManager, "tagConfirmacion");
    }

    public void crearSpinnerGenerosNoUsados(){
        spnGenero = (Spinner) findViewById(R.id.spnGeneroElim);
        DataBaseConnection db=new DataBaseConnection(this.getBaseContext());
        Cursor c=db.getGenerosNoUsados();
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

    @SuppressLint("ValidFragment")
    public class DialogoConfirmacion extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder =
                    new AlertDialog.Builder(getActivity());

            builder.setMessage("¿Confirma la acción seleccionada?")
                    .setTitle("Confirmacion")
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()  {
                        public void onClick(DialogInterface dialog, int id) {
                            int idGen = ((Genero) spnGenero.getSelectedItem()).getId();
                            DataBaseConnection db = new DataBaseConnection(getBaseContext());
                            boolean success = db.eliminarGenero(idGen);
                            if (success) {
                                createToast("Genero eliminado");
                                crearSpinnerGenerosNoUsados();
                                if (spnGenero.getCount() < 1) {
                                    Button b = (Button) findViewById(R.id.btnEliminarGenero);
                                    b.setEnabled(false);
                                }
                            } else createToast("Ocurrió un error");
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            createToast("Acción cancelada");
                            dialog.cancel();
                        }
                    });

            return builder.create();
        }
    }
}
