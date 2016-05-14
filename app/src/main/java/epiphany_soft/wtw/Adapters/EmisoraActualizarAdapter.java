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
import epiphany_soft.wtw.Negocio.Emite;
import epiphany_soft.wtw.R;
// parece q ya esta bn.

public class EmisoraActualizarAdapter extends RecyclerView.Adapter<EmisoraActualizarAdapter.ViewHolder> {
    private Emite[] misEmisiones;

    private ArrayList<ViewHolder> misViewHolder;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public CardView mCardView;
        public TextView mTextView, nombre;
        public EditText numCanalEdit;
        public CheckBox ck;
        public int idEmisora;
        public int numero_canal;
        public String nombre_canal;
        public String nombre_emisora;
        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            mCardView = (CardView)v.findViewById(R.id.cv);
            nombre= (TextView)v.findViewById(R.id.textCard);
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
    public EmisoraActualizarAdapter(Emite[] myDataset) {
        misEmisiones = myDataset;
        misViewHolder = new ArrayList<ViewHolder>();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public EmisoraActualizarAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tv_emisora, parent, false);
        ViewHolder vh = new ViewHolder(v);
        misViewHolder.add(vh);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    // se agrega lo de actualizar
    //aqui creo q toca hacer cambios para el checkbos,  toca hacer  un array en el activity de actuaizar q me tome todos los datos
    // y tambien  hay q hacer  en negocio una clase para recuperar los datos...  y aqui hay un ejemplo de como mandarle los datos  del
    //checkbox a la hora de actualizar.
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //holder.mTextView.setText(misEmisiones[position].toString());
        holder.nombre.setText(misEmisiones[position].getNombre_Emisora());
        holder.numCanalEdit.setText(Integer.toString(misEmisiones[position].getNumero_canal()));
        holder.idEmisora = misEmisiones[position].getIdEmisora();
        holder.nombre_canal = misEmisiones[position].getNombre_canal();
        if (misEmisiones[position].getNombre_canal()!=null){
            holder.ck.setChecked(true);
            holder.numCanalEdit.setVisibility(View.VISIBLE);
         } else {
            holder.ck.setChecked(false);
            holder.numCanalEdit.setVisibility(View.GONE);
            holder.numCanalEdit.setText("");
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return misEmisiones.length;
    }

    public ArrayList<ViewHolder> getMisViewHolder(){
        return misViewHolder;
    }

}