package com.codify.howdy.ui.chat;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codify.howdy.model.Chat;

import java.util.List;

final class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.Holder> {

    private List<Chat> mList;

    ChatAdapter(List<Chat> list) {
        mList = list;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int itemViewType) {
        View cell = LayoutInflater.from(parent.getContext()).inflate(0, parent, false);
        return new Holder(cell);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Chat chat = mList.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        private AppCompatTextView mText;
        private AppCompatImageView mAvatar;

        Holder(View itemView) {
            super(itemView);
        }
    }
}
