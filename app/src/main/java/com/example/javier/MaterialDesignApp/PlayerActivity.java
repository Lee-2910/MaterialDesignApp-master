/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.javier.MaterialDesignApp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.accessibility.CaptioningManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.javier.MaterialDesignApp.Fragments.FragmentDevelop;
import com.example.javier.MaterialDesignApp.Fragments.FragmentFilms;
import com.example.javier.MaterialDesignApp.Fragments.VerticalFragment;
import com.example.javier.MaterialDesignApp.Fragments.VerticalGridFragment;
import com.example.javier.MaterialDesignApp.ListViewAdapter.NavDrawerListAdapter;
import com.example.javier.MaterialDesignApp.Model.AFilmModel;
import com.example.javier.MaterialDesignApp.Model.ListVideoModel;
import com.example.javier.MaterialDesignApp.Model.VideosModel;
import com.example.javier.MaterialDesignApp.RecyclerView.RecyclerViewAdapters.FILMSAdapter;
import com.example.javier.MaterialDesignApp.RecyclerView.RecyclerViewAdapters.ListVideosAdapter;
import com.example.javier.MaterialDesignApp.RecyclerView.RecyclerViewDecorations.DividerItemDecoration;
import com.example.javier.MaterialDesignApp.RecyclerView.RecyclerViewUtils.ItemClickSupport;

import com.example.javier.MaterialDesignApp.Utils.JsonParser;
import com.example.javier.MaterialDesignApp.player.DashRendererBuilder;
import com.example.javier.MaterialDesignApp.player.DefaultRendererBuilder;
import com.example.javier.MaterialDesignApp.player.DemoPlayer;
import com.example.javier.MaterialDesignApp.player.DemoPlayer.RendererBuilder;
import com.example.javier.MaterialDesignApp.player.HlsRendererBuilder;
import com.example.javier.MaterialDesignApp.player.SmoothStreamingRendererBuilder;
import com.example.javier.MaterialDesignApp.player.UnsupportedDrmException;
import com.example.javier.MaterialDesignApp.player.VideoControllerView;
import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.VideoSurfaceView;
import com.google.android.exoplayer.metadata.TxxxMetadata;
import com.google.android.exoplayer.text.CaptionStyleCompat;
import com.google.android.exoplayer.text.SubtitleView;
import com.google.android.exoplayer.util.Util;
import com.google.android.exoplayer.util.VerboseLogUtil;
import com.google.gson.Gson;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * An activity that plays media using {@link DemoPlayer}.
 */
