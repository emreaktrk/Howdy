package istanbul.codify.muudy.helper;

import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import istanbul.codify.muudy.account.AccountUtils;
import istanbul.codify.muudy.logcat.Logcat;
import istanbul.codify.muudy.model.Post;
import istanbul.codify.muudy.model.UserMessage;
import istanbul.codify.muudy.model.Wall;
import istanbul.codify.muudy.model.zipper.PostDetail;
import istanbul.codify.muudy.utils.AndroidUtils;

import java.util.List;

import static istanbul.codify.muudy.ui.home.PostAdapter.RECOMMENDED_USER_POSITION;


public class RecyclerViewHelper {

    OnSwipeDialogCallback onSwipeDialogCallbackk;
    boolean isItemViewSwipeEnable = false;
    ItemTouchHelper itemTouchHelper;

    public void setItemViewSwipeEnable(boolean itemViewSwipeEnable) {
        isItemViewSwipeEnable = itemViewSwipeEnable;
    }

    public void setOnSwipeDialogCallback(OnSwipeDialogCallback onSwipeDialogCallback) {
        this.onSwipeDialogCallbackk = onSwipeDialogCallback;
    }

    public int getItemCount(Wall wall) {

        if (wall.posts.size() > 0) {
            if (wall.recomendedUsers != null) {
                if (wall.recomendedUsers.size() > 0) {
                    return wall.posts.size() + 1;
                } else {
                    return wall.posts.size();
                }
            } else {
                return wall.posts.size();
            }
        } else {
            return 0;
        }
    }

    public int getPostPosition(Wall wall, int position) {

        if (wall.recomendedUsers == null) {
            return position;
        } else {
            if (getItemCount(wall) > RECOMMENDED_USER_POSITION) {
                if (position > RECOMMENDED_USER_POSITION) {
                    return position - 1;
                } else {
                    return position;
                }
            } else {
                return position;
            }
        }

    }

    public void initSwipe(final RecyclerView recyclerView, final Context context, Wall wall, SwipeRefreshLayout swipeRefreshLayout, OnSwipeDialogCallback onSwipeDialogCallback) {
        setOnSwipeDialogCallback(onSwipeDialogCallback);
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public boolean isItemViewSwipeEnabled() {

                return isItemViewSwipeEnable;
            }


            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

                int position = viewHolder.getAdapterPosition();
                int swipeFlags = 0;
                if (wall.recomendedUsers == null) {
                    Post post = wall.posts.get(position);
                    if (post.post_userid == AccountUtils.me(context).iduser) {
                        swipeFlags = ItemTouchHelper.LEFT;

                    } else {
                        swipeFlags = ItemTouchHelper.RIGHT;

                    }
                } else {
                    if (getItemCount(wall) > RECOMMENDED_USER_POSITION) {
                        if (position == RECOMMENDED_USER_POSITION) {
                            swipeFlags = 0;
                        } else {
                            Post post = wall.posts.get(position > 15 ? position - 1 : position);
                            if (post.post_userid == AccountUtils.me(context).iduser) {
                                swipeFlags = ItemTouchHelper.LEFT;

                            } else {
                                swipeFlags = ItemTouchHelper.RIGHT;

                            }
                        }
                    } else {
                        if (position == wall.posts.size()) {
                            swipeFlags = 0;
                        }
                    }


                }

                return makeMovementFlags(0, swipeFlags);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                if (direction == ItemTouchHelper.LEFT) {
                    onSwipeDialogCallbackk.onDialogButtonClick(true, getPostPosition(wall, position), true);
                    /*new AlertDialog.Builder(context)
                            .setMessage("Paylaşımı silmek istediğinizden emin misiniz?")
                            .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    onSwipeDialogCallbackk.onDialogButtonClick(false, getPostPosition(wall,position),true);
                                }
                            }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            onSwipeDialogCallbackk.onDialogButtonClick(false, getPostPosition(wall,position),true);
                        }
                    })
                            .show();
*/
                } else if (direction == ItemTouchHelper.RIGHT) {
                    onSwipeDialogCallbackk.onDialogButtonClick(true, getPostPosition(wall, position), false);
                } else {

                }

                Logcat.d("----+ onSwiped");
            }


