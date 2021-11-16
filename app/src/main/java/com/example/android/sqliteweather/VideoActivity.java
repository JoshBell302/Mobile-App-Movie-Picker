package com.example.android.sqliteweather;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.android.sqliteweather.data.TrailerLink;
import com.example.android.sqliteweather.data.VideoConfig;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class VideoActivity extends YouTubeBaseActivity {
    private static final String TAG = "VideoActivity";
    public static final String EXTRA_TRAILER_KEY = "VideoActivity.TrailerLink";

    YouTubePlayerView mYouTubePlayerView;
    Button play_button;
    YouTubePlayer.OnInitializedListener mOnInitializedListener;
    private String defaultKey ="14P_k6iVybA";
    private TrailerLink trailerLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        Log.d(TAG, "onCreate: Starting");
        play_button = (Button) findViewById(R.id.play_button);
        mYouTubePlayerView = (YouTubePlayerView) findViewById(R.id.video_player);

        Intent intent = getIntent();
        
        if (intent != null && intent.hasExtra(EXTRA_TRAILER_KEY)) {
            trailerLink = (TrailerLink) intent.getSerializableExtra(EXTRA_TRAILER_KEY);
        }else{
            trailerLink = new TrailerLink(defaultKey);
        }

        mOnInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                Log.d(TAG, "onClick: Done Intializing Video Player");
                youTubePlayer.loadVideo(trailerLink.getKey());
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d(TAG, "onClick: Failed Intializing Video Player");
            }
        };
        play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Intializing Video Player");
                mYouTubePlayerView.initialize(VideoConfig.getApiKey(), mOnInitializedListener);
            }
        });
    }
}
