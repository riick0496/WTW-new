package epiphany_soft.wtw.Adapters;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import epiphany_soft.wtw.Activities.Series.ActivityDetalleSerie;
import epiphany_soft.wtw.Activities.Series.ActivityDetalleTemporada;
import epiphany_soft.wtw.DataBase.DataBaseContract;
import epiphany_soft.wtw.Fonts.RobotoFont;
import epiphany_soft.wtw.R;
// parece q ya esta bn.

public class TemporadaAdapter extends RecyclerView.Adapter<TemporadaAdapter.ViewHolder> {
    private String[] numTemporadas;
    private String[] cantCapitulos;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public CardView mCardView;
        public TextView mTextView, cantCapTextView;
        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            mCardView = (CardView)v.findViewById(R.id.cv);
            mTextView = (TextView)v.findViewById(R.id.textCard);
            cantCapTextView = (TextView)v.findViewById(R.id.textCardCantidadCapitulo);
            mTextView.setTypeface(RobotoFont.getInstance(v.getContext()).getTypeFace());
            cantCapTextView.setTypeface(RobotoFont.getInstance(v.getContext()).getTypeFace());
        }


        @Override
        public void onClick(View v) {
            if (mTextView.getText()!="") {
                Intent i = new Intent(v.getContext(), ActivityDetalleTemporada.class);
                int idSerie=((ActivityDetalleSerie)v.getContext()).getIdSerie();
                String nombreSerie = ((ActivityDetalleSerie)v.getContext()).getNombreSerie();
                //Se manda el nombre del programa para saber que información debe mostrarse
                Bundle b = new Bundle();
                b.putInt(DataBaseContract.TemporadaContract.COLUMN_NAME_TEMPORADA_ID, Integer.parseInt(mTextView.getText().toString()));
                b.putInt(DataBaseContract.TemporadaContract.COLUMN_NAME_PROGRAMA_ID,idSerie);
                b.putString(DataBaseContract.ProgramaContract.COLUMN_NAME_PROGRAMA_NOMBRE,nombreSerie);
                i.putExtras(b);
                v.getContext().startActivity(i);
            }
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public TemporadaAdapter(String[] myDataset, String[] cantCapitulos) {
        numTemporadas = myDataset;
        this.cantCapitulos = cantCapitulos;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TemporadaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tv_temporada, parent, false);
        v.setClickable(true);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(numTemporadas[position]);
        holder.cantCapTextView.setText(cantCapitulos[position]);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return numTemporadas.length;
    }

}