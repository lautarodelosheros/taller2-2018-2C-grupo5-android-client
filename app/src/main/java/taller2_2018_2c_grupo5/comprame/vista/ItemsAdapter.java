package taller2_2018_2c_grupo5.comprame.vista;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import taller2_2018_2c_grupo5.comprame.R;
import taller2_2018_2c_grupo5.comprame.dominio.Item;

public class ItemsAdapter extends RecyclerView.Adapter<ItemViewHolder> {

    private ArrayList<Item> items;

    public ItemsAdapter(ArrayList<Item> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.searched_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = this.items.get(position);
        holder.bindTo(item);
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }
}

