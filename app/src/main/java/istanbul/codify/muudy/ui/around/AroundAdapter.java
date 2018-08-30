package istanbul.codify.muudy.ui.around;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.subjects.PublishSubject;
import istanbul.codify.muudy.BuildConfig;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.account.AccountUtils;
import istanbul.codify.muudy.exception.ImplementationMissingException;
import istanbul.codify.muudy.helper.utils.InflaterUtils;
import istanbul.codify.muudy.model.AroundUsers;
import istanbul.codify.muudy.model.Comment;
import istanbul.codify.muudy.model.Post;
import istanbul.codify.muudy.model.PostMediaType;
import istanbul.codify.muudy.model.User;
import istanbul.codify.muudy.model.zipper.Like;
import istanbul.codify.muudy.ui.postdetail.PostDetailAdapter;
import istanbul.codify.muudy.ui.userprofile.UserProfileActivity;
import istanbul.codify.muudy.utils.WordToSpan;
import istanbul.codify.muudy.view.AvatarView;
import istanbul.codify.muudy.view.LikeButton;

import java.util.ArrayList;
import java.util.List;

final class AroundAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private PublishSubject<ArrayList<User>> mMoreSubject = PublishSubject.create();
    private PublishSubject<User> mUserSubject = PublishSubject.create();
    private List<AroundUsers> mList;

    private Post mPost;

    AroundAdapter(List<AroundUsers> list,Post post) {
        mList = list == null ? new ArrayList<>() : list;
        mPost = post;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder  onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType) {
            case AroundAdapter.NoneHolder.TYPE:

                PostMediaType mediaType = mPost.post_media_type;
                switch (mediaType) {
                    case NONE:
                        return new AroundAdapter.NoneHolder(InflaterUtils.cell(R.layout.cell_post_none, parent));
                    case IMAGE:
                        return new AroundAdapter.ImageHolder(InflaterUtils.cell(R.layout.cell_post_image, parent));
                    case VIDEO:
                        return new AroundAdapter.VideoHolder(InflaterUtils.cell(R.layout.cell_post_video, parent));
                    default:
                        throw new ImplementationMissingException();
                }

            case AroundAdapter.AroundUserHolder.TYPE:
                return new AroundAdapter.AroundUserHolder(InflaterUtils.cell(R.layout.cell_around, parent));
            default:
                return null;

        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder  mHolder, int position) {


        if (mHolder instanceof AroundAdapter.AroundUserHolder) {
            AroundUsers around = mList.get(getItemPostion(position));
            AroundAdapter.AroundUserHolder holder= (AroundAdapter.AroundUserHolder) mHolder;
            holder.mTitle.setText(around.title);
            for (int i = 0; i < holder.mContainer.getChildCount(); i++) {
                AvatarView avatar = (AvatarView) holder.mContainer.getChildAt(i);
                if (around.users.size() <= i) {
                    avatar.setVisibility(View.GONE);
                } else {
                    avatar.setVisibility(View.VISIBLE);

                    User user = around.users.get(i);
                    avatar.setUser(user);
                }
            }
        } else if (mHolder instanceof AroundAdapter.NoneHolder) {
            Post post = mPost;

            AroundAdapter.NoneHolder none = (AroundAdapter.NoneHolder) mHolder;
            none.mMessage.setText(post.post_text);
            none.mLike.setLike(post.isliked);
            none.mLike.setText(post.post_likecount + "");
            none.mDate.setText(post.humanDate);
            none.mComment.setText(post.post_commentcount + "");
            Picasso
                    .with(mHolder.itemView.getContext())
                    .load(BuildConfig.URL + post.imgpath1)
                    .placeholder(R.drawable.ic_avatar)
                    .into(none.mImage);

            WordToSpan wordToSpan = new WordToSpan();
            wordToSpan.setColorMENTION(mHolder.itemView.getContext().getResources().getColor(R.color.blue));

            wordToSpan.setLink(post.post_text,none.mMessage, post.username);

            wordToSpan.setClickListener(new WordToSpan.ClickListener() {
                @Override
                public void onClick(String type, String text) {
                    Toast.makeText(none.itemView.getContext(),text,Toast.LENGTH_LONG).show();
                    if(type.equals("mention")){

                        String username = text.substring(1);
                        UserProfileActivity.start(username);

                    }
                }
            });

            if (mHolder instanceof AroundAdapter.MediaHolder) {

                AroundAdapter.MediaHolder media = (AroundAdapter.MediaHolder) mHolder;
                Picasso
                        .with(mHolder.itemView.getContext())
                        .load(BuildConfig.URL + post.post_mediapath)
                        .centerCrop()
                        .resize(180, 180)
                        .into(media.mMedia);
            } else {
                if (post.post_emoji != null) {
                    none.mEmotion.setVisibility(View.VISIBLE);
                    Picasso
                            .with(mHolder.itemView.getContext())
                            .load(BuildConfig.URL + post.post_emoji)
                            .into(none.mEmotion);
                } else {
                    none.mEmotion.setVisibility(View.GONE);
                }
            }

        }
    }

    @Override
    public int getItemCount() {
        return mList.size() + (mPost != null ? 1 : 0);

    }

    private int getItemPostion(int position){
        if(mPost != null){
            return position - 1;
        }else{
            return position;
        }
    }

    @Override
    public int getItemViewType ( int position){

        if(position == 0){
            if (mPost != null){
                return NoneHolder.TYPE;
            }else{
                return AroundUserHolder.TYPE;
            }
        }else{
            return AroundUserHolder.TYPE;
        }

    }

    PublishSubject<ArrayList<User>> moreClicks() {
        return mMoreSubject;
    }

    PublishSubject<User> userClicks() {
        return mUserSubject;
    }

    class AroundUserHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public static final int TYPE = 289;

        private LinearLayoutCompat mContainer;
        private AppCompatTextView mTitle;
        private AppCompatTextView mMore;

        AroundUserHolder(View itemView) {
            super(itemView);

            mTitle = itemView.findViewById(R.id.around_title);
            mMore = itemView.findViewById(R.id.around_more);
            mContainer = itemView.findViewById(R.id.around_container);

            mMore.setOnClickListener(this);
            for (int i = 0; i < mContainer.getChildCount(); i++) {
                View child = mContainer.getChildAt(i);
                if (child instanceof AvatarView) {
                    child.setTag(i);
                    child.setOnClickListener(this);
                }
            }
        }

        @Override
        public void onClick(View view) {
            AroundUsers around = mList.get(getItemPostion(getAdapterPosition()));

            if (view instanceof AvatarView) {
                int position = (int) view.getTag();
                User user = around.users.get(position);
                mUserSubject.onNext(user);
            } else {
                mMoreSubject.onNext(around.users);
            }
        }
    }


    class NoneHolder extends RecyclerView.ViewHolder {

        public static final int TYPE = 279;

        private AppCompatTextView mMessage;
        private CircleImageView mImage;
        private CircleImageView mEmotion;
        private LikeButton mLike;
        private AppCompatTextView mComment;
        private AppCompatTextView mDate;

        NoneHolder(View itemView) {
            super(itemView);

            mMessage = itemView.findViewById(R.id.post_message);
            mEmotion = itemView.findViewById(R.id.post_emotion);
            mDate = itemView.findViewById(R.id.post_date);

            mImage = itemView.findViewById(R.id.post_image);

            mLike = itemView.findViewById(R.id.post_like);

            mComment = itemView.findViewById(R.id.post_comment);

        }


    }


    abstract class MediaHolder extends AroundAdapter.NoneHolder {

        private AppCompatImageView mMedia;

        MediaHolder(View itemView) {
            super(itemView);

            mMedia = itemView.findViewById(R.id.post_media);

        }


        abstract void onMediaClicked();
    }


    class ImageHolder extends AroundAdapter.MediaHolder {

        ImageHolder(View itemView) {
            super(itemView);
        }

        @Override
        void onMediaClicked() {

        }
    }

    class VideoHolder extends AroundAdapter.MediaHolder {

        VideoHolder(View itemView) {
            super(itemView);
        }

        @Override
        void onMediaClicked() {

        }
    }

}
