package epiphany_soft.wtw.Adapters;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

import epiphany_soft.wtw.DataBase.DataBaseConnection;
import epiphany_soft.wtw.DataBase.DataBaseContract;
import epiphany_soft.wtw.Fonts.RobotoFont;
import epiphany_soft.wtw.Fonts.SpecialFont;
import epiphany_soft.wtw.Negocio.Dia;
import epiphany_soft.wtw.Negocio.Horario;
import epiphany_soft.wtw.R;
// parece q ya esta bn.

public class HorarioAdapter extends RecyclerView.Adapter<HorarioAdapter.ViewHolder> {
    private Horario[] mDataset;
    private int idPrograma;
    private Context context;
    private Dia[] dias;

    private ArrayList<HorarioAdapter.ViewHolder> misViewHolder;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            Serializable, TimePickerDialog.OnTimeSetListener{
        // each data item is just a string in this case
        RecyclerView mRecyclerView;
       public  RecyclerView.Adapter mAdapter;//adaptador de la lista de los dias
        RecyclerView.LayoutManager mLayoutManager;
        private LinearLayout layoutRV;

        public CardView mCardView;
        public TextView mTextView,horaTxt;
        public LinearLayout mLayout;
        public CheckBox ck;
        public Horario mHorario;
        public int idPrograma;
        public boolean isChecked;


        public Context c;

        public ViewHolder(View v) {
            super(v);
            mCardView = (CardView)v.findViewById(R.id.cv);
            mTextView = (TextView)v.findViewById(R.id.textCard);
            mLayout = (LinearLayout)v.findViewById(R.id.layoutExterno);
            horaTxt=(EditText) v.findViewById(R.id.txtHora);
            horaTxt.setOnClickListener(this);
            ck = (CheckBox)v.findViewById(R.id.textCardCK);
            ck.setOnClickListener(this);
            ((Button)v.findViewById(R.id.btnCambiarHora)).setOnClickListener(this);
            setSpecialFonts(v);
            mTextView.setTypeface(RobotoFont.getInstance(v.getContext()).getTypeFace());

            mRecyclerView = (RecyclerView) v.findViewById(R.id.rv_horario);
            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(v.getContext(),LinearLayoutManager.HORIZONTAL,false);
            mRecyclerView.setLayoutManager(mLayoutManager);
            //mCardView.setCardBackgroundColor(v.getResources().getColor(R.color.colorTable2));
            layoutRV = (LinearLayout) v.findViewById(R.id.layoutHorarioRV);
        }

        @Override
        public void onClick(View v) {

            if (v.getId()==R.id.textCardCK) {
                if (isChecked) {
                    mLayout.setVisibility(View.GONE);
                    ck.setChecked(false);
                } else {
                    mLayout.setVisibility(View.VISIBLE);
                    ck.setChecked(true);
                }
                isChecked = ck.isChecked();
            }
            if (v.getId() == R.id.btnCambiarHora) {
                showTimePickerDialog(v);
            }


/*
            if (v.getId()==R.id.textCardCK) {
               // boolean success;
               // DataBaseConnection db = new DataBaseConnection(v.getContext());
                if (mHorario.getId() != 0) {
                   // success = db.eliminarHorario(mHorario.getId());
                   // if (success) {
                        ck.setChecked(false);
                       mLayout.setVisibility(View.GONE);
                        //mHorario.setId(0);
                       // mHorario.setIdPrograma(0);
                      //  ActivityDetalleSerie.actualizado = true;
                       // ActivityDetallePelicula.actualizado = true;
                   // }
                }
            else {
                  //  mHorario.setIdPrograma(idPrograma);
                    //success = db.insertarHorario(mHorario);
                   // if (success) {
                      //  ck.setChecked(true);
                      //  mHorario.setId(db.getHorarioId(mHorario.getIdPrograma(), mHorario.getNombreCanal()));
                        mLayout.setVisibility(View.VISIBLE);
                       // ActivityDetalleSerie.actualizado = true;
                       // ActivityDetallePelicula.actualizado = true;
                    }
                }
           //}
        else if (v.getId()==R.id.btnCambiarHora){
                showTimePickerDialog(v);
            }

            */
        }

        private void crearRecyclerViewDias() {
            Dia dias[]= new Dia[7];
            String contenido[] = new String[]{"D", "L", "Ma", "Mi", "J", "V", "S"}; // aqui...

            if (mHorario.getId()==0) { //Ya se sabe que isChecked = false, no se requiere la consulta a la BD

                for (int i=0;i<7;i++){
                    dias[i]=new Dia(i+1);
                    dias[i].setNombre(contenido[i]);
                    dias[i].setIsChecked(false);

                }
            } else{
             DataBaseConnection db = new DataBaseConnection(this.c);
                Cursor c=db.consultarHorarioDia(mHorario.getId());
                if (c!=null) {
                    for (int i=0;i<7;i++){
                        c.moveToNext();
                       int aux=c.getInt(c.getColumnIndex(DataBaseContract.DiaHorarioContract.COLUMN_NAME_RELACION_ID));
                       dias[i]=new Dia(i+1);
                        dias[i].setNombre(contenido[i]);
                    if (aux==0)  dias[i].setIsChecked(false);
                    else dias[i].setIsChecked(true);
                    }
                }

            }

            Float height = this.c.getResources().getDimension(R.dimen.size_dia)*(contenido.length);
            TableRow.LayoutParams params = new TableRow.LayoutParams(1200, height.intValue());
            layoutRV.setLayoutParams(params);

            if (contenido!=null) {
                mAdapter = new DiaAdapter(dias,c); // se le pasa el dia
                mRecyclerView.setAdapter(mAdapter);
            }
        }

        private void setSpecialFonts(View v){
            TextView horaLabel=(TextView) v.findViewById(R.id.lblHora);
            horaLabel.setTypeface(SpecialFont.getInstance(v.getContext()).getTypeFace());
            TextView diasLabel=(TextView) v.findViewById(R.id.lblDias);
            diasLabel.setTypeface(SpecialFont.getInstance(v.getContext()).getTypeFace());
            //Los textos
            horaTxt.setTypeface(RobotoFont.getInstance(v.getContext()).getTypeFace());
        }

        public void showTimePickerDialog(View v) {
            Bundle b = new Bundle();
            b.putSerializable("horario",this);
            DialogFragment newFragment = new TimePickerFragment();
            newFragment.setArguments(b);
            newFragment.show(((FragmentActivity)c).getSupportFragmentManager(), "timePicker");
        }

        @Override
        //Este se activa cuando se fija la hora en el reloj
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String text =Integer.toString(hourOfDay)+":"+Integer.toString(minute);
           // esto le agregamos para la hora
            horaTxt.setText(text);
            mHorario.setHora(text);
        }

