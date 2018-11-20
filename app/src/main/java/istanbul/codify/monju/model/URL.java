package istanbul.codify.monju.model;

import android.support.annotation.StringDef;
import istanbul.codify.monju.BuildConfig;

@StringDef({URL.POLICY})
public @interface URL {
    String POLICY = BuildConfig.URL + "sozlesme/gor/gizlilik";
}