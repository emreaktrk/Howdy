package com.codify.howdy.ui.compose;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codify.howdy.BuildConfig;
import com.codify.howdy.R;
import com.codify.howdy.model.Category;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.reactivex.subjects.PublishSubject;

final class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.Holder> {

    private final PublishSubject<Category> mPublish = PublishSubject.create();
    private final List<Category> mList;

    CategoryAdapter(List<Category> list) {
        this.mList = list;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cell = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_category, parent, false);
        return new Holder(cell);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Category category = mList.get(position);

        Picasso
                .with(holder.mImage.getContext())
                .load(BuildConfig.API_URL + category.words_top_category_icon);
        holder.mText.setText(category.words_top_category_text);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    PublishSubject<Category> itemClicks() {
        return mPublish;
    }


    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private AppCompatImageView mImage;
        private AppCompatTextView mText;

        Holder(View itemView) {
            super(itemView);

            mImage = itemView.findViewById(R.id.category_image);
            mText = itemView.findViewById(R.id.category_text);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Category category = mList.get(getAdapterPosition());
            mPublish.onNext(category);
        }
    }
}
