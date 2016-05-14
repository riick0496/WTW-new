package epiphany_soft.wtw.Activities;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

import epiphany_soft.wtw.Activities.Series.ActivityDetalleSerie;
import epiphany_soft.wtw.ActivityBase;
import epiphany_soft.wtw.Adapters.DiaAdapter;
import epiphany_soft.wtw.Adapters.HorarioAdapter;
import epiphany_soft.wtw.DataBase.DataBaseConnection;
import epiphany_soft.wtw.DataBase.DataBaseContract.ProgramaContract;
import epiphany_soft.wtw.Negocio.Horario;
import epiphany_soft.wtw.R;

import static epiphany_soft.wtw.DataBase.DataBaseContract.CanalContract;
import static epiphany_soft.wtw.DataBase.DataBaseContract.HorarioContract;

/**
 * Created by Camilo on 15/04/2016.
 */
public class ActivityAsociarCanal extends ActivityBase {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    //private RecyclerView.Adapter dAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    String nombrePrograma;
    int idPrograma;
    //int id_dia;
    //TextView txtHora;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asociar_canal);
        setTitle("ASOCIAR CANAL");

        Bundle b = getIntent().getExtras();
        nombrePrograma = b.getString(ProgramaContract.COLUMN_NAME_PROGRAMA_NOMBRE);
        idPrograma = b.getInt(ProgramaContract.COLUMN_NAME_PROGRAMA_ID);
       // id_dia= b.getInt(DataBaseContract.DiaContract.COLUMN_NAME_ID_DIA);

        crearRecyclerViewCanales();
      //  setSpecialFonts();
    }

    private void crearRecyclerViewCanales(){
        DataBaseConnection db=new DataBaseConnection(this.getBaseContext());
        Cursor c=db.getHorariosPrograma(idPrograma);
        if (c!=null) {
            Horario[] horarios=new Horario[c.getCount()];
            int i=0;
            while (c.moveToNext()) {
                String nombreCanal = c.getString(c.getColumnIndex(CanalContract.COLUMN_NAME_CANAL_ID));
                int idPrograma = c.getInt(c.getColumnIndex(HorarioContract.COLUMN_NAME_PROGRAMA_ID));
                int idRel = c.getInt(c.getColumnIndex(HorarioContract.COLUMN_NAME_RELACION_ID));
                String Hora = c.getString(c.getColumnIndex(HorarioContract.COLUMN_NAME_RELACION_HORA));
                horarios[i] = new Horario(idRel,nombreCanal,idPrograma, Hora);
                i++;
            }
            this.crearRecyclerViewCanales(horarios);
        } else this.crearRecyclerViewCanales(null);
    }

    private void crearRecyclerViewCanales(Horario[] contenido){
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_consulta_canal);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        if (contenido!=null) {
            mAdapter = new HorarioAdapter(this,contenido,idPrograma);
            mRecyclerView.setAdapter(mAdapter);

        }
    }

    public void onClickRegistrarHorario(View v) {
       this.registrarInfoHorario();
        createToast("Cambios Realizados");
        this.finish();
    }

    private boolean registrarInfoHorario(){
        HorarioAdapter e = (HorarioAdapter) mAdapter;
        ArrayList<HorarioAdapter.ViewHolder> listHolder = e.getMisViewHolder();
        DataBaseConnection db = new DataBaseConnection(this.getBaseContext());
        int idPrograma;
        for (int i=0;i<listHolder.size();i++){
            HorarioAdapter.ViewHolder ev = listHolder.get(i);
            if (ev.isChecked) {
                idPrograma = ev.idPrograma;
                ev.mHorario.setIdPrograma(idPrograma);

                if (ev.mHorario.getId() != 0) {
                    // esto es para el actualizar, que porel momento no se va a realizar
                    db.eliminarHorario(ev.mHorario.getId());//TODO: buscar una mejor forma
                }
                    boolean success;
                    success = db.insertarHorario(ev.mHorario);
                    if (success) {
                        ev.ck.setChecked(true);
                        ev.mHorario.setId(db.getHorarioId(ev.mHorario.getIdPrograma(), ev.mHorario.getNombreCanal()));
                        ActivityDetalleSerie.actualizado = true;
                        ActivityDetallePelicula.actualizado = true;

                        //aqui puse lo de insertar el dia a la relacion diaHorario
                        DiaAdapter d = (DiaAdapter) ev.mAdapter;
                        ArrayList<DiaAdapter.ViewHolder> listadias = d.getMisViewHolder();
                        for (int j = 0; j < listadias.size(); j++) {
                            DiaAdapter.ViewHolder dia = listadias.get(j);
                            if (dia.isChecked) {
                                db.insertarRelacionDiaHorario(ev.mHorario.getId(), j + 1); //   (j+1) porque se almacenan los id de los dias

                            }
                        }

                    // cierra if de success
                }
            }
            else{
                boolean success;
                if (ev.mHorario.getId() != 0) {
                    success = db.eliminarHorario(ev.mHorario.getId());
                    if (success) {ev.ck.setChecked(false);
                        ev.mHorario.setId(0);
                        ev.mHorario.setIdPrograma(0);
                        ActivityDetalleSerie.actualizado = true;
                        ActivityDetallePelicula.actualizado = true;
                    }
                }
            }

               /* DiaAdapter d= (DiaAdapter) mAdapter;
                int id_horario= db.consultarIdHorario(idPrograma);
                ArrayList<DiaAdapter.ViewHolder> listadias = d.getMisViewHolder();
                for (int j=0;j<listadias.size();j++){
                    DiaAdapter.ViewHolder dia = listadias.get(i);
                    if (dia.isChecked){
                        db.insertarRelacionDiaHorario(id_horario,j+1); //   (j+1) porque se almacenan los id de los dias

                    }
                }*/
        }
        return true;
    }

}
