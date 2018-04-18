package istanbul.codify.muudy.ui.chat;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.squareup.picasso.Picasso;
import io.reactivex.subjects.PublishSubject;
import istanbul.codify.muudy.BuildConfig;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.helper.transformation.RoundedCornersTransformation;
import istanbul.codify.muudy.model.Chat;
import istanbul.codify.muudy.ui.chat.decorator.IncomingChatDecorator;
import istanbul.codify.muudy.ui.chat.decorator.OutgoingChatDecorator;

import java.util.ArrayList;
import java.util.List;


final class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.Holder> {

    private PublishSubject<Chat> mSubject = PublishSubject.create();
    private List<Chat> mList;
    private long mUserId;

    ChatAdapter(@Nullable List<Chat> list, long userId) {
        mList = list == null ? new ArrayList<>() : list;
        mUserId = userId;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int itemViewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (itemViewType) {
            case TextHolder.TYPE:
                return new TextHolder(
                        inflater.inflate(R.layout.cell_chat_text, parent, false)
                );
            case ImageHolder.TYPE:
                return new ImageHolder(
                        inflater.inflate(R.layout.cell_chat_image, parent, false)
                );
            default:
                throw new IllegalArgumentException("Unknown view type : " + itemViewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Chat chat = mList.get(position);
        if (chat.message_fromuserid == mUserId) {
            new OutgoingChatDecorator(holder.getView());
        } else {
            new IncomingChatDecorator(holder.getView());
        }

        if (holder instanceof TextHolder) {
            TextHolder view = (TextHolder) holder;
            view.mText.setText(chat.message_text);
        } else if (holder instanceof ImageHolder) {
            ImageHolder view = (ImageHolder) holder;

            Picasso
                    .with(view.itemView.getContext())
                    .load(BuildConfig.URL + chat.message_img_path)
                    .transform(new RoundedCornersTransformation(SizeUtils.dp2px(8), 0))
                    .into(view.mImage);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Chat chat = mList.get(position);
        return StringUtils.isEmpty(chat.message_img_path) ? TextHolder.TYPE : ImageHolder.TYPE;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    abstract class Holder extends RecyclerView.ViewHolder {

        Holder(View itemView) {
            super(itemView);

            if (!(itemView instanceof FrameLayout)) {
                throw new IllegalArgumentException("Root view must be FrameLayout");
            }
        }

        abstract View getView();
    }

    PublishSubject<Chat> imageClicks() {
        return mSubject;
    }

    class TextHolder extends Holder {

        private static final int TYPE = 0;

        private AppCompatTextView mText;

        TextHolder(View itemView) {
            super(itemView);

            mText = itemView.findViewById(R.id.chat_text);
        }

        @Override
        View getView() {
            return mText;
        }
    }

    class ImageHolder extends Holder implements View.OnClickListener {

        private static final int TYPE = 1;

        private AppCompatImageView mImage;

        ImageHolder(View itemView) {
            super(itemView);

            mImage = itemView.findViewById(R.id.chat_image);
            mImage.setOnClickListener(this);
        }

        @Override
        View getView() {
            return mImage;
        }

        @Override
        public void onClick(View view) {
            Chat chat = mList.get(getAdapterPosition());
            mSubject.onNext(chat);
        }
    }
}
