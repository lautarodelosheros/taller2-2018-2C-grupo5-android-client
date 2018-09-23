package taller2_2018_2c_grupo5.comprame.vista;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Locale;

import taller2_2018_2c_grupo5.comprame.R;
import taller2_2018_2c_grupo5.comprame.dominio.Item;

public class ItemViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener  {
    private ImageView imageViewFoto;
    private TextView textViewNombre;
    private TextView textViewPrecio;

    public ItemViewHolder(final View itemView) {
        super(itemView);

        imageViewFoto = itemView.findViewById(R.id.foto_item);
        textViewNombre = itemView.findViewById(R.id.nombre_item);
        textViewPrecio = itemView.findViewById(R.id.precio_item);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Ir a Fragment de publicacion de Item
            }
        });

    }

    public void bindTo(Item item) {

        Glide
            .with(itemView.getContext())
            .load(item.getFoto(0))
                .placeholder(R.drawable.item_placeholder)
                .error(R.drawable.item_error)
            .into(imageViewFoto);

        textViewNombre.setText(item.getNombre());
        textViewPrecio.setText(String.format(Locale.getDefault(), "$%.2f", item.getPrecioUnitario()));
    }

    @Override
    public void onClick(View view) {

    }

}
