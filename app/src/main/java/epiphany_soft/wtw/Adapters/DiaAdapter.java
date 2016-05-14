package epiphany_soft.wtw.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import epiphany_soft.wtw.Fonts.RobotoFont;
import epiphany_soft.wtw.Negocio.Dia;
import epiphany_soft.wtw.R;
// parece q ya esta bn.

public class DiaAdapter extends RecyclerView.Adapter<DiaAdapter.ViewHolder> {
    private Dia[] dias;
    private String NombreDia;
    private Context context;


    private ArrayList<DiaAdapter.ViewHolder> misViewHolder;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public CardView mCardView;
        public TextView mTextView;
        public boolean isChecked=false;

        public Dia mDia;

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            mCardView = (CardView)v.findViewById(R.id.cv);
            mTextView = (TextView)v.findViewById(R.id.textCard);
            mCardView.setCardBackgroundColor(v.getResources().getColor(R.color.colorButtonUnselected));
            mTextView.setTypeface(RobotoFont.getInstance(v.getContext()).getTypeFace());
        }

        public void configurarVista(Context c){
            if (mDia.isChecked()){
                isChecked=true;
                mCardView.setBackgroundColor(c.getResources().getColor(R.color.colorButtonSelected));
            }
            else {
                isChecked=false;
                mCardView.setBackgroundColor(c.getResources().getColor(R.color.colorButtonUnselected));
            }

        }


        @Override
        public void onClick(View v) {

            if (isChecked){
                isChecked=false;

                mCardView.setBackgroundColor(v.getResources().getColor(R.color.colorButtonUnselected));
            } else {
                isChecked=true;

                mCardView.setBackgroundColor(v.getResources().getColor(R.color.colorButtonSelected));
            }
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
       // le quite el parametro
    public DiaAdapter(Dia[] misdias,Context c) {
        dias = misdias;
        misViewHolder = new ArrayList<ViewHolder>();
        context = c;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public DiaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tv_dia, parent, false);
        v.setClickable(true);
        ViewHolder vh = new ViewHolder(v);
        misViewHolder.add(vh);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.mTextView.setText(dias[position].toString());
        holder.isChecked=dias[position].isChecked();
        holder.mDia=dias[position];
        holder.configurarVista(context);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dias.length;
    }

    public ArrayList<DiaAdapter.ViewHolder> getMisViewHolder(){
        return misViewHolder;
    }

}