        public static class TimePickerFragment extends DialogFragment {
            private HorarioAdapter.ViewHolder ev;

            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                // Use the current time as the default values for the picker
                Bundle b = getArguments();
                ev = (HorarioAdapter.ViewHolder)b.getSerializable("horario");
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                // Create a new instance of TimePickerDialog and return it
                return new TimePickerDialog(getActivity(), ev, hour, minute,
                        DateFormat.is24HourFormat(getActivity()));
            }
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public HorarioAdapter(Context c,Horario[] myDataset, int idPrograma) {
        mDataset = myDataset;
        this.idPrograma = idPrograma;
        this.context = c;

        //aqui modfique
        misViewHolder = new ArrayList<ViewHolder>();


    }

    // Create new views (invoked by the layout manager)
    @Override
    public HorarioAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tv_horario, parent, false);
        ViewHolder vh = new ViewHolder(v);

        //aqui modifique
        misViewHolder.add(vh);
        return vh;

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(mDataset[position].toString());
        holder.mHorario = mDataset[position];
        holder.idPrograma = idPrograma;
        holder.ck.setChecked(false);
        holder.c=context;
        if (mDataset[position].getId()!=0){
            holder.ck.setChecked(true);
            if (holder.mHorario.getHora()!=null)
                holder.horaTxt.setText(holder.mHorario.getHora().toString());
            else
                holder.horaTxt.setText("");
            holder.mLayout.setVisibility(View.VISIBLE);
        }
        holder.isChecked=holder.ck.isChecked();
        holder.crearRecyclerViewDias();
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    public ArrayList<HorarioAdapter.ViewHolder> getMisViewHolder(){
        return misViewHolder;
    }

}