package taller2_2018_2c_grupo5.comprame.actividades.comunes;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import taller2_2018_2c_grupo5.comprame.R;


public abstract class RecyclerFragment extends Fragment implements View.OnClickListener {

    private RecyclerView mList;
    private RecyclerView.Adapter mAdapter;

    /** Required Overrides for Sample Fragments */

    protected abstract RecyclerView.LayoutManager getLayoutManager();

    protected abstract void configureAdapter();

    public RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }

    public void setConfiguredAdapter(RecyclerView.Adapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recycler, container, false);

        mList = rootView.findViewById(R.id.section_list);
        mList.setLayoutManager(getLayoutManager());
        mList.addItemDecoration(getItemDecoration());

        mList.getItemAnimator().setAddDuration(1000);
        mList.getItemAnimator().setChangeDuration(1000);
        mList.getItemAnimator().setMoveDuration(1000);
        mList.getItemAnimator().setRemoveDuration(1000);

        //Esto agrega la linea separadora entre items
        DividerItemDecoration divisor = new DividerItemDecoration(mList.getContext(), DividerItemDecoration.VERTICAL);
        //Si se quisiera usar una imagen como separador
        mList.addItemDecoration(divisor);

        configureAdapter();
        mList.setAdapter(getAdapter());

        return rootView;
    }

    @Override
    public void onClick(View v) {

    }

    public RecyclerView.ItemDecoration getItemDecoration() {
        return new InsertDecoration(getActivity());
    }

}
