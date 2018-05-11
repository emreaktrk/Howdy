package istanbul.codify.muudy.ui.home;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.subjects.PublishSubject;
import istanbul.codify.muudy.BuildConfig;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.account.AccountUtils;
import istanbul.codify.muudy.exception.ImplementationMissingException;
import istanbul.codify.muudy.helper.utils.InflaterUtils;
import istanbul.codify.muudy.model.Follow;
import istanbul.codify.muudy.model.Post;
import istanbul.codify.muudy.model.PostMediaType;
import istanbul.codify.muudy.model.User;
import istanbul.codify.muudy.model.zipper.Like;
import istanbul.codify.muudy.view.LikeButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @IntRange(from = 0, to = Integer.MAX_VALUE)
    private static final int RECOMMENDED_USER_POSITION = 14;

    private PublishSubject<Post> mPostSubject = PublishSubject.create();
    private PublishSubject<Like> mLikeSubject = PublishSubject.create();
    private PublishSubject<Post> mImageSubject = PublishSubject.create();
    private PublishSubject<Post> mVideoSubject = PublishSubject.create();
    private PublishSubject<Post> mAvatarSubject = PublishSubject.create();
    private PublishSubject<Post> mMuudySubject = PublishSubject.create();
    private PublishSubject<Post> mDeleteSubject = PublishSubject.create();
    private List<Post> mPosts;
    private RecommendedUsersAdapter mUsers;
    private int mCommentVisibility = View.VISIBLE;

    public PostAdapter(@Nullable List<Post> posts) {
        mPosts = posts == null ? new ArrayList<>() : posts;
    }

    public PostAdapter(@Nullable List<Post> posts, @Nullable List<User> users) {
        mPosts = posts == null ? new ArrayList<>() : posts;

        if (users != null && !users.isEmpty()) {
            mUsers = new RecommendedUsersAdapter(users);
        }
    }

    public PostAdapter(Post post) {
        mPosts = Collections.singletonList(post);
        mCommentVisibility = View.GONE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PostMediaType mediaType = PostMediaType.values()[viewType];
        switch (mediaType) {
            case NONE:
                return new NoneHolder(InflaterUtils.cell(R.layout.cell_post_none, parent));
            case IMAGE:
                return new ImageHolder(InflaterUtils.cell(R.layout.cell_post_image, parent));
            case VIDEO:
                return new VideoHolder(InflaterUtils.cell(R.layout.cell_post_video, parent));
            case RECOMMENDED_USERS:
                return new RecommendedHolder(InflaterUtils.cell(R.layout.cell_post_recommended_user, parent));
            default:
                throw new ImplementationMissingException();
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NoneHolder) {
            Post post = getItem(position);

            NoneHolder none = (NoneHolder) holder;
            none.mMessage.setText(post.post_text);
            none.mLike.setLike(post.isliked);
            none.mDate.setText(post.humanDate);
            none.mLike.setText(post.post_likecount + "");
            none.mComment.setText(post.post_commentcount + "");
            Picasso
                    .with(holder.itemView.getContext())
                    .load(BuildConfig.URL + post.imgpath1)
                    .placeholder(R.drawable.ic_avatar)
                    .into(none.mImage);
            Picasso
                    .with(holder.itemView.getContext())
                    .load(BuildConfig.URL + post.post_emoji)
                    .into(none.mEmotion);

            if (holder instanceof MediaHolder) {
                MediaHolder media = (MediaHolder) holder;
                Picasso
                        .with(holder.itemView.getContext())
                        .load(BuildConfig.URL + post.post_mediapath)
                        .centerCrop()
                        .resize(180, 180)
                        .into(media.mMedia);
            }
        }

        if (holder instanceof RecommendedHolder) {
            RecommendedHolder recommended = (RecommendedHolder) holder;
            recommended.mRecycler.setAdapter(mUsers);
        }
    }

    @Override
    public int getItemCount() {
        return mUsers == null ? mPosts.size() : mPosts.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (mUsers == null) {
            Post post = mPosts.get(position);
            return post.post_media_type == null ? PostMediaType.NONE.ordinal() : post.post_media_type.ordinal();
        } else {
            if (getItemCount() > RECOMMENDED_USER_POSITION) {
                if (position == RECOMMENDED_USER_POSITION) {
                    return PostMediaType.RECOMMENDED_USERS.ordinal();
                }
            } else {
                if (position == getItemCount()) {
                    return PostMediaType.RECOMMENDED_USERS.ordinal();
                }
            }

            Post post = mPosts.get(position > 15 ? position - 1 : position);
            return post.post_media_type == null ? PostMediaType.NONE.ordinal() : post.post_media_type.ordinal();
        }
    }

    private Post getItem(int position) {
        if (mUsers == null) {
            return mPosts.get(position);
        } else {
            if (getItemCount() > RECOMMENDED_USER_POSITION && position > RECOMMENDED_USER_POSITION) {
                return mPosts.get(position - 1);
            } else {
                return mPosts.get(position);
            }
        }
    }

    public PublishSubject<Post> itemClicks() {
        return mPostSubject;
    }

    public PublishSubject<Like> likeClicks() {
        return mLikeSubject;
    }

    public PublishSubject<Post> imageClicks() {
        return mImageSubject;
    }

    public PublishSubject<Post> videoClicks() {
        return mVideoSubject;
    }

    public PublishSubject<Post> avatarClicks() {
        return mAvatarSubject;
    }

    public PublishSubject<Follow> followClicks() {
        if (mUsers == null) {
            return PublishSubject.create();
        }

        return mUsers.followClicks();
    }

    public PublishSubject<User> userClicks() {
        if (mUsers == null) {
            return PublishSubject.create();
        }

        return mUsers.userClicks();
    }

    public PublishSubject<Post> muudyClicks() {
        return mMuudySubject;
    }

    public PublishSubject<Post> deleteClicks() {
        return mDeleteSubject;
    }

    class NoneHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, LikeButton.OnLikeClickListener {

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
            mImage.setOnClickListener(this);

            mLike = itemView.findViewById(R.id.post_like);
            mLike.setOnLikeClickListener(this);


            mComment = itemView.findViewById(R.id.post_comment);
            mComment.setVisibility(mCommentVisibility);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Post post = getItem(getAdapterPosition());
            switch (view.getId()) {
                case R.id.post_image:
                    mAvatarSubject.onNext(post);
                    return;
                default:
                    mPostSubject.onNext(post);
            }
        }

        @Override
        public void onLike(boolean isLiked) {
            Post post = getItem(getAdapterPosition());
            Like like = new Like(post, isLiked);
            mLikeSubject.onNext(like);
        }

        @Override
        public boolean onLongClick(View view) {
            Post post = getItem(getAdapterPosition());
            boolean own = AccountUtils.own(view.getContext(), post.iduser);
            if (own) {
                mDeleteSubject.onNext(post);
            } else {
                mMuudySubject.onNext(post);
            }

            return true;
        }
    }

    abstract class MediaHolder extends NoneHolder {

        private AppCompatImageView mMedia;

        MediaHolder(View itemView) {
            super(itemView);

            mMedia = itemView.findViewById(R.id.post_media);
            mMedia.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.post_media:
                    onMediaClicked();
                    return;
                default:
                    super.onClick(view);
            }
        }

        abstract void onMediaClicked();
    }


    class ImageHolder extends MediaHolder {

        ImageHolder(View itemView) {
            super(itemView);
        }

        @Override
        void onMediaClicked() {
            Post post = getItem(getAdapterPosition());
            mImageSubject.onNext(post);
        }
    }

    class VideoHolder extends MediaHolder {

        VideoHolder(View itemView) {
            super(itemView);
        }

        @Override
        void onMediaClicked() {
            Post post = getItem(getAdapterPosition());
            mVideoSubject.onNext(post);
        }
    }

    class RecommendedHolder extends RecyclerView.ViewHolder {

        private RecyclerView mRecycler;

        RecommendedHolder(View itemView) {
            super(itemView);

            mRecycler = itemView.findViewById(R.id.post_recommended_user_recycler);
        }
    }
}
