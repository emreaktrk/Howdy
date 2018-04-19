package istanbul.codify.muudy.ui.word;

import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.subjects.PublishSubject;
import istanbul.codify.muudy.BuildConfig;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.model.Word;

import java.util.ArrayList;
import java.util.List;

final class WordAdapter extends RecyclerView.Adapter<WordAdapter.Holder> {

    private final List<Word> mList;
    private final PublishSubject<Word> mPublish = PublishSubject.create();
    private List<Word> mFiltered;

    WordAdapter(@Nullable List<Word> list) {
        mList = list == null ? new ArrayList<>() : list;
        mFiltered = new ArrayList<>(mList);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cell = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_word, parent, false);
        return new Holder(cell);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Word word = mFiltered.get(position);

        Picasso
                .with(holder.itemView.getContext())
                .load(BuildConfig.URL + word.words_emoji_url)
                .into(holder.mImage);
        holder.mText.setText(word.words_word);

        holder.mCategory.setVisibility(TextUtils.isEmpty(word.word_top_category_text) ? View.GONE : View.VISIBLE);
        holder.mCategory.setText(word.word_top_category_text);
    }

    @Override
    public int getItemCount() {
        return mFiltered.size();
    }

    PublishSubject<Word> itemClicks() {
        return mPublish;
    }

    List<Word> getWords() {
        return mList;
    }

    void setFiltered(@Nullable List<Word> filtered) {
        this.mFiltered = filtered == null ? new ArrayList<>(mList) : filtered;
        notifyDataSetChanged();
    }


    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CircleImageView mImage;
        private AppCompatTextView mText;
        private AppCompatTextView mCategory;

        Holder(View itemView) {
            super(itemView);

            mImage = itemView.findViewById(R.id.word_image);
            mText = itemView.findViewById(R.id.word_text);
            mCategory = itemView.findViewById(R.id.word_category);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Word word = mFiltered.get(getAdapterPosition());
            mPublish.onNext(word);
        }
    }
}
