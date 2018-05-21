package istanbul.codify.muudy.ui.compose;

import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.squareup.picasso.Picasso;
import io.reactivex.subjects.PublishSubject;
import istanbul.codify.muudy.BuildConfig;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.model.Activity;

import java.util.ArrayList;
import java.util.List;

public final class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.Holder> {

    private final PublishSubject<Activity> mPublish = PublishSubject.create();
    private List<Activity> mList;
    private Activity mSelected;

    public ActivityAdapter(@Nullable List<Activity> list, @Nullable Activity selected) {
        mList = list == null ? new ArrayList<>() : list;
        mSelected = selected;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cell = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_activity, parent, false);
        return new Holder(cell);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Activity activity = mList.get(position);
        holder.mText.setText(activity.activities_title);
        Picasso
                .with(holder.mImage.getContext())
                .load(BuildConfig.URL + activity.activities_icon_url)
                .into(holder.mImage);
        holder.itemView.setSelected(mSelected != null && mSelected.equals(activity));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public PublishSubject<Activity> itemClicks() {
        return mPublish;
    }

    public @Nullable
    Activity getSelected() {
        return mSelected;
    }

    public void setSelected(@Nullable Activity selected) {
        mSelected = selected;
    }

    public void notifyDataSetChanged(List<Activity> list) {
        mList = list;
        notifyDataSetChanged();
    }


    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private AppCompatImageView mImage;
        private AppCompatTextView mText;

        Holder(View itemView) {
            super(itemView);

            mText = itemView.findViewById(R.id.activity_text);
            mImage = itemView.findViewById(R.id.activity_image);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Activity activity = mList.get(getAdapterPosition());
            mPublish.onNext(activity);
        }
    }
}
