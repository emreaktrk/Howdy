package istanbul.codify.muudy;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;


public abstract class MuudyFragment extends Fragment {

    abstract protected @LayoutRes
    int getLayoutResId();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutResId(), container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (this instanceof EventSupport) {
            EventBus
                    .getDefault()
                    .register(this);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (this instanceof EventSupport) {
            EventBus
                    .getDefault()
                    .unregister(this);
        }
    }

    @SuppressWarnings("unchecked")
    public @Nullable
    <T extends Serializable> T resolveResult(int requestCode, int resultCode, Intent data, Class<? extends T> clazz, int waitingRequestCode) {
        if (getActivity() != null && getActivity() instanceof MuudyActivity) {
            return ((MuudyActivity) getActivity()).resolveResult(requestCode, resultCode, data, clazz, waitingRequestCode);
        }

        return null;
    }
}
