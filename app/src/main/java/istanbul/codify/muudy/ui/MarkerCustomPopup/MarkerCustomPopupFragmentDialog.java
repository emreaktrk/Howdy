package istanbul.codify.muudy.ui.MarkerCustomPopup;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import istanbul.codify.muudy.BuildConfig;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.model.EmojiLocation;


public class MarkerCustomPopupFragmentDialog extends DialogFragment {

    public MarkerCustomPopupFragmentDialog() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public MarkerCustomPopupFragmentDialog(EmojiLocation emojiLocation) {
        // Required empty public constructor
        this.emojiLocation = emojiLocation;
    }

    EmojiLocation emojiLocation;

    View mRootView;
    TextView txt;
    ImageView img;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_marker_custom_popup_fragment_dialog, container, false);

        txt = mRootView.findViewById(R.id.txt);
        img = mRootView.findViewById(R.id.img);

        Picasso
                .with(getContext())
                .load(BuildConfig.URL + emojiLocation.post_emoji)
                .placeholder(R.drawable.ic_avatar)
                .into(img);

        txt.setText(emojiLocation.post_emoji_word + "");


        return mRootView;
    }

}
