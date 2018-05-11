package istanbul.codify.muudy.view;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.View;

public class FollowButton extends AppCompatButton implements View.OnClickListener {

    private OnFollowClickListener mFollowListener;
    private OnUnfollowClickListener mUnfollowListener;
    private OnRequestClickListener mRequestListener;
    private OnRequestCancelClickListener mRequestCancelListener;
    private State mState;

    public FollowButton(Context context) {
        super(context);

        init();
    }

    public FollowButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public FollowButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        setOnClickListener(this);
    }

    public FollowButton setFollowClickListener(OnFollowClickListener listener) {
        mFollowListener = listener;

        return this;
    }

    public FollowButton setUnfollowClickListener(OnUnfollowClickListener listener) {
        mUnfollowListener = listener;

        return this;
    }

    public FollowButton setRequestClickListener(OnRequestClickListener listener) {
        mRequestListener = listener;

        return this;
    }

    public FollowButton setRequestCancelClickListener(OnRequestCancelClickListener listener) {
        mRequestCancelListener = listener;

        return this;
    }

    public State getState() {
        return mState;
    }

    public void setState(State state) {
        mState = state;
        switch (state) {
            case FOLLOW:
                setText("Takip Et");
                return;
            case UNFOLLOW:
                setText("Takibi Bırak");
                return;
            case REQUEST:
                setText("Takip İsteği Gönder");
                return;
            case REQUEST_CANCEL:
                setText("Takip İsteğini İptal Et");
                return;
            default:
                throw new IllegalArgumentException("Not implemented");
        }
    }

    @Override
    public void onClick(View view) {
        if (mState == null) {
            return;
        }

        switch (mState) {
            case FOLLOW:
                if (mFollowListener != null) {
                    mFollowListener.onFollowClicked(this);
                }
                return;
            case UNFOLLOW:
                if (mUnfollowListener != null) {
                    mUnfollowListener.onUnfollowClicked(this);
                }
                return;
            case REQUEST:
                if (mRequestListener != null) {
                    mRequestListener.onRequestClicked(this);
                }
                return;
            case REQUEST_CANCEL:
                if (mRequestCancelListener != null) {
                    mRequestCancelListener.onRequestCancelClicked(this);
                }
                return;
            default:
                throw new IllegalArgumentException("Not implemented");
        }
    }

    public enum State {
        FOLLOW,
        UNFOLLOW,
        REQUEST,
        REQUEST_CANCEL
    }

    public interface OnFollowClickListener {
        void onFollowClicked(FollowButton compound);
    }

    public interface OnUnfollowClickListener {
        void onUnfollowClicked(FollowButton compound);
    }

    public interface OnRequestClickListener {
        void onRequestClicked(FollowButton compound);
    }

    public interface OnRequestCancelClickListener {
        void onRequestCancelClicked(FollowButton compound);
    }
}