public class PlayerActivity extends FragmentActivity implements SurfaceHolder.Callback, OnClickListener,
        DemoPlayer.Listener, DemoPlayer.TextListener, DemoPlayer.Id3MetadataListener, VideoControllerView.MediaPlayerControl {
  final Context context = this;
    public static final String CONTENT_TYPE_EXTRA = "content_type";
    public static final String CONTENT_ID_EXTRA = "content_id";
    String urlPost;
    private static final String TAG = "PlayerActivity";

    private static final float CAPTION_LINE_HEIGHT_RATIO = 0.0533f;
    private static final int MENU_GROUP_TRACKS = 1;
    private static final int ID_OFFSET = 2;

    private EventLogger eventLogger;
    private VideoControllerView mediaController;
    private View debugRootView;
    private View shutterView;
    private VideoSurfaceView surfaceView;
    private TextView debugTextView;
    private TextView playerStateTextView;
    private SubtitleView subtitleView;
    private Button videoButton;
    private Button audioButton;
    private Button textButton;
    private Button retryButton;
    TypedValue typedValueToolbarHeight = new TypedValue();
    private DemoPlayer player;
    private boolean playerNeedsPrepare;

    private long playerPosition;
    private boolean enableBackgroundAudio;
    int recyclerViewPaddingTop;
    private Uri contentUri=null;
    private int contentType;
    private String contentId;
    private int fullScreen = 1;
    // Activity lifecycle
    JSONObject jsonObjectDevelopPosts;
    JSONArray jsonArrayDevelopContent;
    ListVideoModel Film;
    VideosModel video;
    ArrayList<VideosModel> Videos;
    View root;
    Listener delegate;
    RecyclerView recyclerView;
    RecyclerView.Adapter recyclerViewAdapter;
    private NavDrawerListAdapter adapter;
    private ListView lv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String data = prefs.getString("AFilmModel",null);
        Gson gson = new Gson();

        AFilmModel aFilm = gson.fromJson(data, AFilmModel.class);
        String link = "http://nhatphim.com/index/api-detail?id="+aFilm.getId();
        getListVideo(link);
      //  contentUri = Uri.parse("http://www.phim3s.net/phim-bo/thien-kim-tro-ve_8328/xem-phim/221808/video.mp4");
     /*   contentUri = Uri.parse("http://www.phim3s.net/phim-bo/thien-kim-tro-ve_8328/xem-phim/221808/video.mp4");*/
        contentType = 2;
        contentId = "dizzy";

        setContentView(R.layout.player_activity);
       root = findViewById(R.id.root);
        root.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    toggleControlsVisibility();
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    view.performClick();
                }
                return true;
            }
        });

        shutterView = findViewById(R.id.shutter);
        debugRootView = findViewById(R.id.controls_root);

        surfaceView = (VideoSurfaceView) findViewById(R.id.surface_view);
        surfaceView.getHolder().addCallback(this);
        debugTextView = (TextView) findViewById(R.id.debug_text_view);

        playerStateTextView = (TextView) findViewById(R.id.player_state_view);
        subtitleView = (SubtitleView) findViewById(R.id.subtitles);

        mediaController = new VideoControllerView(this);
        mediaController.setAnchorView((FrameLayout) root);
        mediaController.setPrevNextListeners(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //next button clicked
                Toast t = Toast.makeText(PlayerActivity.this, "click", Toast.LENGTH_SHORT);
                t.show();
            }
        }, new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast t = Toast.makeText(PlayerActivity.this, "pt bt", Toast.LENGTH_SHORT);
                t.show();
            }
        });
        retryButton = (Button) findViewById(R.id.retry_button);
        retryButton.setOnClickListener(this);
        videoButton = (Button) findViewById(R.id.video_controls);
        audioButton = (Button) findViewById(R.id.audio_controls);
        textButton = (Button) findViewById(R.id.text_controls);

        DemoUtil.setDefaultCookieManager();
        configureSubtitleView();
        delegate = new Listener() {
            @Override
            public void GetFinish() {
              //  adapter = new NavDrawerListAdapter(PlayerActivity.this,Film.getServer());
              //  lv.setAdapter(adapter);
                contentUri = Uri.parse(Film.getServer().get(0).getLink());
                if (player == null&&contentUri!=null) {

                    preparePlayer();
                    ProgressBar progressBar = (ProgressBar) root.findViewById(R.id.progressBar);
                    progressBar.setVisibility(View.GONE);
                    /////////////////////////////////////////////
                    FragmentManager fragmentManager;
                    FragmentTransaction fragmentTransaction;
                    fragmentManager = getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    VerticalGridFragment verticalFragment = new VerticalGridFragment(PlayerActivity.this,Film.getServer());
                    fragmentTransaction.replace(R.id.view_fm, verticalFragment);
                    fragmentTransaction.commit();

                }
            }
        };
        recyclerViewDesign();

    }

    private void recyclerViewDesign() {
  // Setup RecyclerView inside drawer


		
      /* recyclerView = (RecyclerView) findViewById(R.id.recyclerViewDevelop1);
       // do not change the size of the RecyclerView
       // recyclerView.setHasFixedSize(true);
        // Divider
      //  recyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(android.R.drawable.divider_horizontal_bright)));

        // improve performance if you know that changes in content
     

        // use a linear layout manager
       // LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PlayerActivity.this.getApplicationContext());
      //  recyclerView.setLayoutManager(new LinearLayoutManager(PlayerActivity.this));
      //  recyclerView.setItemAnimator(new DefaultItemAnimator());
        this.getTheme().resolveAttribute(android.R.attr.actionBarSize, typedValueToolbarHeight, true);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (Build.VERSION.SDK_INT >= 19) {

            } else {
                recyclerViewPaddingTop = TypedValue.complexToDimensionPixelSize(typedValueToolbarHeight.data, getResources().getDisplayMetrics());
            }
        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (Build.VERSION.SDK_INT >= 21) {
                recyclerViewPaddingTop = TypedValue.complexToDimensionPixelSize(typedValueToolbarHeight.data, getResources().getDisplayMetrics());
            }
            if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {

            }
            if (Build.VERSION.SDK_INT < 19) {
                recyclerViewPaddingTop = TypedValue.complexToDimensionPixelSize(typedValueToolbarHeight.data, getResources().getDisplayMetrics());
            }
        }

        recyclerView.setPadding(0, recyclerViewPaddingTop, 0, 0);


        ItemClickSupport itemClickSupport = ItemClickSupport.addTo(recyclerView);
        itemClickSupport.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position, long id) {


/////

            }
        });*/
    }
    @Override
    public void onResume() {

        super.onResume();
        configureSubtitleView();
        if (player == null&&contentUri!=null) {
            preparePlayer();
        } else if (player != null) {
            player.setBackgrounded(false);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!enableBackgroundAudio) {
            releasePlayer();
        } else {
            player.setBackgrounded(true);
        }
        shutterView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    // OnClickListener methods

    @Override
    public void onClick(View view) {
        if (view == retryButton) {
            preparePlayer();
        }
    }

    // Internal methods

    private RendererBuilder getRendererBuilder() {
        String userAgent = DemoUtil.getUserAgent(this);
        switch (contentType) {
            case DemoUtil.TYPE_SS:
                return new SmoothStreamingRendererBuilder(userAgent, contentUri.toString(), contentId,
                        new SmoothStreamingTestMediaDrmCallback(), debugTextView);
            case DemoUtil.TYPE_DASH:
                return new DashRendererBuilder(userAgent, contentUri.toString(), contentId,
                        new WidevineTestMediaDrmCallback(contentId), debugTextView);
            case DemoUtil.TYPE_HLS:
                return new HlsRendererBuilder(userAgent, contentUri.toString(), contentId);
            default:
                return new DefaultRendererBuilder(this, contentUri, debugTextView);
        }
    }

    private void preparePlayer() {
        if (player == null) {
            player = new DemoPlayer(getRendererBuilder());
            player.addListener(this);
            player.setTextListener(this);
            player.setMetadataListener(this);
            player.seekTo(playerPosition);
            playerNeedsPrepare = true;
            mediaController.setMediaPlayer(this);
            mediaController.setEnabled(true);
            eventLogger = new EventLogger();
            eventLogger.startSession();
            player.addListener(eventLogger);
            player.setInfoListener(eventLogger);
            player.setInternalErrorListener(eventLogger);

        }
        if (playerNeedsPrepare) {
            player.prepare();
            playerNeedsPrepare = false;
            updateButtonVisibilities();
        }
        player.setSurface(surfaceView.getHolder().getSurface());
        player.setPlayWhenReady(true);

    }

    private void releasePlayer() {
        if (player != null) {
            playerPosition = player.getCurrentPosition();

            player.release();
            player = null;
            eventLogger.endSession();
            eventLogger = null;
        }
    }

    // DemoPlayer.Listener implementation

    @Override
    public void onStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == ExoPlayer.STATE_ENDED) {
            showControls();
        }

        String text = "playWhenReady=" + playWhenReady + ", playbackState=";
        switch (playbackState) {
            case ExoPlayer.STATE_BUFFERING:
                text += "buffering";
                break;
            case ExoPlayer.STATE_ENDED:
                text += "ended";
                break;
            case ExoPlayer.STATE_IDLE:
                text += "idle";
                break;
            case ExoPlayer.STATE_PREPARING:
                text += "preparing";
                break;
            case ExoPlayer.STATE_READY:

                //  progressBar.setVisibility(View.GONE);

                text += "ready";
                break;
            default:
                text += "unknown";
                break;
        }
        playerStateTextView.setText(text);
        updateButtonVisibilities();
    }

    @Override
    public void onError(Exception e) {
        if (e instanceof UnsupportedDrmException) {
            // Special case DRM failures.
            UnsupportedDrmException unsupportedDrmException = (UnsupportedDrmException) e;
            int stringId = unsupportedDrmException.reason == UnsupportedDrmException.REASON_NO_DRM
                    ? R.string.drm_error_not_supported
                    : unsupportedDrmException.reason == UnsupportedDrmException.REASON_UNSUPPORTED_SCHEME
                    ? R.string.drm_error_unsupported_scheme
                    : R.string.drm_error_unknown;
            Toast.makeText(getApplicationContext(), stringId, Toast.LENGTH_LONG).show();
        }
        playerNeedsPrepare = true;
        updateButtonVisibilities();
        showControls();
    }

    @Override
    public void onVideoSizeChanged(int width, int height, float pixelWidthAspectRatio) {
        shutterView.setVisibility(View.GONE);
        surfaceView.setVideoWidthHeightRatio(
                height == 0 ? 1 : (width * pixelWidthAspectRatio) / height);
    }

    // User controls

    private void updateButtonVisibilities() {
        retryButton.setVisibility(playerNeedsPrepare ? View.VISIBLE : View.GONE);
        videoButton.setVisibility(haveTracks(DemoPlayer.TYPE_VIDEO) ? View.VISIBLE : View.GONE);
        audioButton.setVisibility(haveTracks(DemoPlayer.TYPE_AUDIO) ? View.VISIBLE : View.GONE);
        textButton.setVisibility(haveTracks(DemoPlayer.TYPE_TEXT) ? View.VISIBLE : View.GONE);
    }

    private boolean haveTracks(int type) {
        return player != null && player.getTracks(type) != null;
    }

    public void showVideoPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        configurePopupWithTracks(popup, null, DemoPlayer.TYPE_VIDEO);
        popup.show();
    }

    public void showAudioPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        Menu menu = popup.getMenu();
        menu.add(Menu.NONE, Menu.NONE, Menu.NONE, R.string.enable_background_audio);
        final MenuItem backgroundAudioItem = menu.findItem(0);
        backgroundAudioItem.setCheckable(true);
        backgroundAudioItem.setChecked(enableBackgroundAudio);
        OnMenuItemClickListener clickListener = new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item == backgroundAudioItem) {
                    enableBackgroundAudio = !item.isChecked();
                    return true;
                }
                return false;
            }
        };
        configurePopupWithTracks(popup, clickListener, DemoPlayer.TYPE_AUDIO);
        popup.show();
    }

    public void showTextPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        configurePopupWithTracks(popup, null, DemoPlayer.TYPE_TEXT);
        popup.show();
    }

    public void showVerboseLogPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        Menu menu = popup.getMenu();
        menu.add(Menu.NONE, 0, Menu.NONE, R.string.logging_normal);
        menu.add(Menu.NONE, 1, Menu.NONE, R.string.logging_verbose);
        menu.setGroupCheckable(Menu.NONE, true, true);
        menu.findItem((VerboseLogUtil.areAllTagsEnabled()) ? 1 : 0).setChecked(true);
        popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == 0) {
                    VerboseLogUtil.setEnableAllTags(false);
                } else {
                    VerboseLogUtil.setEnableAllTags(true);
                }
                return true;
            }
        });
        popup.show();
    }

    private void configurePopupWithTracks(PopupMenu popup,
                                          final OnMenuItemClickListener customActionClickListener,
                                          final int trackType) {
        if (player == null) {
            return;
        }
        String[] tracks = player.getTracks(trackType);
        if (tracks == null) {
            return;
        }
        popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return (customActionClickListener != null
                        && customActionClickListener.onMenuItemClick(item))
                        || onTrackItemClick(item, trackType);
            }
        });
        Menu menu = popup.getMenu();
        // ID_OFFSET ensures we avoid clashing with Menu.NONE (which equals 0)
        menu.add(MENU_GROUP_TRACKS, DemoPlayer.DISABLED_TRACK + ID_OFFSET, Menu.NONE, R.string.off);
        if (tracks.length == 1 && TextUtils.isEmpty(tracks[0])) {
            menu.add(MENU_GROUP_TRACKS, DemoPlayer.PRIMARY_TRACK + ID_OFFSET, Menu.NONE, R.string.on);
        } else {
            for (int i = 0; i < tracks.length; i++) {
                menu.add(MENU_GROUP_TRACKS, i + ID_OFFSET, Menu.NONE, tracks[i]);
            }
        }
        menu.setGroupCheckable(MENU_GROUP_TRACKS, true, true);
        menu.findItem(player.getSelectedTrackIndex(trackType) + ID_OFFSET).setChecked(true);
    }

    private boolean onTrackItemClick(MenuItem item, int type) {
        if (player == null || item.getGroupId() != MENU_GROUP_TRACKS) {
            return false;
        }
        player.selectTrack(type, item.getItemId() - ID_OFFSET);
        return true;
    }

    private void toggleControlsVisibility() {
        if (mediaController.isShowing()) {
            mediaController.hide();
            debugRootView.setVisibility(View.GONE);
        } else {
            showControls();
        }
    }

    private void showControls() {
        if (getBufferPercentage() > 0) {
            mediaController.show();
            debugRootView.setVisibility(View.VISIBLE);
        }


    }

    // DemoPlayer.TextListener implementation

    @Override
    public void onText(String text) {
        if (TextUtils.isEmpty(text)) {
            subtitleView.setVisibility(View.INVISIBLE);
        } else {
            subtitleView.setVisibility(View.VISIBLE);
            subtitleView.setText(text);
        }
    }

    // DemoPlayer.MetadataListener implementation

    @Override
    public void onId3Metadata(Map<String, Object> metadata) {
        for (int i = 0; i < metadata.size(); i++) {
            if (metadata.containsKey(TxxxMetadata.TYPE)) {
                TxxxMetadata txxxMetadata = (TxxxMetadata) metadata.get(TxxxMetadata.TYPE);
                Log.i(TAG, String.format("ID3 TimedMetadata: description=%s, value=%s",
                        txxxMetadata.description, txxxMetadata.value));
            }
        }
    }

    // SurfaceHolder.Callback implementation

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (player != null) {
            player.setSurface(holder.getSurface());

        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // Do nothing.
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (player != null) {
            player.blockingClearSurface();
        }
    }

    private void configureSubtitleView() {
        CaptionStyleCompat captionStyle;
        float captionTextSize = getCaptionFontSize();
        if (Util.SDK_INT >= 19) {
            captionStyle = getUserCaptionStyleV19();
            captionTextSize *= getUserCaptionFontScaleV19();
        } else {
            captionStyle = CaptionStyleCompat.DEFAULT;
        }
        subtitleView.setStyle(captionStyle);
        subtitleView.setTextSize(captionTextSize);
    }

    private float getCaptionFontSize() {
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay();
        Point displaySize = new Point();
        display.getSize(displaySize);
        return Math.max(getResources().getDimension(R.dimen.subtitle_minimum_font_size),
                CAPTION_LINE_HEIGHT_RATIO * Math.min(displaySize.x, displaySize.y));
    }

    @TargetApi(19)
    private float getUserCaptionFontScaleV19() {
        CaptioningManager captioningManager =
                (CaptioningManager) getSystemService(Context.CAPTIONING_SERVICE);
        return captioningManager.getFontScale();
    }

    @TargetApi(19)
    private CaptionStyleCompat getUserCaptionStyleV19() {
        CaptioningManager captioningManager =
                (CaptioningManager) getSystemService(Context.CAPTIONING_SERVICE);
        return CaptionStyleCompat.createFromCaptionStyle(captioningManager.getUserStyle());
    }

    @Override
    public void start() {
        Toast t = Toast.makeText(PlayerActivity.this, "1", Toast.LENGTH_SHORT);
        t.show();
        player.getPlayerControl().start();
    }

    @Override
    public void pause() {
        Toast t = Toast.makeText(PlayerActivity.this, "2", Toast.LENGTH_SHORT);
        t.show();
        player.getPlayerControl().pause();
    }

    @Override
    public int getDuration() {
        return (int) player.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return (int) player.getCurrentPosition();
    }

    @Override
    public void seekTo(int pos) {
        player.seekTo(pos);
    }

    @Override
    public boolean isPlaying() {

        return player.getPlayWhenReady();
    }

    @Override
    public int getBufferPercentage() {
        return player.getBufferedPercentage();
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public boolean isFullScreen() {
        return false;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        fullScreen = newConfig.orientation;
        if (newConfig.orientation == 2) {

            PlayerActivity.this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                    | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                    | View.SYSTEM_UI_FLAG_IMMERSIVE);
        }
    }


    @Override
    public void toggleFullScreen() {

        if (fullScreen == 2) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if (fullScreen == 1) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }


    }
    public void getListVideo(String link) {
        urlPost = link;
        new AsyncTaskNewsParseJson().execute(urlPost);

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
                Film = new ListVideoModel();
                jsonArrayDevelopContent = jsonObjectDevelopPosts.getJSONArray("row");
                JSONObject objdata = jsonArrayDevelopContent.getJSONObject(0);
                Film.setId(objdata.getString("id"));
                Film.setId_film(objdata.getString("id_film"));
                Film.setCategory(objdata.getString("category"));
                Film.setTitle_vn(objdata.getString("title_vn"));
                Film.setTitle_en(objdata.getString("title_en"));
                Film.setImages(objdata.getString("images"));
                Film.setCountry(objdata.getString("country"));
                Film.setDirector(objdata.getString("director"));
                Film.setContent(objdata.getString("content"));
                Film.setInfo(objdata.getString("info"));
                Film.setActor(objdata.getString("actor"));
                String trFilm = objdata.getString("server-1");
                JSONObject obj = new JSONObject(trFilm);
                JSONArray arr =obj.optJSONArray("videos");
                Videos = new ArrayList<VideosModel>();
                for (int i = 0; i < arr.length(); i++) {
                    video = new VideosModel();
                    JSONObject jsonobject = arr.getJSONObject(i);
                    video.setLable(jsonobject.getString("label"));
                    video.setLink(jsonobject.getString("link"));
                    video.setImages(Film.getImages());
                    Videos.add(video);
                }
                Film.setServer(Videos);


            } catch (IOException | JSONException e) {
                e.printStackTrace();

               // error = true;
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {


           //recyclerViewAdapter = new ListVideosAdapter(PlayerActivity.this,Film.getServer());
          //  recyclerView.setAdapter(recyclerViewAdapter);

         //   swipeRefreshLayout = (android.support.v4.widget.SwipeRefreshLayout) findViewById(R.id.swipe_container);
          //  swipeRefreshLayout.setRefreshing(false);

            // Create the recyclerViewAdapter
           // recyclerViewAdapter = new ListVideosAdapter(PlayerActivity.this,Film.getServer());
            //recyclerView.setAdapter(recyclerViewAdapter);

         //   swipeRefreshLayout = (android.support.v4.widget.SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
           // swipeRefreshLayout.setRefreshing(false);

           //recyclerViewAdapter.notifyDataSetChanged();

            delegate.GetFinish();
        }
    }

    interface Listener {
        public void GetFinish();
    }

}
