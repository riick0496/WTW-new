package epiphany_soft.wtw.Activities.Series;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TableRow;
import android.widget.TextView;

import epiphany_soft.wtw.Activities.ActivityAsociarCanal;
import epiphany_soft.wtw.Activities.ActivityConsultarPrograma;
import epiphany_soft.wtw.Activities.ActivityConsultarProgramasAgenda;
import epiphany_soft.wtw.ActivityBase;
import epiphany_soft.wtw.Adapters.CanalAdapter;
import epiphany_soft.wtw.Adapters.TemporadaAdapter;
import epiphany_soft.wtw.DataBase.DataBaseConnection;
import epiphany_soft.wtw.DataBase.DataBaseContract;
import epiphany_soft.wtw.Fonts.RobotoFont;
import epiphany_soft.wtw.Fonts.SpecialFont;
import epiphany_soft.wtw.Negocio.Sesion;
import epiphany_soft.wtw.R;

import static epiphany_soft.wtw.DataBase.DataBaseContract.GeneroContract;
import static epiphany_soft.wtw.DataBase.DataBaseContract.ProgramaContract;

// se supone que esta clase con ss metodos ya esta bn .. :)
public class ActivityDetalleSerie extends ActivityBase{
    private RecyclerView recyclerViewTemp, recyclerViewCanal;
    private RecyclerView.Adapter adapterTemp, adapterCanal;
    private RecyclerView.LayoutManager layoutManagerTemp, layoutManagerCanal;

    private String nombre,sinopsis,genero,pais;
    private int anio, idSerie;
    private boolean calificado, isFavorito;
    public static boolean actualizado=false;

