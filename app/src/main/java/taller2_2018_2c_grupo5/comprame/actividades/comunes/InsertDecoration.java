package taller2_2018_2c_grupo5.comprame.actividades.comunes;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import taller2_2018_2c_grupo5.comprame.R;

/**
 * ItemDecoration implementation that applies an inset margin
 * around each child of the RecyclerView. The inset value is controlled
 * by a dimension resource.
 */
public class InsertDecoration extends RecyclerView.ItemDecoration {

    private int mInsets;

    public InsertDecoration(Context context) {
        mInsets = context.getResources().getDimensionPixelSize(R.dimen.card_size);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //We can supply forced insets for each item view here in the Rect
        outRect.set(mInsets, mInsets, mInsets, mInsets);
    }
}
