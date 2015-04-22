package com.example.javier.MaterialDesignApp.RecyclerView.RecyclerViewAdapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.javier.MaterialDesignApp.Model.AFilmModel;
import com.example.javier.MaterialDesignApp.Model.ListVideoModel;
import com.example.javier.MaterialDesignApp.Model.VideosModel;
import com.example.javier.MaterialDesignApp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListVideosAdapter extends RecyclerView.Adapter<ListVideosAdapter.ViewHolder> {

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
        }
    };
    Context context;
    private ArrayList<VideosModel> FilmAdapter;


    // Adapter's Constructor
    public ListVideosAdapter(Context context, ArrayList<VideosModel> filmadapter) {
       this.FilmAdapter = filmadapter;
        this.context = context;
       // this.film=video;
    }

    // Create new views. This is invoked by the layout manager.
    @Override
    public ListVideosAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new view by inflating the row item xml.
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
	
        v.setOnClickListener(onClickListener);
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        // Set strings to the views
        final TextView textViewTitle = (TextView) holder.view.findViewById(R.id.textViewItemTitle);
        //final TextView textViewContent = (TextView) holder.view.findViewById(R.id.textViewItemContent);
      //  final ImageView imageViewImage = (ImageView) holder.view.findViewById(R.id.imageViewImage);
        textViewTitle.setText(FilmAdapter.get(position).getLable());
     //   textViewContent.setText(FilmAdapter.get(position).getLink());
       /* Picasso.with(context).load(film.getImages())
                .placeholder(holder.view.getResources()
                        .getDrawable(R.drawable.ic_contact_icon)).into(imageViewImage);*/
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return FilmAdapter.size();
    }


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View view;

        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }
}
