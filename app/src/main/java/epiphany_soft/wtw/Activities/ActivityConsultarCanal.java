package epiphany_soft.wtw.Activities;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import epiphany_soft.wtw.ActivityBase;
import epiphany_soft.wtw.Adapters.CanalAdapter;
import epiphany_soft.wtw.DataBase.DataBaseConnection;
import epiphany_soft.wtw.DataBase.DataBaseContract;
import epiphany_soft.wtw.Fonts.RobotoFont;
import epiphany_soft.wtw.R;


public class ActivityConsultarCanal extends ActivityBase {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private EditText txtBuscar;

    public static boolean actualizado=false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_canales);
        txtBuscar = (EditText) findViewById(R.id.txtBuscarCanal);
        setTitle("CONSULTAR CANAL");
        llenarRecyclerOnCreate();
        setSpecialFonts();
    }

    private void setSpecialFonts(){
        txtBuscar.setTypeface(RobotoFont.getInstance(this).getTypeFace());
    }

    @Override
    public void onResume(){
        super.onResume();
        if (actualizado) this.recreate();
        actualizado=false;
    }

    private void crearRecycledView(String[] contenido){
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_consulta_canal);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        if (contenido!=null) {
            mAdapter = new CanalAdapter(contenido);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    public void onClickBuscar(View v) {
        String text = txtBuscar.getText().toString();
        DataBaseConnection db=new DataBaseConnection(this.getBaseContext());
        if (!text.equals("")){
            Cursor c=db.consultarCanalLikeNombre(text);
            if (c!=null) {
                String[] nombres=new String[c.getCount()];
                int i=0;
                while (c.moveToNext()) {
                    nombres[i] = c.getString(c.getColumnIndex(DataBaseContract.CanalContract.COLUMN_NAME_CANAL_ID));
                    i++;
                }
                this.crearRecycledView(nombres);
                if (c.getCount()==0) createToast("No se encontraron resultados");
            }
        }
    }

    private void llenarRecyclerOnCreate(){
        DataBaseConnection db=new DataBaseConnection(this.getBaseContext());
        Cursor c=db.consultarCanalLikeNombre("");
        if (c!=null) {
            String[] nombres=new String[c.getCount()];
            int i=0;
            while (c.moveToNext()) {
                nombres[i] = c.getString(c.getColumnIndex(DataBaseContract.CanalContract.COLUMN_NAME_CANAL_ID));
                i++;
            }
            this.crearRecycledView(nombres);
        } else this.crearRecycledView(null);
    }
}
