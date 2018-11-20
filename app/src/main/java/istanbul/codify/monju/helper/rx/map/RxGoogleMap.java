package istanbul.codify.monju.helper.rx.map;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import com.google.android.gms.maps.GoogleMap;

public final class RxGoogleMap {

    @CheckResult
    @NonNull
    public static MapCameraIdleObservable cameraIdles(@NonNull GoogleMap map) {
        if (map == null) {
            throw new NullPointerException("Map is null");
        }

        return new MapCameraIdleObservable(map);
    }
}
