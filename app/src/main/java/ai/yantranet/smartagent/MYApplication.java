package ai.yantranet.smartagent;

import android.app.Application;

public class MYApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

    }
    static {
        System.loadLibrary("keys");
    }
}
