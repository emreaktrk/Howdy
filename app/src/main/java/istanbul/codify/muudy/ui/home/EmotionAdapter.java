package istanbul.codify.muudy.ui.home;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.subjects.PublishSubject;
import istanbul.codify.muudy.BuildConfig;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.model.Emotion;

import java.util.ArrayList;
import java.util.List;

final class EmotionAdapter extends RecyclerView.Adapter<EmotionAdapter.Holder> {

    private final PublishSubject<Emotion> mPublish = PublishSubject.create();
    private List<Emotion> mList;

    EmotionAdapter(@Nullable List<Emotion> list) {
        mList = list == null ? new ArrayList<>() : list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View cell = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_emotion, parent, false);
        return new Holder(cell);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Emotion emotion = mList.get(position);

        holder.mText.setText(emotion.words_word);
        Picasso
                .with(holder.itemView.getContext())
                .load(BuildConfig.URL + emotion.words_emoji_url)
                .into(holder.mImage);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    PublishSubject<Emotion> itemClicks() {
        return mPublish;
    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CircleImageView mImage;
        private AppCompatTextView mText;

        Holder(View itemView) {
            super(itemView);

            mImage = itemView.findViewById(R.id.emotion_image);
            mText = itemView.findViewById(R.id.emotion_text);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            Emotion emotion = mList.get(getAdapterPosition());
            mPublish.onNext(emotion);
        }
    }
}
