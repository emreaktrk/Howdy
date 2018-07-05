package istanbul.codify.muudy.ui.postdetail;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.subjects.PublishSubject;
import istanbul.codify.muudy.BuildConfig;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.account.AccountUtils;
import istanbul.codify.muudy.exception.ImplementationMissingException;
import istanbul.codify.muudy.helper.utils.InflaterUtils;
import istanbul.codify.muudy.model.Badge;
import istanbul.codify.muudy.model.Comment;
import istanbul.codify.muudy.model.More;
import istanbul.codify.muudy.model.Post;
import istanbul.codify.muudy.model.PostMediaType;
import istanbul.codify.muudy.model.User;
import istanbul.codify.muudy.model.zipper.Like;
import istanbul.codify.muudy.model.zipper.PostDetail;
import istanbul.codify.muudy.ui.home.PostAdapter;
import istanbul.codify.muudy.view.LikeButton;

/**
 * Created by egesert on 4.07.2018.
 */

public class PostDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Post mPost;
    private List<Comment> mComments;
    private List<Badge> mBadges;

    private final PublishSubject<Long> mUserSubject = PublishSubject.create();
    private PublishSubject<Post> mPostSubject = PublishSubject.create();
    private PublishSubject<Like> mLikeSubject = PublishSubject.create();
    private PublishSubject<Post> mImageSubject = PublishSubject.create();
    private PublishSubject<Post> mVideoSubject = PublishSubject.create();
    private PublishSubject<Post> mAvatarSubject = PublishSubject.create();
    private PublishSubject<Post> mMuudySubject = PublishSubject.create();
    private PublishSubject<Post> mDeleteSubject = PublishSubject.create();
    private PublishSubject<Post> mLikerSubject = PublishSubject.create();

    public PostDetailAdapter(Post post, List<Comment> comments, List<Badge> badges) {
        mPost = post;
        mComments = comments;
        mBadges = badges;

    }




    private boolean isCommentCell(int position){
        if(position - mBadges.size() - 1 >= 0){
            return true;
        }else{
            return false;
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType) {
            case PostDetailAdapter.NoneHolder.TYPE:
                PostMediaType mediaType = mPost.post_media_type;
                switch (mediaType) {
                    case NONE:
                        return new NoneHolder(InflaterUtils.cell(R.layout.cell_post_none, parent));
                    case IMAGE:
                        return new ImageHolder(InflaterUtils.cell(R.layout.cell_post_image, parent));
                    case VIDEO:
                        return new VideoHolder(InflaterUtils.cell(R.layout.cell_post_video, parent));
                    default:
                        throw new ImplementationMissingException();
                }

            case CommentHolder.TYPE:
                return new CommentHolder(InflaterUtils.cell(R.layout.cell_comment, parent));

            case BadgeHolder.TYPE:
                return new BadgeHolder(InflaterUtils.cell(R.layout.cell_badge, parent));
            default:
                return null;

        }

    }

        @Override
        public void onBindViewHolder (@NonNull RecyclerView.ViewHolder mHolder,int position){

            if (mHolder instanceof CommentHolder) {
                CommentHolder holder = (CommentHolder) mHolder;
                Comment comment = mComments.get(position - mBadges.size() - 1);

                holder.mText.setText(comment.postcomment_text);
                holder.mDate.setText(comment.humanDate);
                SpannableStringBuilder str = new SpannableStringBuilder(comment.commenterUser.username);
                str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, comment.commenterUser.username.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.mUsername.setText(str);
                Picasso
                        .with(holder.mAvatar.getContext())
                        .load(BuildConfig.URL + comment.commenterUser.imgpath1)
                        .placeholder(R.drawable.ic_avatar)
                        .into(holder.mAvatar);
            } else if (mHolder instanceof NoneHolder) {
                Post post = mPost;

                NoneHolder none = (NoneHolder) mHolder;
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

                SpannableStringBuilder str = new SpannableStringBuilder(post.post_text);
                str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, post.username.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                none.mMessage.setText(str);

                if (mHolder instanceof MediaHolder) {

                    MediaHolder media = (MediaHolder) mHolder;
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

            } else if (mHolder instanceof BadgeHolder) {
                BadgeHolder holder = (BadgeHolder) mHolder;
                Badge badge = mBadges.get(position - 1);

                holder.mText.setText(badge.r_text);
                Picasso
                        .with(holder.mImage.getContext())
                        .load(BuildConfig.URL + badge.r_img)
                        .into(holder.mImage);
            }

        }

        @Override
        public int getItemCount () {
            return 1 + mComments.size() + mBadges.size();
        }

        @Override
        public int getItemViewType ( int position){

            if(position == 0){
                return NoneHolder.TYPE;
            }else{
                if (isCommentCell(position)){
                    return CommentHolder.TYPE;
                }else{
                    return BadgeHolder.TYPE;
                }
            }

        }


        class CommentHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public static final int TYPE = 278;

            private CircleImageView mAvatar;
            private AppCompatTextView mUsername;
            private AppCompatTextView mText;
            private AppCompatTextView mDate;

            public CommentHolder(View itemView) {
                super(itemView);

                mAvatar = itemView.findViewById(R.id.comment_avatar);
                mUsername = itemView.findViewById(R.id.comment_username);
                mText = itemView.findViewById(R.id.comment_text);
                mDate = itemView.findViewById(R.id.comment_date);

                mAvatar.setOnClickListener(this);

            }

            @Override
            public void onClick(View v) {
                Long userId = mComments.get(getAdapterPosition() - mBadges.size() - 1).postcomment_commenterid;//TODO pozisyon çek
                mUserSubject.onNext(userId);
            }
        }


        class BadgeHolder extends RecyclerView.ViewHolder {

            private static final int TYPE = 277;

            private AppCompatImageView mImage;
            private AppCompatTextView mText;

            public BadgeHolder(View itemView) {
                super(itemView);

                mImage = itemView.findViewById(R.id.badge_image);
                mText = itemView.findViewById(R.id.badge_text);
            }
        }


        public PublishSubject<Post> muudyClicks () {
            return mMuudySubject;
        }

        public PublishSubject<Post> deleteClicks () {
            return mDeleteSubject;
        }

        public PublishSubject<Post> likeCountClick () {
            return mLikerSubject;
        }



    public PublishSubject<Like> likeClicks() {
        return mLikeSubject;
    }

    public PublishSubject<Post> imageClicks() {
        return mImageSubject;
    }

    public PublishSubject<Long> commentAvatarClick() {
        return mUserSubject;
    }

    public PublishSubject<Post> videoClicks() {
        return mVideoSubject;
    }

    public PublishSubject<Post> avatarClicks() {
        return mAvatarSubject;
    }

        class NoneHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, LikeButton.OnLikeClickListener {

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
                mImage.setOnClickListener(this);

                mLike = itemView.findViewById(R.id.post_like);
                mLike.setOnLikeClickListener(this);


                mComment = itemView.findViewById(R.id.post_comment);
                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
            }

            @Override
            public void onClick(View view) {
                Post post = mPost;
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
                Post post = mPost;
                Like like = new Like(post, isLiked);
                mLikeSubject.onNext(like);
            }

            @Override
            public void onLikeTextClicked() {
                Post post = mPost;
                mLikerSubject.onNext(post);
            }

            @Override
            public boolean onLongClick(View view) {
                Post post = mPost;
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
                Post post = mPost;
                mImageSubject.onNext(post);
            }
        }

        class VideoHolder extends MediaHolder {

            VideoHolder(View itemView) {
                super(itemView);
            }

            @Override
            void onMediaClicked() {
                Post post = mPost;
                mVideoSubject.onNext(post);
            }
        }
    }



