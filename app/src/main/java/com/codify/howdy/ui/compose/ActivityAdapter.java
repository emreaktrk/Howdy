package com.codify.howdy.ui.compose;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codify.howdy.R;
import com.codify.howdy.model.Word;

import java.util.List;

import io.reactivex.subjects.PublishSubject;

final class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.Holder> {

    private final PublishSubject<Word> mPublish = PublishSubject.create();
    private List<Word> mList;

    ActivityAdapter(List<Word> list) {
        mList = list;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cell = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_activity, parent, false);
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

    PublishSubject<Word> itemClicks() {
        return mPublish;
    }

    public void notifyDataSetChanged(List<Word> list) {
        mList = list;
        notifyDataSetChanged();
    }


    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private AppCompatImageView mImage;
        private AppCompatTextView mText;

        Holder(View itemView) {
            super(itemView);

            mText = itemView.findViewById(R.id.activity_text);
            mImage = itemView.findViewById(R.id.activity_image);
        }

        @Override
        public void onClick(View view) {
            Word word = mList.get(getAdapterPosition());
            mPublish.onNext(word);
        }
    }
}
