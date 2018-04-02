package com.codify.howdy.ui.compose;

import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.codify.howdy.R;
import com.codify.howdy.model.Selectable;
import io.reactivex.subjects.PublishSubject;

import java.util.ArrayList;
import java.util.List;

final class SelectedAdapter extends RecyclerView.Adapter<SelectedAdapter.Holder> {

    private final PublishSubject<Selectable> mPublish = PublishSubject.create();
    private List<? extends Selectable> mList;

    SelectedAdapter(@Nullable List<? extends Selectable> list) {
        mList = list == null ? new ArrayList<>() : list;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cell = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_selected_word, parent, false);
        return new Holder(cell);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Selectable selectable = mList.get(position);
        holder.mText.setText(selectable.text());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    PublishSubject<Selectable> removeClicks() {
        return mPublish;
    }

    public void notifyDataSetChanged(List<? extends Selectable> list) {
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
            Selectable selectable = mList.get(getAdapterPosition());
            mPublish.onNext(selectable);
        }
    }
}
