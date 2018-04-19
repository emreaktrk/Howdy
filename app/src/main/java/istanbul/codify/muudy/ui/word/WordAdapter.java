package istanbul.codify.muudy.ui.word;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
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

final class WordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<Word> mList;
    private final PublishSubject<Word> mWordPublish = PublishSubject.create();
    private final PublishSubject<Object> mSuggestPublish = PublishSubject.create();
    private List<Word> mFiltered;
    private boolean mHasQuery;

    WordAdapter(@Nullable List<Word> list) {
        mList = list == null ? new ArrayList<>() : list;
        mFiltered = new ArrayList<>(mList);
    }

    @Override
    public int getItemViewType(int position) {
        return mFiltered.isEmpty() && mHasQuery ? Empty.TYPE : Row.TYPE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case Row.TYPE:
                View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_word, parent, false);
                return new Row(row);
            case Empty.TYPE:
                View empty = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_word_empty, parent, false);
                return new Empty(empty);
            default:
                throw new IllegalArgumentException("Unknown view type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof Row) {
            Row row = (Row) holder;

            Word word = mFiltered.get(position);

            Picasso
                    .with(holder.itemView.getContext())
                    .load(BuildConfig.URL + word.words_emoji_url)
                    .into(row.mImage);
            row.mText.setText(word.words_word);

            row.mCategory.setVisibility(TextUtils.isEmpty(word.word_top_category_text) ? View.GONE : View.VISIBLE);
            row.mCategory.setText(word.word_top_category_text);
        }
    }

    @Override
    public int getItemCount() {
        return mFiltered.isEmpty() && mHasQuery ? 1 : mFiltered.size();
    }

    PublishSubject<Word> itemClicks() {
        return mWordPublish;
    }

    PublishSubject<Object> suggestClicks() {
        return mSuggestPublish;
    }

    List<Word> getWords() {
        return mList;
    }

    void setFiltered(@Nullable List<Word> filtered, boolean hasQuery) {
        this.mFiltered = filtered == null ? new ArrayList<>(mList) : filtered;
        this.mHasQuery = hasQuery;
        notifyDataSetChanged();
    }


    class Row extends RecyclerView.ViewHolder implements View.OnClickListener {

        private static final int TYPE = 946;

        private CircleImageView mImage;
        private AppCompatTextView mText;
        private AppCompatTextView mCategory;

        Row(View itemView) {
            super(itemView);

            mImage = itemView.findViewById(R.id.word_image);
            mText = itemView.findViewById(R.id.word_text);
            mCategory = itemView.findViewById(R.id.word_category);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Word word = mFiltered.get(getAdapterPosition());
            mWordPublish.onNext(word);
        }
    }

    class Empty extends RecyclerView.ViewHolder implements View.OnClickListener {

        private static final int TYPE = 727;

        private AppCompatButton mEmpty;

        Empty(View itemView) {
            super(itemView);

            mEmpty = itemView.findViewById(R.id.word_empty);
            mEmpty.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mSuggestPublish.onNext(new Object());
        }
    }
}
