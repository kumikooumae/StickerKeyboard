package kumiko.stickerkeyboard;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
    }

    static Context getAppContext() {
        return appContext;
    }
}