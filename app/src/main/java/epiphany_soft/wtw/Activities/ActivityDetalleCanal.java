package epiphany_soft.wtw.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import epiphany_soft.wtw.ActivityBase;
import epiphany_soft.wtw.Adapters.DetalleCanalAdapter;
import epiphany_soft.wtw.DataBase.DataBaseConnection;
import epiphany_soft.wtw.DataBase.DataBaseContract;
import epiphany_soft.wtw.Fonts.RobotoFont;
import epiphany_soft.wtw.Fonts.SpecialFont;
import epiphany_soft.wtw.Negocio.Canal;
import epiphany_soft.wtw.Negocio.Emisora;
import epiphany_soft.wtw.Negocio.Sesion;
import epiphany_soft.wtw.R;

/**
 * Created by Camilo on 11/04/2016.
 */
public class ActivityDetalleCanal extends ActivityBase {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    TextView nombreTxt;

    private int id_canal, id_emisora;
    public static boolean actualizado=false;
    public static boolean eliminado=false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_canal);

        setTitle("INFORMACIÒN CANAL");
        setTitle(Canal.getInstance().getNombreCanal());
        ((TextView) findViewById(R.id.txtNombreCanal)).setText(Canal.getInstance().getNombreCanal());

        this.setSpecialFonts();
        this.crearRecyclerViewEmisora();
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

    @Override
    public void onResume(){
        super.onResume();
        if (eliminado){
            actualizado=false;
            eliminado=false;
            this.finish();
        }
        if (actualizado) {
            ((TextView) findViewById(R.id.txtNombreCanal)).setText(Canal.getInstance().getNombreCanal());
            this.recreate();
        }
        actualizado=false;
    }



    private void crearRecyclerViewEmisora(){
        DataBaseConnection db=new DataBaseConnection(this.getBaseContext());
        Cursor c=db.consultarNombreEmisoras(Canal.getInstance().getNombreCanal());
        if (c!=null) {
            Emisora[] emisoras=new Emisora[c.getCount()];
            int i=0;
            while (c.moveToNext()){
                 String nombreemisoras=c.getString(c.getColumnIndex(DataBaseContract.EmisoraContract.COLUMN_NAME_EMISORA_NOMBRE));
                int numerocanal=c.getInt(c.getColumnIndex(DataBaseContract.EmiteContract.COLUMN_NAME_CANAL_NUMERO));
                emisoras[i] = new Emisora(numerocanal,nombreemisoras);
                i++;
            }
            this.crearRecyclerViewEmisora(emisoras);
        }
    }

    private void crearRecyclerViewEmisora(Emisora[] contenido){
        LinearLayout layoutRV = (LinearLayout) findViewById(R.id.layoutCanalRV);
        Float height = getResources().getDimension(R.dimen.size_emisora)*(contenido.length);
        TableRow.LayoutParams params = new TableRow.LayoutParams(200, height.intValue());
        layoutRV.setLayoutParams(params);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_consulta_emisoras);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        if (contenido!=null) {
            mAdapter = new DetalleCanalAdapter(contenido);
            mRecyclerView.setAdapter(mAdapter);
        }
    }


    public void onClickActualizarCanal(View v){
        Intent i = new Intent(this, ActivityActualizarCanal.class);
        startActivity(i);
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



    /*
    private void crearRecyclerViewEmisora(){
        DataBaseConnection db=new DataBaseConnection(this.getBaseContext());
    Cursor c=db.consultarNombreEmisoras(nombreCanal);
    if (c!=null) {
        String[] nombreemisoras=new String[c.getCount()];
        String[] numerocanal=new String[c.getCount()];
        int i=0;
        while (c.moveToNext()){
            nombreemisoras[i]=c.getString(c.getColumnIndex(DataBaseContract.EmisoraContract.COLUMN_NAME_EMISORA_NOMBRE));
            numerocanal[i]="#Canal: "+c.getString(c.getColumnIndex(DataBaseContract.EmiteContract.COLUMN_NAME_CANAL_NUMERO));
            i++;
        }
        this.crearRecyclerViewEmisora(nombreemisoras, numerocanal);
    }
}*/


}
