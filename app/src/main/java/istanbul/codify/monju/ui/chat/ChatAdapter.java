package istanbul.codify.monju.ui.chat;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import io.reactivex.subjects.PublishSubject;
import istanbul.codify.monju.BuildConfig;
import istanbul.codify.monju.R;
import istanbul.codify.monju.helper.transformation.RoundedCornersTransformation;
import istanbul.codify.monju.model.Chat;
import istanbul.codify.monju.ui.chat.decorator.IncomingChatDecorator;
import istanbul.codify.monju.ui.chat.decorator.OutgoingChatDecorator;

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

        if (holder instanceof TextHolder) {
            TextHolder view = (TextHolder) holder;
            view.mText.setText(chat.message_text);
            view.mDate.setText(chat.message_humanDate);

            if (chat.message_fromuserid == mUserId) {
                new OutgoingChatDecorator(view.mRoot, view.mDate, view.mText);
            } else {
                new IncomingChatDecorator(view.mRoot, view.mDate, view.mText);
            }
        } else if (holder instanceof ImageHolder) {
            ImageHolder view = (ImageHolder) holder;

            if (chat.message_fromuserid == mUserId) {
                new OutgoingChatDecorator(holder.getView());
            } else {
                new IncomingChatDecorator(holder.getView());
            }

            Transformation transformation = new RoundedCornersTransformation(SizeUtils.dp2px(8), 0);
            int size = SizeUtils.dp2px(172);

            Picasso
                    .with(view.itemView.getContext())
                    .load(BuildConfig.URL + chat.message_img_path)
                    .centerCrop()
                    .resize(size, size)
                    .transform(transformation)
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

    PublishSubject<Chat> imageClicks() {
        return mSubject;
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

    class TextHolder extends Holder {

        private static final int TYPE = 0;

        private AppCompatTextView mText;
        private AppCompatTextView mDate;
        private LinearLayout mRoot;


        TextHolder(View itemView) {
            super(itemView);

            mText = itemView.findViewById(R.id.chat_text);
            mDate = itemView.findViewById(R.id.date_text);
            mRoot = itemView.findViewById(R.id.chat_linear);
        }

        @Override
        View getView() {
            return mRoot;
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
