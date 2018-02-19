package com.codify.howdy.ui.messages;

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

final class UserMessagesAdapter extends RecyclerView.Adapter<UserMessagesAdapter.Holder> {

    private List<User> mList;

    UserMessagesAdapter(List<User> list) {
        this.mList = list;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cell = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_user_messages, parent, false);
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
        holder.mLastMessage.setText(user.namesurname);
        holder.mDate.setText(user.namesurname);
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

        private AppCompatTextView mUsername;
        private AppCompatTextView mLastMessage;
        private AppCompatTextView mDate;
        private AppCompatImageView mImage;

        Holder(View itemView) {
            super(itemView);

            mUsername = itemView.findViewById(R.id.user_messages_username);
            mLastMessage = itemView.findViewById(R.id.user_messages_last);
            mDate = itemView.findViewById(R.id.user_messages_date);
            mImage = itemView.findViewById(R.id.user_messages_image);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
