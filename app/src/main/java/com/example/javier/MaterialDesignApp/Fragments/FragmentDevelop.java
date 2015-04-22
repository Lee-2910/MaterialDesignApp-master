package com.example.javier.MaterialDesignApp.Fragments;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.javier.MaterialDesignApp.MainActivity;
import com.example.javier.MaterialDesignApp.Model.AFilmModel;
import com.example.javier.MaterialDesignApp.Model.PageModel;
import com.example.javier.MaterialDesignApp.Model.VideosModel;
import com.example.javier.MaterialDesignApp.R;
import com.example.javier.MaterialDesignApp.RecyclerView.RecyclerViewAdapters.DevelopAdapter;
import com.example.javier.MaterialDesignApp.RecyclerView.RecyclerViewDecorations.DividerItemDecoration;
import com.example.javier.MaterialDesignApp.RecyclerView.RecyclerViewUtils.EndlessRecyclerOnScrollListener;
import com.example.javier.MaterialDesignApp.RecyclerView.RecyclerViewUtils.ItemClickSupport;
import com.example.javier.MaterialDesignApp.Utils.JsonParser;
import com.example.javier.MaterialDesignApp.Utils.ScrollManagerToolbar;
import com.example.javier.MaterialDesignApp.VideoPlayActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import static android.support.v7.widget.RecyclerView.Adapter;

public class FragmentDevelop extends Fragment {
    Listener delegate;
    Listener1 delegate1;
    String urlPost;

    SwipeRefreshLayout swipeRefreshLayout;

