package com.codify.howdy.ui.category;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codify.howdy.R;
import com.codify.howdy.model.Word;

import java.util.ArrayList;

import io.reactivex.subjects.PublishSubject;

final class WordAdapter extends RecyclerView.Adapter<WordAdapter.Holder> {

    private ArrayList<Word> mList;
    private PublishSubject<Word> mPublish = PublishSubject.create();

    WordAdapter(ArrayList<Word> list) {
        this.mList = list;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cell = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_word, parent, false);
        return new Holder(cell);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    PublishSubject<Word> itemClicks() {
        return mPublish;
    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Holder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Word word = mList.get(getAdapterPosition());
            mPublish.onNext(word);
        }
    }
}
