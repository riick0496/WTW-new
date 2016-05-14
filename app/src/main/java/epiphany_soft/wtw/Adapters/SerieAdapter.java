package epiphany_soft.wtw.Adapters;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import epiphany_soft.wtw.Activities.Series.ActivityDetalleSerie;
import epiphany_soft.wtw.DataBase.DataBaseConnection;
import epiphany_soft.wtw.DataBase.DataBaseContract;
import epiphany_soft.wtw.Fonts.RobotoFont;
import epiphany_soft.wtw.Negocio.Programa;
import epiphany_soft.wtw.Negocio.Sesion;
import epiphany_soft.wtw.R;
// parece q ya esta bn.

public class SerieAdapter extends RecyclerView.Adapter<SerieAdapter.ViewHolder> {
    private Programa[] mDataset;
    private String parent;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public CardView mCardView;
        public TextView mTextView;
        public ImageButton btnImg;
        public Programa miPrograma;
        public String parent;
        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            mCardView = (CardView)v.findViewById(R.id.cv);
            mTextView = (TextView)v.findViewById(R.id.textCard);
            mTextView.setTypeface(RobotoFont.getInstance(v.getContext()).getTypeFace());
            btnImg = (ImageButton)v.findViewById(R.id.btnImg);
        }

        public void configurarImageButton(){
            if (Sesion.getInstance().isActiva()) {
                btnImg.setBackgroundColor(mCardView.getSolidColor());
                if (miPrograma.isFavorito())
                    btnImg.setImageResource(R.drawable.ic_remove);
                else
                    btnImg.setImageResource(R.drawable.ic_add);
                btnImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DataBaseConnection db = new DataBaseConnection(v.getContext());
                        if (miPrograma.isFavorito()) {
                            if (db.eliminarFavorito(Sesion.getInstance().getIdUsuario(),miPrograma.getIdPrograma())) {
                                btnImg.setImageResource(R.drawable.ic_add);
                                miPrograma.setFavorito(false);
                                if (parent.equals("Agenda")){
                                    mCardView.removeAllViews();
                                }
                            }
                        } else {
                            if (db.insertarFavorito(Sesion.getInstance().getIdUsuario(), miPrograma.getIdPrograma())) {
                                btnImg.setImageResource(R.drawable.ic_remove);
                                miPrograma.setFavorito(true);
                            }
                        }
                    }
                });
            } else {
                btnImg.setVisibility(View.GONE);
            }
        }


        @Override
        public void onClick(View v) {
            if (mTextView.getText()!="") {
                Intent i = new Intent(v.getContext(), ActivityDetalleSerie.class);
                //Se manda el nombre del programa para saber que información debe mostrarse
                Bundle b = new Bundle();
                b.putString(DataBaseContract.ProgramaContract.COLUMN_NAME_PROGRAMA_NOMBRE, mTextView.getText().toString());
                i.putExtras(b);
                v.getContext().startActivity(i);
            }
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public SerieAdapter(Programa[] myDataset) {
        mDataset = myDataset;
        parent="";
    }

    public void setParent(String parent){
        this.parent=parent;
    }
    // Create new views (invoked by the layout manager)
    @Override
    public SerieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tv_pelicula, parent, false);
        v.setClickable(true);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(mDataset[position].getNombre());
        holder.miPrograma=mDataset[position];
        holder.parent=this.parent;
        holder.configurarImageButton();

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }

}