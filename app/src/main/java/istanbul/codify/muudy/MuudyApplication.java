package istanbul.codify.muudy;

import android.app.Application;
import android.content.Context;

import com.blankj.utilcode.util.Utils;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import io.fabric.sdk.android.Fabric;
import istanbul.codify.muudy.analytics.Analytics;
import istanbul.codify.muudy.analytics.FabricAnalyst;
import istanbul.codify.muudy.analytics.FirebaseAnalyst;
import com.google.firebase.FirebaseApp;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;


public final class MuudyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Answers());

        Utils.init(this);

        FirebaseApp.initializeApp(this);
        Analytics
                .getInstance()
                .add(new FirebaseAnalyst())
                .add(new FabricAnalyst());

        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this,Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(false);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);
    }

    public static Context getAppContext() {
        return MuudyApplication.context;
    }
}
