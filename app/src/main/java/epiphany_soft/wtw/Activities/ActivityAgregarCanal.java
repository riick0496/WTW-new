package epiphany_soft.wtw.Activities;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import epiphany_soft.wtw.ActivityBase;
import epiphany_soft.wtw.Adapters.EmisoraAdapter;
import epiphany_soft.wtw.DataBase.DataBaseConnection;
import epiphany_soft.wtw.DataBase.DataBaseContract;
import epiphany_soft.wtw.Fonts.RobotoFont;
import epiphany_soft.wtw.Fonts.SpecialFont;
import epiphany_soft.wtw.Negocio.Emisora;
import epiphany_soft.wtw.R;

/**
 * Created by Camilo on 11/04/2016.
 */
public class ActivityAgregarCanal extends ActivityBase {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    TextView nombreTxt;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_canal);

        setTitle("AGREGAR CANAL");

        setSpecialFonts();
        llenarRecyclerOnCreate();
    }

    private void setSpecialFonts(){
        TextView nombreLabel=(TextView) findViewById(R.id.lblNombreCanal);
        nombreLabel.setTypeface(SpecialFont.getInstance(this).getTypeFace());
        TextView emisorasLabel=(TextView) findViewById(R.id.lblEmisoras);
        emisorasLabel.setTypeface(SpecialFont.getInstance(this).getTypeFace());
        //Los textos
        nombreTxt=(TextView) findViewById(R.id.txtNombreCanal);
        nombreTxt.setTypeface(RobotoFont.getInstance(this).getTypeFace());
    }

    public void onClickRegistrarCanal(View v) {
        if (nombreTxt.getText().toString().trim().equals("")) {
            nombreTxt.setError("Introduzca un nombre");
            return;
        }
        this.registrarInfoCanal();
    }

    private void registrarInfoCanal(){
        DataBaseConnection db=new DataBaseConnection(this.getBaseContext());
        boolean success=db.insertarCanal(nombreTxt.getText().toString());
        if (success) {
            registrarEmite();
            createToast("Canal creado");
        }
        else createToast("El canal ya existe");
    }

    private boolean registrarEmite(){
        EmisoraAdapter e = (EmisoraAdapter) mAdapter;
        ArrayList<EmisoraAdapter.ViewHolder> listHolder = e.getMisViewHolder();
        String nombreCanal = nombreTxt.getText().toString();
        int idEmisora;
        Integer numCanal;
        for (int i=0;i<listHolder.size();i++){
            EmisoraAdapter.ViewHolder ev = listHolder.get(i);
            if (ev.ck.isChecked()){
                idEmisora = ev.idEmisora;
                if (!ev.numCanalEdit.getText().toString().equals(""))
                    numCanal = new Integer(ev.numCanalEdit.getText().toString());
                else
                    numCanal = null;
                DataBaseConnection db=new DataBaseConnection(this.getBaseContext());
                db.insertarEmite(nombreCanal,idEmisora,numCanal);
            }
        }
        return true;
    }

    private void crearRecycledView(Emisora[] contenido){
        LinearLayout layoutRV = (LinearLayout) findViewById(R.id.layoutCanalRV);
        Float height = getResources().getDimension(R.dimen.size_emisora)*(contenido.length);
        TableRow.LayoutParams params = new TableRow.LayoutParams(200, height.intValue());
        layoutRV.setLayoutParams(params);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_consulta_emisoras);
        // Se usa un layout manager lineal para el recycler view
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // Se especifica el adaptador
        if (contenido!=null) {
            mAdapter = new EmisoraAdapter(contenido);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    private void llenarRecyclerOnCreate(){
        DataBaseConnection db=new DataBaseConnection(this.getBaseContext());
        Cursor c=db.consultarAllEmisoras();
        if (c!=null) {
            Emisora[] emisoras=new Emisora[c.getCount()];
            int i=0;
            while (c.moveToNext()) {
                String nombre = c.getString(c.getColumnIndex(DataBaseContract.EmisoraContract.COLUMN_NAME_EMISORA_NOMBRE));
                int id = c.getInt(c.getColumnIndex(DataBaseContract.EmisoraContract.COLUMN_NAME_EMISORA_ID));
                emisoras[i] = new Emisora(id,nombre);
                i++;
            }
            this.crearRecycledView(emisoras);
        } else this.crearRecycledView(null);
    }
}
