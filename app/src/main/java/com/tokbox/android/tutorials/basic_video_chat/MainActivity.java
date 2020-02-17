package com.tokbox.android.tutorials.basic_video_chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.tokbox.android.tutorials.basic_video_chat.audioonly.AudioActivity;
import com.tokbox.android.tutorials.basic_video_chat.audioonly.AudioActivity2;
import com.tokbox.android.tutorials.basic_video_chat.videoaudio.VideoAudioActivity;
import com.tokbox.android.tutorials.basic_video_chat.videoaudio.VideoAudioActivity2;
import com.tokbox.android.tutorials.basicvideochat.R;

/**
 * Created by Manish Patel on 2/13/2020.
 */
public class MainActivity extends AppCompatActivity implements OnClickListener {

    Button button_1, button_2, button_3, button_4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);

        button_1 = (Button) findViewById(R.id.button_1);
        button_2 = (Button) findViewById(R.id.button_2);
        button_3 = (Button) findViewById(R.id.button_3);
        button_4 = (Button) findViewById(R.id.button_4);

        button_1.setOnClickListener(this);
        button_2.setOnClickListener(this);
        button_3.setOnClickListener(this);
        button_4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.button_1:
                /**Video + Audio Bidirectional Demo **/
                intent = new Intent(MainActivity.this, VideoAudioActivity.class);
                break;
            case R.id.button_2:
                /**Video + Audio One directional Demo **/
                intent = new Intent(MainActivity.this, VideoAudioActivity2.class);
                break;
            case R.id.button_3:
                /**Audio Bidirectional Demo **/
                intent = new Intent(MainActivity.this, AudioActivity.class);
                break;
            case R.id.button_4:
                /**Audio One directional Demo **/
                intent = new Intent(MainActivity.this, AudioActivity2.class);
                break;
        }

        startActivity(intent);
    }
}
