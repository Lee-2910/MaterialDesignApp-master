package com.example.javier.MaterialDesignApp.RecyclerView.RecyclerViewDemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.javier.MaterialDesignApp.Model.VideosModel;
import com.example.javier.MaterialDesignApp.R;
import com.example.javier.MaterialDesignApp.Utils.PicassoTransform.CircleTransform;
import com.example.javier.MaterialDesignApp.Utils.PicassoTransform.CircleTransformWhite;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class SimpleAdapterGrid extends RecyclerView.Adapter<SimpleAdapterGrid.VerticalItemHolder> {

   // private ArrayList<GameItem> mItems;
   private ImageView mImageView;

   private Context context;
    private AdapterView.OnItemClickListener mOnItemClickListener;
    private ArrayList<VideosModel> _Tap;
    public SimpleAdapterGrid(Context mcontext, ArrayList<VideosModel> tap) {

        this.context=mcontext;
        this._Tap=tap;
    }

    @Override
    public VerticalItemHolder onCreateViewHolder(ViewGroup container, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View root = inflater.inflate(R.layout.view_match_grid_item, container, false);

        return new VerticalItemHolder(root, this);
    }

    @Override
    public void onBindViewHolder(VerticalItemHolder itemHolder, int position) {

        mImageView = (ImageView) itemHolder.itemView.findViewById(R.id.img_video);
        itemHolder.setHomeScore(String.valueOf(_Tap.get(position).getLable()));
        Picasso.with(context).load(_Tap.get(position).getImages()).fit()
                .placeholder(itemHolder.itemView.getResources()
                        .getDrawable(R.drawable.ic_contact_icon)).into(mImageView);

    }

    @Override
    public int getItemCount() {
        return _Tap.size();
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    private void onItemHolderClick(VerticalItemHolder itemHolder) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(null, itemHolder.itemView,itemHolder.getAdapterPosition(), itemHolder.getItemId());
        }
    }

    public void setItemCount(int count) {
        //  mItems.clear();
        //  mItems.addAll(generateDummyData(count));

        notifyDataSetChanged();
    }


    public static class VerticalItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mHomeScore;
        private SimpleAdapterGrid mAdapter;

        public VerticalItemHolder(View itemView, SimpleAdapterGrid adapter) {
            super(itemView);
            itemView.setOnClickListener(this);

            mAdapter = adapter;
            mHomeScore = (TextView) itemView.findViewById(R.id.text_score_home);


        }

        @Override
        public void onClick(View v) {
            mAdapter.onItemHolderClick(this);
        }

               public void setHomeScore(CharSequence homeScore) {
            mHomeScore.setText(homeScore);
        }


    }

}
