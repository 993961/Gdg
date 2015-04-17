package com.sked.gdg.app;

import android.app.Application;

import com.sked.gdg.database.DatabaseHelper;

/**
 * Created by Sanjeet on 26-Feb-15.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DatabaseHelper.initialize(getApplicationContext());
    }
}
