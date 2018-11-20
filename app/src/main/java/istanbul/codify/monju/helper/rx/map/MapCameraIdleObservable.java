package istanbul.codify.monju.helper.rx.map;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.VisibleRegion;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

public class MapCameraIdleObservable extends Observable<VisibleRegion> {

    private GoogleMap mMap;

    MapCameraIdleObservable(GoogleMap map) {
        this.mMap = map;
    }

    @Override
    protected void subscribeActual(Observer<? super VisibleRegion> observer) {
        MapCameraIdleObservable.Listener listener = new MapCameraIdleObservable.Listener(mMap, observer);
        mMap.setOnCameraIdleListener(listener);
        observer.onSubscribe(listener);
    }

    static final class Listener extends MainThreadDisposable implements GoogleMap.OnCameraIdleListener {
        private final GoogleMap mMap;
        private final Observer<? super VisibleRegion> mObserver;

        Listener(GoogleMap map, Observer<? super VisibleRegion> observer) {
            this.mMap = map;
            this.mObserver = observer;
        }

        @Override
        public void onCameraIdle() {
            if (!isDisposed()) {
                VisibleRegion region = mMap.getProjection().getVisibleRegion();
                mObserver.onNext(region);
            }
        }

        @Override
        protected void onDispose() {
            mMap.setOnCameraIdleListener(null);
        }
    }
}
