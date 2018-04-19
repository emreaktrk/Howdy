package istanbul.codify.muudy.ui.places;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import io.reactivex.subjects.PublishSubject;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.model.Place;

import java.util.ArrayList;
import java.util.List;

final class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.Holder> {

    private final PublishSubject<Place> mPublish = PublishSubject.create();
    private List<Place> mList;

    PlacesAdapter(@Nullable List<Place> list) {
        mList = list == null ? new ArrayList<>() : list;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cell = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_places, parent, false);
        return new Holder(cell);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Place place = mList.get(position);

        holder.mRange.setText(place.userDistance + " m");
        holder.mName.setText(place.place_name);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    PublishSubject<Place> itemClicks() {
        return mPublish;
    }

    void notifyDataSetChanged(List<Place> list) {
        mList = list;
        notifyDataSetChanged();
    }


    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private AppCompatTextView mRange;
        private AppCompatTextView mName;

        Holder(View itemView) {
            super(itemView);

            mRange = itemView.findViewById(R.id.places_range);
            mName = itemView.findViewById(R.id.places_name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Place place = mList.get(getAdapterPosition());
            mPublish.onNext(place);
        }
    }
}
