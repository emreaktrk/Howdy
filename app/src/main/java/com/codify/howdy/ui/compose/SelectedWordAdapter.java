package com.codify.howdy.ui.compose;

import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codify.howdy.R;
import com.codify.howdy.model.Word;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.subjects.PublishSubject;

final class SelectedWordAdapter extends RecyclerView.Adapter<SelectedWordAdapter.Holder> {

    private final PublishSubject<Word> mPublish = PublishSubject.create();
    private List<Word> mList;

    SelectedWordAdapter(@Nullable List<Word> list) {
        mList = list == null ? new ArrayList<>() : list;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cell = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_selected_word, parent, false);
        return new Holder(cell);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Word word = mList.get(position);
        holder.mText.setText(word.words_word);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    PublishSubject<Word> removeClicks() {
        return mPublish;
    }

    public void notifyDataSetChanged(List<Word> list) {
        mList = list;
        notifyDataSetChanged();
    }


    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private AppCompatImageButton mRemove;
        private AppCompatTextView mText;

        Holder(View itemView) {
            super(itemView);

            mText = itemView.findViewById(R.id.selected_word_text);
            mRemove = itemView.findViewById(R.id.selected_word_remove);

            mRemove.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Word word = mList.get(getAdapterPosition());
            mPublish.onNext(word);
        }
    }
}