    private ImageButton btnImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_serie);

        Bundle b = getIntent().getExtras();
        String nombreSerie = b.getString(ProgramaContract.COLUMN_NAME_PROGRAMA_NOMBRE);
        btnImg = (ImageButton)findViewById(R.id.btnImg);
        setTitle(nombreSerie);
        this.llenarInfo(nombreSerie);
        crearRecyclerViewTemporadas();
        crearRecyclerViewCanales();
        this.configurarRatingBar();
        this.configurarImageButton();
        setSpecialFonts();
    }

    private void setSpecialFonts(){
        TextView nombreLabel=(TextView) findViewById(R.id.lblNombrePrograma);
        nombreLabel.setTypeface(SpecialFont.getInstance(this).getTypeFace());
        TextView sinopsisLabel=(TextView) findViewById(R.id.lblSinopsis);
        sinopsisLabel.setTypeface(SpecialFont.getInstance(this).getTypeFace());
        TextView lblTemporadas=(TextView) findViewById(R.id.lblTemporadas);
        lblTemporadas.setTypeface(SpecialFont.getInstance(this).getTypeFace());
        TextView genero=(TextView) findViewById(R.id.lblGenero);
        genero.setTypeface(SpecialFont.getInstance(this).getTypeFace());
        TextView lblAnio=(TextView) findViewById(R.id.lblAnioEstreno);
        lblAnio.setTypeface(SpecialFont.getInstance(this).getTypeFace());
        TextView lblPais=(TextView) findViewById(R.id.lblPaisOrigen);
        lblPais.setTypeface(SpecialFont.getInstance(this).getTypeFace());
        TextView lblCanales=(TextView) findViewById(R.id.lblCanales);
        lblCanales.setTypeface(SpecialFont.getInstance(this).getTypeFace());
        //Los textos
        TextView nombreTxt=(TextView) findViewById(R.id.txtNombreSe);
        nombreTxt.setTypeface(RobotoFont.getInstance(this).getTypeFace());
        TextView sinopsisTxt=(TextView) findViewById(R.id.txtSinopsisSe);
        sinopsisTxt.setTypeface(RobotoFont.getInstance(this).getTypeFace());
        TextView anioTxt=(TextView) findViewById(R.id.txtAnioEstreno);
        anioTxt.setTypeface(RobotoFont.getInstance(this).getTypeFace());
        TextView paisTxt=(TextView) findViewById(R.id.txtPaisOrigen);
        paisTxt.setTypeface(RobotoFont.getInstance(this).getTypeFace());
        TextView generoTxt=(TextView) findViewById(R.id.txtGeneroSe);
        generoTxt.setTypeface(RobotoFont.getInstance(this).getTypeFace());
    }

    private void crearRecyclerViewTemporadas(){
        DataBaseConnection db=new DataBaseConnection(this.getBaseContext());
        Cursor c=db.getTemporadasDeSerie(idSerie);
        if (c!=null) {
            String[] numerosTemp=new String[c.getCount()];
            String[] numerosCap=new String[c.getCount()];
            int i=0;
            while (c.moveToNext()){
                numerosTemp[i]=c.getString(c.getColumnIndex(DataBaseContract.TemporadaContract.COLUMN_NAME_TEMPORADA_ID));
                numerosCap[i]="#Capítulos: "+c.getString(c.getColumnIndex("cuenta"));
                i++;
            }
            this.crearRecyclerViewTemporadas(numerosTemp, numerosCap);
        }
    }

    private void crearRecyclerViewTemporadas(String[] contenido, String[] cantTemp){
        LinearLayout layoutRV = (LinearLayout) findViewById(R.id.layoutTempRV);
        Float height = getResources().getDimension(R.dimen.size_temporada)*(contenido.length);
        TableRow.LayoutParams params = new TableRow.LayoutParams(200, height.intValue());
        layoutRV.setLayoutParams(params);
        recyclerViewTemp = (RecyclerView) findViewById(R.id.rv_temporadas);
        // Se usa cuando se sabe que cambios en el contenido no cambian el tamaño del layout
        recyclerViewTemp.setHasFixedSize(false);
        // Se usa un layout manager lineal para el recycler view
        layoutManagerTemp = new LinearLayoutManager(this);
        recyclerViewTemp.setLayoutManager(layoutManagerTemp);
        // Se especifica el adaptador
        if (contenido!=null) {
            adapterTemp = new TemporadaAdapter(contenido,cantTemp);
            recyclerViewTemp.setAdapter(adapterTemp);
        }
    }

    private void crearRecyclerViewCanales(){
        DataBaseConnection db=new DataBaseConnection(this.getBaseContext());
        Cursor c=db.consultarCanalesDePrograma(idSerie);
        if (c!=null) {
            String[] canales=new String[c.getCount()];
            int i=0;
            while (c.moveToNext()){
                canales[i]=c.getString(c.getColumnIndex(DataBaseContract.EmiteContract.COLUMN_NAME_CANAL_ID));
                i++;
            }
            this.crearRecyclerViewCanales(canales);
        }
    }

    private void crearRecyclerViewCanales(String[] contenido){
        LinearLayout layoutRV = (LinearLayout) findViewById(R.id.layoutCanalRV);
        Float height = getResources().getDimension(R.dimen.size_canal)*(contenido.length);
        TableRow.LayoutParams params = new TableRow.LayoutParams(200, height.intValue());
        layoutRV.setLayoutParams(params);
        recyclerViewCanal = (RecyclerView) findViewById(R.id.rv_canales);
        // Se usa cuando se sabe que cambios en el contenido no cambian el tamaño del layout
        recyclerViewCanal.setHasFixedSize(false);
        // Se usa un layout manager lineal para el recycler view
        layoutManagerCanal = new LinearLayoutManager(this);
        recyclerViewCanal.setLayoutManager(layoutManagerCanal);
        // Se especifica el adaptador
        if (contenido!=null) {
            adapterCanal = new CanalAdapter(contenido);
            recyclerViewCanal.setAdapter(adapterCanal);
        }
    }


    @Override
    public void onResume(){
        super.onResume();
        if (actualizado) this.recreate();
        actualizado=false;
    }

    private void llenarInfo(String nombreSerie){

        DataBaseConnection db=new DataBaseConnection(this.getBaseContext());
        Cursor c=db.consultarSeriePorNombre(nombreSerie);
        c.moveToNext();
        nombre = c.getString(c.getColumnIndex(ProgramaContract.COLUMN_NAME_PROGRAMA_NOMBRE));
        sinopsis = c.getString(c.getColumnIndex(ProgramaContract.COLUMN_NAME_PROGRAMA_SINOPSIS));
        genero = c.getString(c.getColumnIndex(GeneroContract.COLUMN_NAME_GENERO_NOMBRE));
        anio = c.getInt(c.getColumnIndex(ProgramaContract.COLUMN_NAME_PROGRAMA_ANIO_ESTRENO));
        pais = c.getString(c.getColumnIndex(ProgramaContract.COLUMN_NAME_PROGRAMA_PAIS_ORIGEN));
        idSerie =  c.getInt(c.getColumnIndex(ProgramaContract.COLUMN_NAME_PROGRAMA_ID));
        if (c.getInt(c.getColumnIndex(DataBaseContract.AgendaContract.COLUMN_NAME_USUARIO_ID))==Sesion.getInstance().getIdUsuario()){
            isFavorito=true;
        } else isFavorito=false;
        if (!nombre.equals("")) ((TextView) findViewById(R.id.txtNombreSe)).setText(nombre);
        else ((TextView) findViewById(R.id.txtNombreSe)).setText("Serie sin nombre");
        if (!sinopsis.equals("")) ((TextView) findViewById(R.id.txtSinopsisSe)).setText(sinopsis);
        else ((TextView) findViewById(R.id.txtSinopsisSe)).setText("Serie sin sinopsis");
        if (!genero.equals("")) ((TextView) findViewById(R.id.txtGeneroSe)).setText(genero);
        else ((TextView) findViewById(R.id.txtGeneroSe)).setText("Serie sin genero");
        if (anio!=0) ((TextView) findViewById(R.id.txtAnioEstreno)).setText(Integer.toString(anio));
        else ((TextView) findViewById(R.id.txtAnioEstreno)).setText("Serie sin año registrado");
        if (!pais.equals("")) ((TextView) findViewById(R.id.txtPaisOrigen)).setText(pais);
        else ((TextView) findViewById(R.id.txtPaisOrigen)).setText("Serie sin país registrado");
    }

    public void onClickActualizar(View v){
        Intent i = new Intent(this, ActivityActualizarSerie.class);
        Bundle b = new Bundle();
        b.putString(ProgramaContract.COLUMN_NAME_PROGRAMA_NOMBRE, nombre);
        b.putString(ProgramaContract.COLUMN_NAME_PROGRAMA_SINOPSIS,sinopsis);
        b.putString(GeneroContract.COLUMN_NAME_GENERO_NOMBRE, genero);
        b.putString(ProgramaContract.COLUMN_NAME_PROGRAMA_PAIS_ORIGEN,pais);
        b.putInt(ProgramaContract.COLUMN_NAME_PROGRAMA_ANIO_ESTRENO,anio);
        i.putExtras(b);
        startActivity(i);
    }

    public void onClickRegistrarTemporada(View v){

        Intent i = new Intent(this, ActivityAgregarTemporada.class);
        Bundle b = new Bundle();
        b.putInt(DataBaseContract.TemporadaContract.COLUMN_NAME_PROGRAMA_ID, idSerie);
        b.putString(ProgramaContract.COLUMN_NAME_PROGRAMA_NOMBRE, nombre);
        i.putExtras(b);
        startActivity(i);
    }

    public void onClickAsociarCanal(View v){

        Intent i = new Intent(this, ActivityAsociarCanal.class);
        Bundle b = new Bundle();
        b.putInt(ProgramaContract.COLUMN_NAME_PROGRAMA_ID, idSerie);
        b.putString(ProgramaContract.COLUMN_NAME_PROGRAMA_NOMBRE, nombre);
        i.putExtras(b);
        startActivity(i);
    }

    private void configurarRatingBar(){
        final RatingBar calificacion = (RatingBar) findViewById(R.id.ratingBar);
        DataBaseConnection db=new DataBaseConnection(this.getBaseContext());
        Cursor c=db.consultarCalificacion(Sesion.getInstance().getIdUsuario(),idSerie);
        calificado=false;
        if (c!=null && c.getCount()==1){
            c.moveToNext();
            float rating = c.getFloat(c.getColumnIndex(DataBaseContract.CalificacionContract.COLUMN_NAME_VALOR_CALIFICACION));
            calificacion.setRating(rating);
            calificado=true;
        }
        calificacion.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                DataBaseConnection db = new DataBaseConnection(ratingBar.getContext());
                if (calificado) {
                    db.actualizarCalificacion(Sesion.getInstance().getIdUsuario(), idSerie, calificacion.getRating());
                } else {
                    db.insertarCalificacion(Sesion.getInstance().getIdUsuario(), idSerie, calificacion.getRating());
                    calificado = true;
                }
            }
        });
    }

    protected void hideWhenNoSession(){
        if (!Sesion.getInstance().isActiva()){
            FloatingActionButton b = (FloatingActionButton) findViewById(R.id.fab);
            hide(b);
            Button btn = (Button) findViewById(R.id.btn_AgregarTemporada);
            hide(btn);
            btn = (Button) findViewById(R.id.btn_AsociarCanal);
            hide(btn);
            RatingBar rb = (RatingBar) findViewById(R.id.ratingBar);
            hide(rb);
            TextView txt = (TextView) findViewById(R.id.lblCalificacion);
            hide(txt);
            View v = (View) findViewById(R.id.v1);
            hide(v);
            v = (View) findViewById(R.id.v2);
            hide(v);
        }
    }

    protected void showWhenSession(){
        if (Sesion.getInstance().isActiva()){
            FloatingActionButton b = (FloatingActionButton) findViewById(R.id.fab);
            show(b);
            Button btn = (Button) findViewById(R.id.btn_AgregarTemporada);
            show(btn);
            btn = (Button) findViewById(R.id.btn_AsociarCanal);
            show(btn);
            RatingBar rb = (RatingBar) findViewById(R.id.ratingBar);
            show(rb);
            TextView txt = (TextView) findViewById(R.id.lblCalificacion);
            show(txt);
            View v = (View) findViewById(R.id.v1);
            show(v);
            v = (View) findViewById(R.id.v2);
            show(v);
        }
    }

    public void configurarImageButton(){
        if (Sesion.getInstance().isActiva()) {
            btnImg.setBackgroundColor(getResources().getColor(R.color.colorTable));
            if (this.isFavorito)
                btnImg.setImageResource(R.drawable.ic_remove);
            else
                btnImg.setImageResource(R.drawable.ic_add);
            btnImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataBaseConnection db = new DataBaseConnection(v.getContext());
                    if (isFavorito) {
                        if (db.eliminarFavorito(Sesion.getInstance().getIdUsuario(),idSerie)) {
                            btnImg.setImageResource(R.drawable.ic_add);
                            isFavorito = false;
                            ActivityConsultarProgramasAgenda.actualizado=true;
                            ActivityConsultarPrograma.actualizado=true;
                                /*mCardView.removeAllViews();*/
                        }
                    } else {
                        if (db.insertarFavorito(Sesion.getInstance().getIdUsuario(), idSerie)) {
                            btnImg.setImageResource(R.drawable.ic_remove);
                            isFavorito=true;
                            ActivityConsultarProgramasAgenda.actualizado=true;
                            ActivityConsultarPrograma.actualizado=true;
                        }
                    }
                }
            });
        } else {
            btnImg.setVisibility(View.GONE);
        }
    }

    public int getIdSerie(){
        return idSerie;
    }

    public String getNombreSerie(){
        return nombre;
    }
}
