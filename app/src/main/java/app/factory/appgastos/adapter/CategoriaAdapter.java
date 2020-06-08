package app.factory.appgastos.adapter;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import app.factory.appgastos.R;
import app.factory.appgastos.entidad.Categoria;
import app.factory.appgastos.views.ListCategoriaFragment;


import java.text.DecimalFormat;
import java.util.List;


public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.ViewHolder> {

    private final List<Categoria> lstCategoria;
    private final ListCategoriaFragment mListener;

    public CategoriaAdapter(List<Categoria> items, ListCategoriaFragment listener) {
        lstCategoria = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_categoria, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = lstCategoria.get(position);
        holder.txtIdCategoria.setText( String.valueOf(lstCategoria.get(position).getIdCategoria()) );
        holder.txtCategoria.setText(lstCategoria.get(position).getCategoria());
        holder.txtMontoMovCategoria.setText( decimalToString(lstCategoria.get(position).getMontoMovimientoCategoria())  );

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    //mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    private String decimalToString(double val){
        String strVal = "";
        DecimalFormat decimalFormat = new DecimalFormat("#,###.00");
        strVal = decimalFormat.format(val);
        return strVal;
    }

    @Override
    public int getItemCount() {
        return lstCategoria.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView txtIdCategoria;
        public final TextView txtCategoria;
        public Categoria mItem;
        private TextView txtMontoMovCategoria;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            txtIdCategoria =  view.findViewById(R.id.txtIdCategoria);
            txtCategoria =  view.findViewById(R.id.txtCategoria);
            txtMontoMovCategoria = view.findViewById(R.id.txtMontoMovCategoria);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + txtCategoria.getText() + "'";
        }
    }
}
