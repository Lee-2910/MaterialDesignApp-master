package com.example.javier.MaterialDesignApp.RecyclerView.RecyclerViewDemo;

import android.content.Context;
import android.view.View;

import com.example.javier.MaterialDesignApp.Model.VideosModel;
import com.example.javier.MaterialDesignApp.R;

import java.util.ArrayList;


public class SimpleStaggeredAdapter extends SimpleAdapter {


    public SimpleStaggeredAdapter(Context mcontext, ArrayList<VideosModel> tap) {
        super(mcontext, tap);
    }

    @Override
    public void onBindViewHolder(VerticalItemHolder itemHolder, int position) {
        super.onBindViewHolder(itemHolder, position);

        final View itemView = itemHolder.itemView;
        if (position % 4 == 0) {
            int height = itemView.getContext().getResources()
                    .getDimensionPixelSize(R.dimen.card_staggered_height);
            itemView.setMinimumHeight(height);
        } else {
            itemView.setMinimumHeight(0);
        }
    }
}
