package com.example.javier.MaterialDesignApp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.javier.MaterialDesignApp.DividerDecoration;
import com.example.javier.MaterialDesignApp.Model.VideosModel;
import com.example.javier.MaterialDesignApp.RecyclerView.RecyclerViewDemo.SimpleAdapter;

import java.util.ArrayList;


public class VerticalFragment extends RecyclerFragment {
  private ArrayList<VideosModel> _Tap;
    private Context context;
   /* public static VerticalFragment newInstance() {
        VerticalFragment fragment = new VerticalFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }*/
    public VerticalFragment(Context mcontext,ArrayList<VideosModel> tap) {
        // Empty constructor required for DialogFragment

        this.context = mcontext;
        this._Tap=tap;

    }
    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        //We must draw dividers ourselves if we want them in a list
        return new DividerDecoration(getActivity());
    }

    @Override
    protected int getDefaultItemCount() {
        return _Tap.size();
    }

    @Override
    protected SimpleAdapter getAdapter() {
        return new SimpleAdapter(getActivity(),_Tap);
    }
}
