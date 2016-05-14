package epiphany_soft.wtw.Adapters;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import epiphany_soft.wtw.Activities.ActivityDetalleCanal;
import epiphany_soft.wtw.Fonts.RobotoFont;
import epiphany_soft.wtw.Negocio.Canal;
import epiphany_soft.wtw.R;
// parece q ya esta bn.

public class CanalAdapter extends RecyclerView.Adapter<CanalAdapter.ViewHolder> {
    private String[] mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public CardView mCardView;
        public TextView mTextView;
        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            mCardView = (CardView)v.findViewById(R.id.cv);
            mTextView = (TextView)v.findViewById(R.id.textCard);
            mTextView.setTypeface(RobotoFont.getInstance(v.getContext()).getTypeFace());
        }

        @Override
        public void onClick(View v) {
            if (mTextView.getText()!="") {
                Intent i = new Intent(v.getContext(), ActivityDetalleCanal.class);
                //Se manda el nombre del programa para saber que información debe mostrarse
                Canal.getInstance().setNombreCanal(this.mTextView.getText().toString());
                v.getContext().startActivity(i);
            }
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CanalAdapter(String[] myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CanalAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tv_canal, parent, false);
        v.setClickable(true);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(mDataset[position]);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }

}