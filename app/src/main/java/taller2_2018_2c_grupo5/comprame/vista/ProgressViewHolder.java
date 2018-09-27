package taller2_2018_2c_grupo5.comprame.vista;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import taller2_2018_2c_grupo5.comprame.R;

public class ProgressViewHolder extends RecyclerView.ViewHolder {
    public ProgressBar progressBar;

    public ProgressViewHolder(View view) {
        super(view);
        progressBar = view.findViewById(R.id.progressBar);
    }
}