package com.example.nishant.chatnow;

import android.app.Application;

import com.parse.Parse;

public class ChatNowApplication extends Application {
    public void onCreate() {
        Parse.enableLocalDatastore(this);

        Parse.initialize(this);
    }
}