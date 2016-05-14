package epiphany_soft.wtw.Activities;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import epiphany_soft.wtw.Activities.Series.ActivityDetalleSerie;
import epiphany_soft.wtw.ActivityBase;
import epiphany_soft.wtw.Adapters.EmisoraActualizarAdapter;
import epiphany_soft.wtw.DataBase.DataBaseConnection;
import epiphany_soft.wtw.DataBase.DataBaseContract;
import epiphany_soft.wtw.Fonts.RobotoFont;
import epiphany_soft.wtw.Fonts.SpecialFont;
import epiphany_soft.wtw.Negocio.Canal;
import epiphany_soft.wtw.Negocio.Emite;
import epiphany_soft.wtw.R;

/**
 * Created by Camilo on 11/04/2016.
 */
public class ActivityActualizarCanal extends ActivityBase {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    TextView nombreTxt;
   private  EditText name;
    private String nombreCanal;
    private int id_canal, id_emisora;
    public static boolean actualizado=false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_canal);

        setTitle("INFORMACIÒN CANAL");
        name=(EditText)findViewById(R.id.txtNombreCanal);

        nombreCanal = Canal.getInstance().getNombreCanal();
        setTitle(nombreCanal);
        name.setText(nombreCanal);
        //((TextView) findViewById(R.id.txtNombreCanal)).setText(nombreCanal);

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
        if (actualizado) this.recreate();
        actualizado=false;
    }

    private void crearRecyclerViewEmisora(){
        DataBaseConnection db=new DataBaseConnection(this.getBaseContext());
        Cursor c=db.consultarEmisorasDeCanal(nombreCanal);
        if (c!=null) {
            Emite[] emites=new Emite[c.getCount()];
            int i=0;
            while (c.moveToNext()){
                 String nombre_emisora=c.getString(c.getColumnIndex(DataBaseContract.EmisoraContract.COLUMN_NAME_EMISORA_NOMBRE));
                int numero_canal=c.getInt(c.getColumnIndex(DataBaseContract.EmiteContract.COLUMN_NAME_CANAL_NUMERO));
                int id_emisora=c.getInt(c.getColumnIndex(DataBaseContract.EmisoraContract.COLUMN_NAME_EMISORA_ID));
                String nombre_canal =c.getString(c.getColumnIndex(DataBaseContract.EmiteContract.COLUMN_NAME_CANAL_ID));

                emites[i] = new Emite(id_emisora,nombre_canal, numero_canal,nombre_emisora);
                

                i++;
            }
            this.crearRecyclerViewEmisora(emites);
        }
    }

    private void crearRecyclerViewEmisora(Emite[] contenido){
        LinearLayout layoutRV = (LinearLayout) findViewById(R.id.layoutCanalRV);
        Float height = getResources().getDimension(R.dimen.size_emisora)*(contenido.length);
        TableRow.LayoutParams params = new TableRow.LayoutParams(200, height.intValue());
        layoutRV.setLayoutParams(params);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_consulta_emisoras);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        if (contenido!=null) {
            mAdapter = new EmisoraActualizarAdapter(contenido);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

 // metodos para actualizar ...... (aun no se terminan. )
    public void onClickActualizarC(View v) {
            if (nombreTxt.getText().toString().trim().equals("")) {
                nombreTxt.setError("Introduzca el nombre del canal");
                return;
            }
            this.ActualizarInfoCanal();
    }

        private void ActualizarInfoCanal(){
            DataBaseConnection db=new DataBaseConnection(this.getBaseContext());
            // el actualizar esta mal , toca cambiar el parametro para nuevo y viejo ojo.. por ahora lo ppongo asi
            boolean success=db.actualizarCanal(nombreCanal, nombreTxt.getText().toString() );
            if (success) {
                nombreCanal =  nombreTxt.getText().toString();
                ActualizarEmite();
                createToast("Canal Actualizado");
                ActivityDetalleCanal.actualizado=true;
                Canal.getInstance().setNombreCanal(nombreCanal);
                ActivityConsultarCanal.actualizado=true;
                this.finish();
            }
            else createToast("El nombre ya existe");
        }

        private boolean ActualizarEmite(){
            EmisoraActualizarAdapter e = (EmisoraActualizarAdapter) mAdapter;
            ArrayList<EmisoraActualizarAdapter.ViewHolder> listHolder = e.getMisViewHolder();
            String nombreCanal = nombreTxt.getText().toString();
            int idEmisora;
            Integer numCanal;
            for (int i=0;i<listHolder.size();i++){
                EmisoraActualizarAdapter.ViewHolder ev = listHolder.get(i);
                idEmisora = ev.idEmisora;
                if (!ev.numCanalEdit.getText().toString().equals(""))
                    numCanal = new Integer(ev.numCanalEdit.getText().toString());
                else
                    numCanal = null;
                DataBaseConnection db=new DataBaseConnection(this.getBaseContext());
                if (ev.ck.isChecked()){
                    // esto esta mal toca actualizar el viejo  y el nuevo
                    if (ev.nombre_canal!=null)
                        db.actualizarEmite(nombreCanal,nombreTxt.getText().toString(),idEmisora,numCanal);
                    else
                        db.insertarEmite(nombreTxt.getText().toString(),idEmisora,numCanal);
                } else{
                    if (ev.nombre_canal!=null)
                        db.eliminarEmite(nombreCanal,idEmisora);
                }
            }
            return true;
        }



    public void onClickEliminar(View v) {

        DataBaseConnection db=new DataBaseConnection(this.getBaseContext());
        boolean success = db.eliminarCanal(nombreCanal);
        if (success) {
            createToast("El Canal se Elimino Exitosamente");
            //ActivityConsultarCanal.actualizado=true;
            ActivityDetalleCanal.eliminado=true;
            ActivityDetalleSerie.actualizado=true;
            ActivityDetallePelicula.actualizado=true;
            ActivityConsultarCanal.actualizado=true;
           this.finish();

        } else createToast("Ocurrió un error al Eliminar");
    }


}
