package istanbul.codify.monju;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;

import istanbul.codify.monju.model.User;


public abstract class MuudyBottomSheet extends BottomSheetDialogFragment {

    abstract protected @LayoutRes
    int getLayoutResId();

    public User mUser;

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

        User user = getSerializable(User.class);

        if (user != null) {
            mUser = user;
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
