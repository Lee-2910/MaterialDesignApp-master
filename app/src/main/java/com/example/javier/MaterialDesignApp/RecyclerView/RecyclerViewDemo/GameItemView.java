package com.example.javier.MaterialDesignApp.RecyclerView.RecyclerViewDemo;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.javier.MaterialDesignApp.R;


public class GameItemView extends GridLayout {

    private TextView mHomeScore;
    private ImageView imageview;

    public GameItemView(Context context) {
        super(context);
    }

    public GameItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GameItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mHomeScore = (TextView) findViewById(R.id.text_score_home);
   //    imageview = (ImageView) findViewById(R.id.img_video);
    }

    @Override
    public String toString() {
        return null;
    }
}
