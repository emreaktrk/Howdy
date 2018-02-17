package com.codify.howdy.ui.word;

import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codify.howdy.BuildConfig;
import com.codify.howdy.R;
import com.codify.howdy.model.Word;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.subjects.PublishSubject;

final class WordAdapter extends RecyclerView.Adapter<WordAdapter.Holder> {

    private final List<Word> mList;
    private final PublishSubject<Word> mPublish = PublishSubject.create();
    private List<Word> mFiltered;

    WordAdapter(List<Word> list) {
        this.mList = list;
        this.mFiltered = new ArrayList<>(list);
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

        private AppCompatImageView mImage;
        private AppCompatTextView mText;

        Holder(View itemView) {
            super(itemView);

            mImage = itemView.findViewById(R.id.word_image);
            mText = itemView.findViewById(R.id.word_text);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Word word = mFiltered.get(getAdapterPosition());
            mPublish.onNext(word);
        }
    }
}