    PageModel Page;
    AFilmModel film;
    ArrayList<AFilmModel> films;
    ArrayList<AFilmModel> Morefilms;
    VideosModel video;
    ArrayList<VideosModel> videos;
    int postNumber = 99;
    SharedPreferences sharedPreferences;
    Boolean error = false;
    RecyclerView recyclerView;
    Adapter adapter;
    View view;
    int recyclerViewPaddingTop;
    TypedValue typedValueToolbarHeight = new TypedValue();
    Toolbar toolbar;
    FrameLayout statusBar;
    private LinearLayoutManager mLayoutManager;
    private boolean loading = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    JSONObject jsonObjectDevelopPosts;
    JSONArray jsonArrayDevelopContent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_develop, container, false);

        sharedPreferences = getActivity().getSharedPreferences("VALUES", Context.MODE_PRIVATE);

        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        statusBar = (FrameLayout) getActivity().findViewById(R.id.statusBar);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Develop");

        // Hide and show toolbar and statusBar on scroll
        toolbarHideShow();

        // Setup RecyclerView News
        recyclerViewDevelop(view);

        // Setup swipe to refresh
        swipeToRefresh(view);

        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                visibleItemCount = mLayoutManager.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        loading = false;
                        LoadMore("http://nhatphim.com/index/api?type=yes&page=2&limit=20");
                        Toast toast = Toast.makeText(getActivity(), "test ly", Toast.LENGTH_SHORT);
                        toast.show();

                    }
                }
            }
        });

        return view;
    }


    private void recyclerViewDevelop(View view) {

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewDevelop);

        // Divider
        recyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(android.R.drawable.divider_horizontal_bright)));

        // improve performance if you know that changes in content
        // do not change the size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, typedValueToolbarHeight, true);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (Build.VERSION.SDK_INT >= 19) {
                recyclerViewPaddingTop = TypedValue.complexToDimensionPixelSize(typedValueToolbarHeight.data, getResources().getDisplayMetrics()) + convertToPx(25);
            } else {
                recyclerViewPaddingTop = TypedValue.complexToDimensionPixelSize(typedValueToolbarHeight.data, getResources().getDisplayMetrics());
            }
        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (Build.VERSION.SDK_INT >= 21) {
                recyclerViewPaddingTop = TypedValue.complexToDimensionPixelSize(typedValueToolbarHeight.data, getResources().getDisplayMetrics());
            }
            if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
                recyclerViewPaddingTop = TypedValue.complexToDimensionPixelSize(typedValueToolbarHeight.data, getResources().getDisplayMetrics()) + convertToPx(25);
            }
            if (Build.VERSION.SDK_INT < 19) {
                recyclerViewPaddingTop = TypedValue.complexToDimensionPixelSize(typedValueToolbarHeight.data, getResources().getDisplayMetrics());
            }
        }

        recyclerView.setPadding(0, recyclerViewPaddingTop, 0, 0);

        urlPost = "http://nhatphim.com/index/api?type=yes&page=1&limit=20";
        films = new ArrayList<AFilmModel>();
        new AsyncTaskNewsParseJson().execute(urlPost);
        ItemClickSupport itemClickSupport = ItemClickSupport.addTo(recyclerView);
        itemClickSupport.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position, long id) {
                sharedPreferences.edit().putString("TITLE", films.get(position).getTitle_en()).apply();
                sharedPreferences.edit().putString("CONTENT", films.get(position).getTitle_en()).apply();
                sharedPreferences.edit().putString("EXCERPT", films.get(position).getTitle_vn()).apply();
                sharedPreferences.edit().putString("IMAGE", films.get(position).getImages()).apply();
                Toast.makeText(getActivity(), "Clicked: " + position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), VideoPlayActivity.class);
                startActivity(intent);
            }
        });


        delegate = new Listener() {
            @Override
            public void GetFinish() {

               /* recyclerView.setOnScrollListener( new OnScrollListener() {

                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);


                    }
                });*/

            }
        };


    }

    public void LoadMore(String link) {
        urlPost = link;
        adapter.notifyItemInserted(films.size());
        new AsyncTaskNewsParseJsonMore().execute(urlPost);
        delegate1 = new Listener1() {
            @Override
            public void GetFinish1() {
                adapter.notifyDataSetChanged();
                toolbarHideShow();
            }
        };

    }

    private void swipeToRefresh(View view) {
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        int start = convertToPx(0), end = recyclerViewPaddingTop + convertToPx(16);
        swipeRefreshLayout.setProgressViewOffset(true, start, end);
        TypedValue typedValueColorPrimary = new TypedValue();
        TypedValue typedValueColorAccent = new TypedValue();
        getActivity().getTheme().resolveAttribute(R.attr.colorPrimary, typedValueColorPrimary, true);
        getActivity().getTheme().resolveAttribute(R.attr.colorAccent, typedValueColorAccent, true);
        final int colorPrimary = typedValueColorPrimary.data, colorAccent = typedValueColorAccent.data;
        swipeRefreshLayout.setColorSchemeColors(colorPrimary, colorAccent);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new AsyncTaskNewsParseJson().execute(urlPost);
            }
        });
    }

    public void toolbarHideShow() {
        final Activity activity = getActivity();
        toolbar.post(new Runnable() {
            @Override
            public void run() {
                ScrollManagerToolbar manager = new ScrollManagerToolbar(activity);
                manager.attach(recyclerView);
                manager.addView(toolbar, ScrollManagerToolbar.Direction.UP);
                manager.addView(statusBar, ScrollManagerToolbar.Direction.UP);
                manager.setInitialOffset(toolbar.getHeight());

            }
        });
    }

    public int convertToPx(int dp) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (dp * scale + 0.5f);
    }

    public void animationTranslationY(View view, int duration, int interpolator, int translationY) {
        Animator slideInAnimation = ObjectAnimator.ofFloat(view, "translationY", translationY);
        slideInAnimation.setDuration(duration);
        slideInAnimation.setInterpolator(new AccelerateInterpolator(interpolator));
        slideInAnimation.start();
    }

    interface Listener {
        public void GetFinish();
    }

    interface Listener1 {
        public void GetFinish1();
    }

    public class AsyncTaskNewsParseJson extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
        }

        // get JSON Object
        @Override
        protected String doInBackground(String... url) {

            urlPost = url[0];

            try {
                jsonObjectDevelopPosts = JsonParser.readJsonFromUrl(urlPost);
                postNumber = jsonObjectDevelopPosts.getJSONArray("row").length();
                Page = new PageModel();
                jsonArrayDevelopContent = jsonObjectDevelopPosts.getJSONArray("row");
                Page.setPage(jsonObjectDevelopPosts.getString("page"));
                Page.setTotalView(jsonObjectDevelopPosts.getString("totalview"));
                Page.setTotalRow(jsonObjectDevelopPosts.getString("totalrow"));
                Page.setTotalPage(jsonObjectDevelopPosts.getString("totalpage"));
                films.clear();
                sharedPreferences.edit().putString("DEVELOP", jsonArrayDevelopContent.toString()).apply();

                for (int i = 0; i < postNumber; i++) {
                    film = new AFilmModel();
                    JSONObject jsonobject = jsonArrayDevelopContent.getJSONObject(i);
                    film.setId(jsonobject.getString("id"));
                    film.setId_film(jsonobject.getString("id_film"));
                    film.setCategory(jsonobject.getString("category"));
                    film.setImages(jsonobject.getString("images"));
                    film.setTitle_en(jsonobject.getString("title_en"));
                    film.setTitle_vn(jsonobject.getString("title_vn"));
                    JSONObject objserver = jsonobject.getJSONObject("server-1");
                    JSONArray arrvideo = objserver.getJSONArray("videos");
                    videos = new ArrayList<VideosModel>();
                    for (int j = 0; j < arrvideo.length(); j++) {
                        JSONObject jsonObjectVideos = arrvideo.getJSONObject(j);
                        video = new VideosModel();
                        video.setLable(jsonObjectVideos.getString("label"));
                        video.setLink(jsonObjectVideos.getString("link"));
                        videos.add(video);

                    }
                    film.setServer(videos);
                    films.add(film);
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();

                error = true;
            }
            return null;
        }

        // Set facebook items to the textviews and imageviews
        @Override
        protected void onPostExecute(String result) {

            toolbar.setTranslationY(0);
            adapter = new DevelopAdapter(getActivity(), films);
            recyclerView.setAdapter(adapter);
            swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
            swipeRefreshLayout.setRefreshing(false);
            ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            progressBar.setVisibility(View.GONE);
            delegate.GetFinish();
            //   toolbarHideShow();
        }
    }

    public class AsyncTaskNewsParseJsonMore extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
        }

        // get JSON Object
        @Override
        protected String doInBackground(String... url) {

            urlPost = url[0];

            try {
                jsonObjectDevelopPosts = JsonParser.readJsonFromUrl(urlPost);
                postNumber = jsonObjectDevelopPosts.getJSONArray("row").length();
                Page = new PageModel();
                jsonArrayDevelopContent = jsonObjectDevelopPosts.getJSONArray("row");
                Page.setPage(jsonObjectDevelopPosts.getString("page"));
                Page.setTotalView(jsonObjectDevelopPosts.getString("totalview"));
                Page.setTotalRow(jsonObjectDevelopPosts.getString("totalrow"));
                Page.setTotalPage(jsonObjectDevelopPosts.getString("totalpage"));

                sharedPreferences.edit().putString("DEVELOP", jsonArrayDevelopContent.toString()).apply();
                for (int i = 0; i < postNumber; i++) {
                    film = new AFilmModel();
                    JSONObject jsonobject = jsonArrayDevelopContent.getJSONObject(i);
                    film.setId(jsonobject.getString("id"));
                    film.setId_film(jsonobject.getString("id_film"));
                    film.setCategory(jsonobject.getString("category"));
                    film.setImages(jsonobject.getString("images"));
                    film.setTitle_en(jsonobject.getString("title_en"));
                    film.setTitle_vn(jsonobject.getString("title_vn"));
                    JSONObject objserver = jsonobject.getJSONObject("server-1");
                    JSONArray arrvideo = objserver.getJSONArray("videos");
                    videos = new ArrayList<VideosModel>();
                    for (int j = 0; j < arrvideo.length(); j++) {
                        JSONObject jsonObjectVideos = arrvideo.getJSONObject(j);
                        video = new VideosModel();
                        video.setLable(jsonObjectVideos.getString("label"));
                        video.setLink(jsonObjectVideos.getString("link"));
                        videos.add(video);

                    }
                    film.setServer(videos);
                    films.add(film);
                }
            } catch (IOException | JSONException e) {

                error = true;
            }
            return null;
        }

        // Set facebook items to the textviews and imageviews
        @Override
        protected void onPostExecute(String result) {

            toolbar.setTranslationY(0);
            swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
            swipeRefreshLayout.setRefreshing(false);
            ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            progressBar.setVisibility(View.GONE);
            delegate1.GetFinish1();
        }
    }
}