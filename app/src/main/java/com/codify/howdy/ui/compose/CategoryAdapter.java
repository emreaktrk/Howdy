package com.codify.howdy.ui.compose;

import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.blankj.utilcode.util.ScreenUtils;
import com.codify.howdy.BuildConfig;
import com.codify.howdy.R;
import com.codify.howdy.model.Category;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.subjects.PublishSubject;

import java.util.ArrayList;
import java.util.List;

final class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.Holder> {

    private final PublishSubject<Category> mPublish = PublishSubject.create();
    private final List<Category> mList;

    CategoryAdapter(@Nullable List<Category> list) {
        mList = list == null ? new ArrayList<>() : list;
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
                .load(BuildConfig.URL + category.words_top_category_icon)
                .into(holder.mImage);
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

        private CircleImageView mImage;
        private AppCompatTextView mText;

        Holder(View itemView) {
            super(itemView);

            mImage = itemView.findViewById(R.id.category_image);
            mText = itemView.findViewById(R.id.category_text);

            itemView.setOnClickListener(this);

            itemView.getLayoutParams().width = ScreenUtils.getScreenWidth() / 3;
        }

        @Override
        public void onClick(View view) {
            Category category = mList.get(getAdapterPosition());
            mPublish.onNext(category);
        }
    }
}
