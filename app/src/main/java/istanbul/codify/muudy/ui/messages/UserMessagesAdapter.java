package istanbul.codify.muudy.ui.messages;

import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.subjects.PublishSubject;
import istanbul.codify.muudy.BuildConfig;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.model.Result;
import istanbul.codify.muudy.model.UserMessage;

import java.util.ArrayList;
import java.util.List;

final class UserMessagesAdapter extends RecyclerView.Adapter<UserMessagesAdapter.Holder> {

    private PublishSubject<UserMessage> mPublish = PublishSubject.create();
    private List<UserMessage> mList;

    UserMessagesAdapter(@Nullable List<UserMessage> list) {
        mList = list == null ? new ArrayList<>() : list;
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
                .load(BuildConfig.URL + message.otherUser.imgpath1)
                .placeholder(R.drawable.ic_avatar)
                .into(holder.mImage);
        holder.mUsername.setText(message.otherUser.username);
        if (message.message_text.equals("")) {
            holder.mLastMessage.setText("\uD83D\uDCF7");
        }else {
            holder.mLastMessage.setText(message.message_text);
        }
        holder.mDate.setText(message.message_humandate);

        holder.mUsername.setTypeface(holder.mUsername.getTypeface(), message.message_isreaded == Result.TRUE ? Typeface.NORMAL : Typeface.BOLD);
        holder.mLastMessage.setTypeface(holder.mLastMessage.getTypeface(), message.message_isreaded == Result.TRUE ? Typeface.NORMAL : Typeface.BOLD);
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
        private CircleImageView mImage;

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
