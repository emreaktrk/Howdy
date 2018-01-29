package com.codify.howdy.ui.compose;

import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codify.howdy.R;
import com.codify.howdy.model.Emotion;

import java.util.ArrayList;

import io.reactivex.subjects.PublishSubject;

class EmotionAdapter extends RecyclerView.Adapter<EmotionAdapter.Holder> {

    private PublishSubject<Emotion> mPublish = PublishSubject.create();
    private ArrayList<Emotion> mList;

    EmotionAdapter(ArrayList<Emotion> list) {
        this.mList = list;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cell = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_emotion, parent, false);
        return new Holder(cell);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Emotion emotion = mList.get(position);
        holder.mText.setText(emotion.post_emoji_word);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    PublishSubject<Emotion> itemClicks() {
        return mPublish;
    }


    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private AppCompatImageButton mRemove;
        private AppCompatTextView mText;

        Holder(View itemView) {
            super(itemView);

            mText = itemView.findViewById(R.id.emotion_text);
            mRemove = itemView.findViewById(R.id.emotion_remove);

            mRemove.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Emotion emotion = mList.get(getAdapterPosition());
            mPublish.onNext(emotion);
        }
    }
}
