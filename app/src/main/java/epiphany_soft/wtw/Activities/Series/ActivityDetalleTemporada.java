package epiphany_soft.wtw.Activities.Series;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import epiphany_soft.wtw.ActivityBase;
import epiphany_soft.wtw.Adapters.CapituloAdapter;
import epiphany_soft.wtw.DataBase.DataBaseConnection;
import epiphany_soft.wtw.DataBase.DataBaseContract;
import epiphany_soft.wtw.Negocio.Sesion;
import epiphany_soft.wtw.R;

/**
 * Created by Camilo on 2/04/2016.
 */
public class ActivityDetalleTemporada extends ActivityBase {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    int idSerie, idTemporada;
    String nombreSerie;

    public static boolean actualizado=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_temporada);

        Bundle b = getIntent().getExtras();
        idSerie = b.getInt(DataBaseContract.TemporadaContract.COLUMN_NAME_PROGRAMA_ID);
        idTemporada = b.getInt(DataBaseContract.TemporadaContract.COLUMN_NAME_TEMPORADA_ID);
        nombreSerie = b.getString(DataBaseContract.ProgramaContract.COLUMN_NAME_PROGRAMA_NOMBRE);

        setTitlePersonalizado();
        crearRecycledViewCapitulos();
    }

    private void setTitlePersonalizado(){
        String numTemp = Integer.toString(idTemporada);
        if (numTemp.length()==1) numTemp = "0"+numTemp;
        setTitle(nombreSerie + "-T" + numTemp +": Capítulos");
    }

    @Override
    public void onResume(){
        super.onResume();
        if (actualizado) this.recreate();
        actualizado=false;
    }

    public void onClickAgregarCapitulo(View v){
        Intent i = new Intent(this, ActivityAgregarCapitulo.class);
        Bundle b = new Bundle();
        b.putInt(DataBaseContract.CapituloContract.COLUMN_NAME_TEMPORADA_ID, idTemporada);
        b.putInt(DataBaseContract.CapituloContract.COLUMN_NAME_SERIE_ID,idSerie);
        b.putString(DataBaseContract.ProgramaContract.COLUMN_NAME_PROGRAMA_NOMBRE,nombreSerie);
        i.putExtras(b);
        startActivity(i);
    }

    private void crearRecycledViewCapitulos(){
        DataBaseConnection db=new DataBaseConnection(this.getBaseContext());
        Cursor c=db.consultarCapitulosPorTemporada(idTemporada,idSerie);
        if (c!=null) {
            String[] nombresCap=new String[c.getCount()];
            String[] numerosCap=new String[c.getCount()];
            int i=0;
            while (c.moveToNext()){
                nombresCap[i]=c.getString(c.getColumnIndex(DataBaseContract.CapituloContract.COLUMN_NAME_CAPITULO_NOMBRE));
                numerosCap[i]=c.getString(c.getColumnIndex(DataBaseContract.CapituloContract.COLUMN_NAME_CAPITULO_ID));
                i++;
            }
            this.crearRecycledView(numerosCap,nombresCap);
        }
    }

    private void crearRecycledView(String[] numCapitulos,String[] nombreCapitulos){
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_consultar_capitulo);
        // Se usa cuando se sabe que cambios en el contenido no cambian el tamaño del layout
        mRecyclerView.setHasFixedSize(true);
        // Se usa un layout manager lineal para el recycler view
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // Se especifica el adaptador
        if (numCapitulos!=null && nombreCapitulos!=null) {
            mAdapter = new CapituloAdapter(numCapitulos,nombreCapitulos);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    protected void hideWhenNoSession(){
        if (!Sesion.getInstance().isActiva()){
            FloatingActionButton b = (FloatingActionButton) findViewById(R.id.fab);
            hide(b);
        }
    }

    protected void showWhenSession(){
        if (Sesion.getInstance().isActiva()){
            FloatingActionButton b = (FloatingActionButton) findViewById(R.id.fab);
            show(b);
        }
    }

    public int getIdSerie(){
        return idSerie;
    }

    public int getIdTemporada(){
        return idTemporada;
    }

    public String getNombreSerie(){
        return nombreSerie;
    }
}
