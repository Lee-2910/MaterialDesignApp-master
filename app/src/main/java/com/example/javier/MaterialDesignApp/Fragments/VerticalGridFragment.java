package com.example.javier.MaterialDesignApp.Fragments;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.example.javier.MaterialDesignApp.InsetDecoration;
import com.example.javier.MaterialDesignApp.Model.VideosModel;
import com.example.javier.MaterialDesignApp.RecyclerView.RecyclerViewDemo.SimpleAdapterGrid;

import java.util.ArrayList;

public class VerticalGridFragment extends RecyclerFragmentTemp {

    private ArrayList<VideosModel> _Tap;
    private Context context;

    public VerticalGridFragment(Context mcontext,ArrayList<VideosModel> tap) {
        // Empty constructor required for DialogFragment

        this.context = mcontext;
        this._Tap=tap;

    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new GridLayoutManager(getActivity(), 5, GridLayoutManager.VERTICAL, false);
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new InsetDecoration(getActivity());
    }

    @Override
    protected int getDefaultItemCount() {
        return 0;
    }

    @Override
    protected SimpleAdapterGrid getAdapter() {

        return new SimpleAdapterGrid(getActivity(), _Tap);
    }
}
