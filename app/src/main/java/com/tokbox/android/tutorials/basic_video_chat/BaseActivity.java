package com.tokbox.android.tutorials.basic_video_chat;

import android.support.v7.app.AppCompatActivity;

import com.opentok.android.Publisher;
import com.opentok.android.Session;
import com.opentok.android.Subscriber;

/**
 * Created by Manish Patel on 2/17/2020.
 */
public class BaseActivity extends AppCompatActivity {

    public String LOG_TAG = BaseActivity.class.getSimpleName();
    public static final int RC_SETTINGS_SCREEN_PERM = 123;
    public static final int RC_VIDEO_APP_PERM = 124;

    public boolean isSpeakButtonLongPressed = false;
    public boolean isChannelOwner = true;

    // Suppressing this warning. mWebServiceCoordinator will get GarbageCollected if it is local.
    @SuppressWarnings("FieldCanBeLocal")
    public WebServiceCoordinator mWebServiceCoordinator;

    public Session mSession;
    public Publisher mPublisher;
    public Subscriber mSubscriber;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSession != null) {
            mSession.disconnect();
        }
    }
}
