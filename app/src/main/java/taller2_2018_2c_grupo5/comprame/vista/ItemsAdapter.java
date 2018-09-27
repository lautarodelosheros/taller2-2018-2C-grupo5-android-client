package taller2_2018_2c_grupo5.comprame.vista;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import taller2_2018_2c_grupo5.comprame.R;
import taller2_2018_2c_grupo5.comprame.dominio.Item;

public class ItemsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROGRESS_BAR = 0;

    private List<Item> items;
    private int posicionProgressBar;
    private boolean hayProgressBar = false;

    public ItemsAdapter(List<Item> items) {
        this.items = new ArrayList<>(items);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        if (viewType == VIEW_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.searched_item_layout, parent, false);
            viewHolder = new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar, parent, false);
            viewHolder = new ProgressViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ItemViewHolder) {

            Item item = this.items.get(position);
            ((ItemViewHolder) holder).bindTo(item);

        } else {

            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);

        }
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public void addItems(List<Item> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void clearItems() {
        this.items.clear();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position) != null ? VIEW_ITEM : VIEW_PROGRESS_BAR;
    }

    public void agregarProgressBar() {
        this.items.add(null);
        hayProgressBar = true;
        posicionProgressBar = this.items.size() - 1;
    }

    public void quitarProgressBar() {
        if (hayProgressBar) {
            this.items.remove(posicionProgressBar);
            hayProgressBar = false;
        }
    }
}

