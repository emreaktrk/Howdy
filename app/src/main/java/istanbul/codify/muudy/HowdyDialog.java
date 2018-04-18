package istanbul.codify.muudy;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;


public abstract class HowdyDialog extends DialogFragment {

    abstract protected @LayoutRes
    int getLayoutResId();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutResId(), container, false);
    }

    @SuppressWarnings("unchecked")
    public @Nullable
    <T extends Serializable> T resolveResult(int requestCode, int resultCode, Intent data, Class<? extends T> clazz, int waitingRequestCode) {
        if (getActivity() != null && getActivity() instanceof HowdyActivity) {
            return ((HowdyActivity) getActivity()).resolveResult(requestCode, resultCode, data, clazz, waitingRequestCode);
        }

        return null;
    }

    protected final <T extends Serializable> T getSerializable(Class<? extends T> clazz) {
        String key = clazz.getSimpleName();
        if (getArguments() != null && getArguments().containsKey(key)) {
            return clazz.cast(getArguments().getSerializable(key));
        }

        return null;
    }
}
