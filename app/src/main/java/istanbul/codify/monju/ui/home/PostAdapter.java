package istanbul.codify.monju.ui.home;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;

import android.view.View;
import android.view.ViewGroup;


import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.subjects.PublishSubject;

import istanbul.codify.monju.R;
import istanbul.codify.monju.account.AccountUtils;
import istanbul.codify.monju.exception.ImplementationMissingException;
import istanbul.codify.monju.helper.utils.InflaterUtils;
import istanbul.codify.monju.model.*;
import istanbul.codify.monju.model.zipper.Like;
import istanbul.codify.monju.ui.postdetail.PostDetailActivity;
import istanbul.codify.monju.ui.userprofile.UserProfileActivity;
import istanbul.codify.monju.utils.PicassoHelper;
import istanbul.codify.monju.utils.WordToSpan;
import istanbul.codify.monju.view.LikeButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public final class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @IntRange(from = 0, to = Integer.MAX_VALUE)
    public static final int RECOMMENDED_USER_POSITION = 14;

    private PublishSubject<Post> mPostSubject = PublishSubject.create();
    private PublishSubject<Like> mLikeSubject = PublishSubject.create();
    private PublishSubject<Post> mImageSubject = PublishSubject.create();
    private PublishSubject<Post> mVideoSubject = PublishSubject.create();
    private PublishSubject<Post> mAvatarSubject = PublishSubject.create();
    private PublishSubject<Post> mMuudySubject = PublishSubject.create();
    private PublishSubject<Post> mDeleteSubject = PublishSubject.create();
    private PublishSubject<More> mMoreSubject = PublishSubject.create();
    private PublishSubject<Post> mLikerSubject = PublishSubject.create();
    private List<Post> mPosts;
    private List<User> mUsersList;
    private RecommendedUsersAdapter mUsers;
    private More mMore = new More(0, true);

    public PostAdapter(@Nullable List<Post> posts) {
        mPosts = posts == null ? new ArrayList<>() : posts;
    }

    public PostAdapter(@Nullable List<Post> posts, @Nullable List<User> users) {
        mPosts = posts == null ? new ArrayList<>() : posts;
        mUsersList = users;

        if (users != null && !users.isEmpty()) {
            mUsers = new RecommendedUsersAdapter(users);
        }
    }

    public PostAdapter(Post post) {
        mPosts = Collections.singletonList(post);
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
            none.mLike.setText(post.post_likecount + "");
            none.mDate.setText(post.humanDate);
            none.mComment.setText(post.post_commentcount + "");
            new PicassoHelper(none.mImage.getContext(),none.mImage,post.imgpath1);
           /* Picasso
                    .with(holder.itemView.getContext())
                    .load(BuildConfig.URL + post.imgpath1)
                    .placeholder(R.drawable.ic_avatar)
                    .into(none.mImage);
*/



            WordToSpan wordToSpan = new WordToSpan();
            wordToSpan.setColorMENTION(holder.itemView.getContext().getResources().getColor(R.color.blue));
            wordToSpan.setColorCUSTOM(holder.itemView.getContext().getResources().getColor(R.color.black));

            wordToSpan.setLink(post.post_text,none.mMessage, post.username);

            wordToSpan.setClickListener(new WordToSpan.ClickListener() {
                @Override
                public void onClick(String type, String text) {
                  //  Toast.makeText(none.itemView.getContext(),text,Toast.LENGTH_LONG).show();
                    if(type.equals("mention")){

                        String username = text.substring(1);
                        UserProfileActivity.start(username);

                    }else{
                        PostDetailActivity.start(post.idpost);
                    }
                }
            });

        /*    SpannableStringBuilder str = new SpannableStringBuilder(post.post_text);
            str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, post.username.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View textView) {
                    Toast.makeText(none.itemView.getContext(),"username tıklandı",Toast.LENGTH_LONG).show();
                }
                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(false);
                    ds.setColor(none.itemView.getContext().getResources().getColor(R.color.black));



                }
            };

            str.setSpan(clickableSpan,0,post.username.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            str.setSpan(new ForegroundColorSpan(Color.BLACK), 0, post.username.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


            none.mMessage.setText(str);
*/


            if (holder instanceof MediaHolder) {
                MediaHolder media = (MediaHolder) holder;
                new PicassoHelper(holder.itemView.getContext(),media.mMedia,post.post_mediapath);
             /*   Picasso
                        .with(holder.itemView.getContext())
                        .load(BuildConfig.URL + post.post_mediapath)
                        .centerCrop()
                        .resize(180, 180)
                        .into(media.mMedia);*/
            } else {
                if (post.post_emoji != null) {
                    none.mEmotion.setVisibility(View.VISIBLE);
                    new PicassoHelper(holder.itemView.getContext(),none.mEmotion,post.post_emoji);
                    /*
                    Picasso
                            .with(holder.itemView.getContext())
                            .load(BuildConfig.URL + post.post_emoji)
                            .into(none.mEmotion);*/
                } else {
                    none.mEmotion.setVisibility(View.GONE);
                }
            }


        }

        if (holder instanceof RecommendedHolder) {
            RecommendedHolder recommended = (RecommendedHolder) holder;
            recommended.mRecycler.setAdapter(mUsers);
        }
    }


    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        if ((mMore != null && mMore.enable) && getItemCount() - holder.getAdapterPosition() < 5) {
            mMoreSubject.onNext(mMore);
            mMore = null;
        }
    }



    public More getMore() {
        return mMore;
    }

    @Override
    public int getItemCount() {
        if (mPosts.size() > 0) {
            if (mUsersList != null) {
                if (mUsersList.size() > 0) {
                    return mPosts.size() + 1;
                } else {
                    return mPosts.size();
                }
            } else {
                return mPosts.size();
            }
        } else {
            return 0;
        }
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
                if (position == mPosts.size()) {
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

    public PublishSubject<More> morePages() {
        return mMoreSubject;
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

    public PublishSubject<Post> likeCountClick() {
        return mLikerSubject;
    }

    public void add(ArrayList<Post> posts, More more) {
        mPosts.addAll(posts);
        mMore = more;
    }


    class NoneHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, LikeButton.OnLikeClickListener {

        public static final int TYPE = 279;

        private AppCompatTextView mMessage;
        private CircleImageView mImage;
        private AppCompatImageView mEmotion;
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
        public void onLikeTextClicked() {
            Post post = getItem(getAdapterPosition());
            mLikerSubject.onNext(post);
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
