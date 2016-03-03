package org.example.testname.testapp;

import android.app.Application;
import android.util.Log;

/**
 * Created by Dima on 3/3/2016.
 */
public class TestApplication extends Application {

    public TestApplication() {
        // clean cache

    }

    @Override
    public void onCreate() {
        super.onCreate();
        ImageStorage.ClearTemporaryFolder();
    }
}
