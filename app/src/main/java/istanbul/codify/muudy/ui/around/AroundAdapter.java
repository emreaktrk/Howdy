package istanbul.codify.muudy.ui.around;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import io.reactivex.subjects.PublishSubject;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.model.AroundUsers;
import istanbul.codify.muudy.model.User;
import istanbul.codify.muudy.view.AvatarView;

import java.util.ArrayList;
import java.util.List;

final class AroundAdapter extends RecyclerView.Adapter<AroundAdapter.Holder> {

    private PublishSubject<List<User>> mMoreSubject = PublishSubject.create();
    private PublishSubject<User> mUserSubject = PublishSubject.create();
    private List<AroundUsers> mList;

    AroundAdapter(List<AroundUsers> list) {
        mList = list == null ? new ArrayList<>() : list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View cell = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_around, parent, false);
        return new Holder(cell);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        AroundUsers around = mList.get(position);

        holder.mTitle.setText(around.title);
        for (int i = 0; i < holder.mContainer.getChildCount(); i++) {
            AvatarView avatar = (AvatarView) holder.mContainer.getChildAt(i);
            if (around.users.size() < i) {
                avatar.setVisibility(View.GONE);
            } else {
                avatar.setVisibility(View.VISIBLE);

                User user = around.users.get(i);
                avatar.setUser(user);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    PublishSubject<List<User>> moreClicks() {
        return mMoreSubject;
    }

    PublishSubject<User> userClicks() {
        return mUserSubject;
    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private LinearLayoutCompat mContainer;
        private AppCompatTextView mTitle;
        private AppCompatTextView mMore;

        Holder(View itemView) {
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
            AroundUsers around = mList.get(getAdapterPosition());

            if (view instanceof AvatarView) {
                int position = (int) view.getTag();
                User user = around.users.get(position);
                mUserSubject.onNext(user);
            } else {
                mMoreSubject.onNext(around.users);
            }
        }
    }
}
