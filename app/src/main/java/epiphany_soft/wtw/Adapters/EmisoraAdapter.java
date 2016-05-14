package epiphany_soft.wtw.Adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import epiphany_soft.wtw.Fonts.RobotoFont;
import epiphany_soft.wtw.Negocio.Emisora;
import epiphany_soft.wtw.R;
// parece q ya esta bn.

public class EmisoraAdapter extends RecyclerView.Adapter<EmisoraAdapter.ViewHolder> {
    private Emisora[] misEmisoras;
    /*TODO revisar si se puede hacer de una mejor manera*/
    private ArrayList<EmisoraAdapter.ViewHolder> misViewHolder;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public CardView mCardView;
        public TextView mTextView;
        public EditText numCanalEdit;
        public CheckBox ck;
        public int idEmisora;
        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            mCardView = (CardView)v.findViewById(R.id.cv);
            mTextView = (TextView)v.findViewById(R.id.textCard);
            numCanalEdit = (EditText)v.findViewById(R.id.textCardNumeroCanal);
            ck = (CheckBox) v.findViewById(R.id.textCardCK);
            ck.setOnClickListener(this);
            mTextView.setTypeface(RobotoFont.getInstance(v.getContext()).getTypeFace());
            numCanalEdit.setTypeface(RobotoFont.getInstance(v.getContext()).getTypeFace());
        }


        @Override
        public void onClick(View v) {
            if (ck.isChecked()) {
                numCanalEdit.setVisibility(View.VISIBLE);
            } else {
                numCanalEdit.setVisibility(View.GONE);
            }
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public EmisoraAdapter(Emisora[] myDataset) {
        misEmisoras = myDataset;
        misViewHolder = new ArrayList<ViewHolder>();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public EmisoraAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tv_emisora, parent, false);
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
        holder.idEmisora=misEmisoras[position].getId();
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return misEmisoras.length;
    }

    public ArrayList<EmisoraAdapter.ViewHolder> getMisViewHolder(){
        return misViewHolder;
    }

}