package istanbul.codify.muudy.model;

import android.support.annotation.StringDef;
import istanbul.codify.muudy.BuildConfig;

@StringDef({URL.POLICY})
public @interface URL {
    String POLICY = BuildConfig.URL + "sozlesme/gor/gizlilik";
}