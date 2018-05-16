package istanbul.codify.muudy.ui.statistic.event;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.blankj.utilcode.util.SizeUtils;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.subjects.PublishSubject;
import istanbul.codify.muudy.BuildConfig;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.model.ActivityStat;

import java.util.ArrayList;
import java.util.List;

final class ActivityStatsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<ActivityStat> mList;
    private final PublishSubject<ActivityStat> mPublish = PublishSubject.create();
    private List<ActivityStat> mFiltered;

    ActivityStatsAdapter(@Nullable List<ActivityStat> list) {
        mList = list == null ? new ArrayList<>() : list;
        mFiltered = new ArrayList<>(mList);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case ChartHolder.TYPE:
                return new ChartHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_statistic_event_chart, parent, false));
            case StatsHolder.TYPE:
                return new StatsHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_activity_stat, parent, false));
            default:
                throw new IllegalArgumentException("Not implemented");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ChartHolder) {
            ChartHolder chart = (ChartHolder) holder;

            List<PieEntry> entries = new ArrayList<>();
            int postCount = 0;
            for (ActivityStat stat : mFiltered) {
                postCount += stat.postCount;
                entries.add(new PieEntry(stat.percent, stat.post_emoji_word));
            }

            chart.mTextView.setText(postCount + " Paylaşım");

            PieDataSet set = new PieDataSet(entries, "");
            set.setColors(ColorTemplate.PASTEL_COLORS);
            set.setSliceSpace(2);
            set.setValueTextSize(0f);

            PieData data = new PieData(set);
            data.setValueFormatter(new PercentFormatter());
            data.setValueTextSize(9);
            data.setValueTextColor(Color.WHITE);

            Description description = new Description();
            description.setText("");
            chart.mChart.setDescription(description);

            chart.mChart.getLegend().setEnabled(true);
            chart.mChart.setUsePercentValues(true);

            chart.mChart.setData(data);
            chart.mChart.invalidate();

            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setCornerRadius(SizeUtils.dp2px(5));
            gradientDrawable.setStroke(SizeUtils.dp2px(1), holder.itemView.getContext().getResources().getColor(R.color.blue));
            gradientDrawable.setColor(Color.WHITE);
            chart.mButton.setBackgroundDrawable(gradientDrawable);

        } else if (holder instanceof StatsHolder) {
            StatsHolder stats = (StatsHolder) holder;

            ActivityStat stat = mFiltered.get(position - 1);

            Picasso
                    .with(holder.itemView.getContext())
                    .load(BuildConfig.URL + stat.post_emoji)
                    .into(stats.mImage);
            stats.mText.setText(stat.post_emoji_word);
            stats.mPercent.setText(String.format("%.02f", stat.percent) + "%");
        }
    }

    @Override
    public int getItemCount() {
        return mFiltered.size() + 1;
    }

    PublishSubject<ActivityStat> itemClicks() {
        return mPublish;
    }

    List<ActivityStat> getWords() {
        return mList;
    }

    void setFiltered(@Nullable List<ActivityStat> filtered) {
        this.mFiltered = filtered == null ? new ArrayList<>(mList) : filtered;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ChartHolder.TYPE;
        } else {
            return StatsHolder.TYPE;
        }
    }

    class StatsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private static final int TYPE = 469;

        private CircleImageView mImage;
        private AppCompatTextView mText;
        private AppCompatTextView mPercent;

        StatsHolder(View itemView) {
            super(itemView);

            mImage = itemView.findViewById(R.id.activity_stat_image);
            mText = itemView.findViewById(R.id.activity_stat_text);
            mPercent = itemView.findViewById(R.id.activity_stat_percent);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            ActivityStat stat = mFiltered.get(getAdapterPosition() - 1);
            mPublish.onNext(stat);
        }
    }

    class ChartHolder extends RecyclerView.ViewHolder {

        private static final int TYPE = 270;

        private PieChart mChart;
        private AppCompatButton mButton;
        private AppCompatTextView mTextView;

        ChartHolder(View itemView) {
            super(itemView);

            mChart = itemView.findViewById(R.id.statistic_event_chart);
            mButton = itemView.findViewById(R.id.statistic_event_see_all_post);
            mTextView = itemView.findViewById(R.id.statistic_event_post_count);

        }

    }
}
