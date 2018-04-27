package istanbul.codify.muudy;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;


public abstract class MuudyDialog extends DialogFragment {

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

    protected final <T extends Serializable> T getSerializable(Class<? extends T> clazz) {
        String key = clazz.getSimpleName();
        if (getArguments() != null && getArguments().containsKey(key)) {
            return clazz.cast(getArguments().getSerializable(key));
        }

        return null;
    }
}
