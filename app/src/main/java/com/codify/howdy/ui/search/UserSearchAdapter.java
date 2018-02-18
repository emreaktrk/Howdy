package com.codify.howdy.ui.search;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codify.howdy.BuildConfig;
import com.codify.howdy.R;
import com.codify.howdy.model.User;
import com.squareup.picasso.Picasso;

import java.util.List;

final class UserSearchAdapter extends RecyclerView.Adapter<UserSearchAdapter.Holder> {

    private List<User> mList;

    UserSearchAdapter(List<User> list) {
        this.mList = list;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cell = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_user_search, parent, false);
        return new Holder(cell);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        User user = mList.get(position);

        Picasso
                .with(holder.mImage.getContext())
                .load(BuildConfig.URL + user.imgpath)
                .into(holder.mImage);
        holder.mUsername.setText(user.username);
        holder.mNameSurname.setText(user.namesurname);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    void notifyDataSetChanged(List<User> list) {
        mList = list;
        notifyDataSetChanged();
    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private AppCompatImageView mImage;
        private AppCompatTextView mUsername;
        private AppCompatTextView mNameSurname;

        Holder(View itemView) {
            super(itemView);

            mImage = itemView.findViewById(R.id.user_search_image);
            mUsername = itemView.findViewById(R.id.user_search_username);
            mNameSurname = itemView.findViewById(R.id.user_search_namesurname);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
