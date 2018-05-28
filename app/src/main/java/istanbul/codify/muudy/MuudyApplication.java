package istanbul.codify.muudy;

import android.app.Application;

import com.blankj.utilcode.util.Utils;
import com.crashlytics.android.answers.Answers;
import io.fabric.sdk.android.Fabric;
import istanbul.codify.muudy.analytics.Analytics;
import istanbul.codify.muudy.analytics.FabricAnalyst;
import istanbul.codify.muudy.analytics.FirebaseAnalyst;
import com.google.firebase.FirebaseApp;


public final class MuudyApplication extends Application {

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
    }
}
