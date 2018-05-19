package com.iprismtech.rawvana;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.iprismtech.rawvana.others.HelperObj;
import com.iprismtech.rawvana.others.MyExceptionHandler;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActivityYouTube extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener{
    private Button backBtn;
    private Context context;
    private YouTubePlayerView youTubeView;
    private String API_Key="AIzaSyAWw5h-fYOM3Z9pqPkB1f9FOlNuvXzZ-5Q";
    private static final int RECOVERY_REQUEST = 1;
    private String URL="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youtube);
        context=getApplicationContext();
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this, ActivityYouTube.class));
        //
        setUp();
        onClickEvents();
    }

    private void setUp() {
        try {
            backBtn=(Button)findViewById(R.id.backBtn);
            youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
            //
            URL=getIntent().getExtras().getString("URL");
            if(URL.isEmpty()){
                HelperObj.getInstance().cusToast(context,"No video found");
                finish();
            }


            youTubeView.initialize(API_Key, ActivityYouTube.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void onClickEvents() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {
            player.cueVideo(getVideoId(URL));
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            String error = String.format(getString(R.string.player_error), errorReason.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(API_Key, ActivityYouTube.this);
        }
    }

    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return youTubeView;
    }
    final static String reg = "(?:youtube(?:-nocookie)?\\.com\\/(?:[^\\/\\n\\s]+\\/\\S+\\/|(?:v|e(?:mbed)?)\\/|\\S*?[?&]v=)|youtu\\.be\\/)([a-zA-Z0-9_-]{11})";

    public static String getVideoId(String videoUrl) {
        if (videoUrl == null || videoUrl.trim().length() <= 0)
            return null;

        Pattern pattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(videoUrl);

        if (matcher.find())
            return matcher.group(1);
        return null;
    }
}

