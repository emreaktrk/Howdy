package com.codify.howdy.ui.messages;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codify.howdy.BuildConfig;
import com.codify.howdy.R;
import com.codify.howdy.model.UserMessage;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.reactivex.subjects.PublishSubject;

final class UserMessagesAdapter extends RecyclerView.Adapter<UserMessagesAdapter.Holder> {

    private PublishSubject<UserMessage> mPublish = PublishSubject.create();
    private List<UserMessage> mList;

    UserMessagesAdapter(List<UserMessage> list) {
        mList = list;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cell = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_user_messages, parent, false);
        return new Holder(cell);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        UserMessage message = mList.get(position);

        Picasso
                .with(holder.mImage.getContext())
                .load(BuildConfig.URL + message.message_touserid)
                .into(holder.mImage);
        holder.mUsername.setText(message.message_touserid + "");
        holder.mLastMessage.setText(message.message_text);
        holder.mDate.setText(message.message_isreaded + "");
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    PublishSubject<UserMessage> itemClicks() {
        return mPublish;
    }

    void notifyDataSetChanged(List<UserMessage> list) {
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
            UserMessage message = mList.get(getAdapterPosition());
            mPublish.onNext(message);
        }
    }
}
