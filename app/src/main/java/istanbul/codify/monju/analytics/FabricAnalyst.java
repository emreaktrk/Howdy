package istanbul.codify.monju.analytics;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

public final class FabricAnalyst implements IAnalyst {

    @Override
    public void custom(String event) {
        Answers
                .getInstance()
                .logCustom(new CustomEvent(event));
    }
}
