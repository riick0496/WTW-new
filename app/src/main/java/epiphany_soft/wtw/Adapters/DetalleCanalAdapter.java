package epiphany_soft.wtw.Adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import epiphany_soft.wtw.Fonts.RobotoFont;
import epiphany_soft.wtw.Negocio.Emisora;
import epiphany_soft.wtw.R;


public class DetalleCanalAdapter extends RecyclerView.Adapter<DetalleCanalAdapter.ViewHolder> {
    private Emisora[] misEmisoras;

    private ArrayList<ViewHolder> misViewHolder;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this ase
        public CardView mCardView;
        public TextView mTextView;
        public TextView numCanalEdit;

       public int idEmisora;

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            mCardView = (CardView)v.findViewById(R.id.cv);
            mTextView = (TextView)v.findViewById(R.id.textCard);
            numCanalEdit = (TextView)v.findViewById(R.id.textCardNumeroCanal);
            mTextView.setTypeface(RobotoFont.getInstance(v.getContext()).getTypeFace());
            numCanalEdit.setTypeface(RobotoFont.getInstance(v.getContext()).getTypeFace());
            numCanalEdit.setVisibility(View.VISIBLE);
        }


        @Override
        public void onClick(View v) {

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public DetalleCanalAdapter(Emisora[] myDataset) {
        misEmisoras = myDataset;
        misViewHolder = new ArrayList<ViewHolder>();


    }

    // Create new views (invoked by the layout manager)
    @Override
    public DetalleCanalAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tv_detalle_canal, parent, false);
        ViewHolder vh = new ViewHolder(v);
        misViewHolder.add(vh);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(misEmisoras[position].toString());
        if (misEmisoras[position].getId()!=0)
            holder.numCanalEdit.setText(Integer.toString(misEmisoras[position].getId()));
        else
            holder.numCanalEdit.setText("Desconocido");
        holder.idEmisora=misEmisoras[position].getId();


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return misEmisoras.length;
    }

    public ArrayList<ViewHolder> getMisViewHolder(){
        return misViewHolder;
    }

}