            @Override
            public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                Logcat.d("----+2 farklı"+actionState);
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {


                if(actionState == ItemTouchHelper.ACTION_STATE_DRAG){
                    swipeRefreshLayout.setEnabled(false);

                    Logcat.d("----+ ACTION_STATE_DRAG");
                }else if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
                    Logcat.d("----+ ACTION_STATE_SWIPE");
                    swipeRefreshLayout.setEnabled(false);
                }else if(actionState == ItemTouchHelper.ACTION_STATE_IDLE){
                    Logcat.d("----+ ACTION_STATE_IDLE");
                    swipeRefreshLayout.setEnabled(true);
                }else if(actionState == ItemTouchHelper.ANIMATION_TYPE_SWIPE_CANCEL){
                    Logcat.d("----+ ANIMATION_TYPE_SWIPE_CANCEL");
                    swipeRefreshLayout.setEnabled(true);
                }else if(actionState == ItemTouchHelper.ANIMATION_TYPE_SWIPE_SUCCESS){
                    Logcat.d("----+ ANIMATION_TYPE_SWIPE_SUCCESS");
                    swipeRefreshLayout.setEnabled(true);
                }else{
                    Logcat.d("----+ farklı"+actionState);
                }

                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    Paint p = new Paint();
                    if (dX > 0) {
                        p.setColor(Color.parseColor("#263892"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                        c.drawRect(background, p);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                        Paint paint = new Paint();
                        paint.setColor(Color.WHITE);
                        paint.setTextSize(AndroidUtils.convertDpToPixel(22, context));
                        c.drawText("MUUDY DE", icon_dest.left - 80, icon_dest.centerY() + 20, paint);
                    } else {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, p);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                        Paint paint = new Paint();
                        paint.setColor(Color.WHITE);
                        paint.setTextSize(AndroidUtils.convertDpToPixel(22, context));
                        c.drawText("SİL", icon_dest.left - 80, icon_dest.centerY() + 20, paint);

                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };


        itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void initSwipe(final RecyclerView recyclerView, final Context context, List<Post> posts, OnSwipeDialogCallback onSwipeDialogCallback) {
        setOnSwipeDialogCallback(onSwipeDialogCallback);
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public boolean isItemViewSwipeEnabled() {
                return isItemViewSwipeEnable;
            }

            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int position = viewHolder.getAdapterPosition();
                int swipeFlags = 0;
                if (!isItemViewSwipeEnable) {
                    swipeFlags = 0;
                } else {
                    Post post = posts.get(position);
                    if (post.post_userid == AccountUtils.me(context).iduser) {
                        swipeFlags = ItemTouchHelper.LEFT;
                    } else {
                        swipeFlags = ItemTouchHelper.RIGHT;
                    }

                }
                return makeMovementFlags(0, swipeFlags);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT) {
                    onSwipeDialogCallbackk.onDialogButtonClick(true, position, true);


                } else if (direction == ItemTouchHelper.RIGHT) {
                    onSwipeDialogCallbackk.onDialogButtonClick(true, position, false);
                } else {

                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    Paint p = new Paint();
                    if (dX > 0) {
                        p.setColor(Color.parseColor("#263892"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                        c.drawRect(background, p);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                        Paint paint = new Paint();
                        paint.setColor(Color.WHITE);
                        paint.setTextSize(AndroidUtils.convertDpToPixel(22, context));
                        c.drawText("MUUDY DE", icon_dest.left - 80, icon_dest.centerY() + 20, paint);
                    } else {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, p);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                        Paint paint = new Paint();
                        paint.setColor(Color.WHITE);
                        paint.setTextSize(AndroidUtils.convertDpToPixel(22, context));
                        c.drawText("SİL", icon_dest.left - 80, icon_dest.centerY() + 20, paint);

                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void detachFromRecyclerView(RecyclerView recyclerView) {
        if (itemTouchHelper != null) {
            itemTouchHelper.attachToRecyclerView(null);
            recyclerView.removeItemDecoration(itemTouchHelper);
        }
    }

    public void initSwipeForMessages(final RecyclerView recyclerView, final Context context, List<UserMessage> messages, OnSwipeDialogCallback onSwipeDialogCallback) {
        setOnSwipeDialogCallback(onSwipeDialogCallback);
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public boolean isItemViewSwipeEnabled() {

                return isItemViewSwipeEnable;
            }

            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags(0, ItemTouchHelper.LEFT);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT) {


                    new AlertDialog.Builder(context)
                            .setMessage(messages.get(position).otherUser.username + " kullanıcısıyla olan sohbetinizi silmek istediğinizden emin misiniz?")
                            .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    onSwipeDialogCallbackk.onDialogButtonClick(true, position, true);
                                }
                            })
                            .setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    onSwipeDialogCallbackk.onDialogButtonClick(false, position, true);
                                }
                            }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            onSwipeDialogCallbackk.onDialogButtonClick(false, position, true);
                        }
                    }).show();

                } else if (direction == ItemTouchHelper.RIGHT) {
                    onSwipeDialogCallbackk.onDialogButtonClick(true, position, false);
                } else {

                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    Paint p = new Paint();
                    if (dX > 0) {
                        p.setColor(Color.parseColor("#263892"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                        c.drawRect(background, p);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                        Paint paint = new Paint();
                        paint.setColor(Color.WHITE);
                        paint.setTextSize(AndroidUtils.convertDpToPixel(22, context));
                        c.drawText("MUUDY DE", icon_dest.left - 80, icon_dest.centerY() + 20, paint);
                    } else {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, p);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                        Paint paint = new Paint();
                        paint.setColor(Color.WHITE);
                        paint.setTextSize(AndroidUtils.convertDpToPixel(22, context));
                        c.drawText("SİL", icon_dest.left - 80, icon_dest.centerY() + 20, paint);

                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }


    public void initSwipeForPostDetail(final RecyclerView recyclerView, final Context context, PostDetail postDetail, OnSwipeDialogCallback onSwipeDialogCallback) {
        setOnSwipeDialogCallback(onSwipeDialogCallback);
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public boolean isItemViewSwipeEnabled() {

                return isItemViewSwipeEnable;
            }

            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

                int position = viewHolder.getAdapterPosition();
                int swipeFlags = 0;
                int notErasableItemCount = 1;
                if(postDetail.post.rozetler.size() > 0){
                    notErasableItemCount += postDetail.post.rozetler.size();
                }

                if (position >= postDetail.post.rozetler.size() + 1){
                    if (postDetail.comments.get(position - postDetail.post.rozetler.size() - 1).commenterUser.iduser == AccountUtils.me(context).iduser) {
                        swipeFlags = ItemTouchHelper.LEFT;
                    }else{
                        swipeFlags = 0;
                    }
                }else{
                    swipeFlags = 0;
                }
                return makeMovementFlags(0, swipeFlags);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT) {

                    new AlertDialog.Builder(context)
                            .setMessage(postDetail.comments.get(position - postDetail.post.rozetler.size() - 1).postcomment_text+ " Yorumunuzu silmek istediğinizden emin misiniz?")
                            .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    onSwipeDialogCallbackk.onDialogButtonClick(true, position - postDetail.post.rozetler.size() - 1, true);
                                }
                            })
                            .setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    onSwipeDialogCallbackk.onDialogButtonClick(false, position - postDetail.post.rozetler.size() - 1, true);
                                }
                            }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            onSwipeDialogCallbackk.onDialogButtonClick(false, position - postDetail.post.rozetler.size() - 1, true);
                        }
                    }).show();

                } else if (direction == ItemTouchHelper.RIGHT) {
                    onSwipeDialogCallbackk.onDialogButtonClick(true, position - postDetail.post.rozetler.size() - 1, false);
                } else {

                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    Paint p = new Paint();
                    if (dX > 0) {
                        p.setColor(Color.parseColor("#263892"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                        c.drawRect(background, p);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                        Paint paint = new Paint();
                        paint.setColor(Color.WHITE);
                        paint.setTextSize(AndroidUtils.convertDpToPixel(22, context));
                        c.drawText("MUUDY DE", icon_dest.left - 80, icon_dest.centerY() + 20, paint);
                    } else {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, p);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                        Paint paint = new Paint();
                        paint.setColor(Color.WHITE);
                        paint.setTextSize(AndroidUtils.convertDpToPixel(22, context));
                        c.drawText("SİL", icon_dest.left - 80, icon_dest.centerY() + 20, paint);

                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

}
