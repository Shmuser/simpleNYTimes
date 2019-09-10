package ru.vladroid.NYTimes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsRecyclerAdapter.ViewHolder>{
    private final List<NewsItem> news;
    private final LayoutInflater layoutInflater;
    private static RequestManager imageLoader;
    private final OnItemClickListener listener;

    public NewsRecyclerAdapter(Context context, List<NewsItem> news, OnItemClickListener listener) {
        layoutInflater = LayoutInflater.from(context);
        this.news = news;
        imageLoader = Glide.with(context);
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if (news.get(position).getCategory().getId() == 2)
            return 1;
        return 0;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if (viewType == 1)
            v = layoutInflater.inflate(R.layout.news_view2, parent, false);
        else
            v = layoutInflater.inflate(R.layout.news_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(news.get(position));

    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    public interface OnItemClickListener {
        void onItemClick(NewsItem newsItem);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView newsIcon;
        private final TextView newsCat, newsTitle, newsText, newsDate;

        private ViewHolder(View itemView) {
            super(itemView);
            newsIcon = itemView.findViewById(R.id.news_image);
            newsCat = itemView.findViewById(R.id.news_category);
            newsText = itemView.findViewById(R.id.news_text);
            newsDate = itemView.findViewById(R.id.news_date);
            newsTitle = itemView.findViewById(R.id.news_title);
        }

        private void bind(NewsItem newsItem) {
            newsCat.setText(newsItem.getCategory().getName());
            newsTitle.setText(newsItem.getTitle());
            newsText.setText(newsItem.getPreviewText());
            newsDate.setText(dateToString(newsItem.getPublishDate()));
            imageLoader.load(newsItem.getImageUrl()).into(newsIcon);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (listener != null && pos != RecyclerView.NO_POSITION) {
                        listener.onItemClick(news.get(pos));
                    }
                }
            });
        }
    }

    static String dateToString(Date date) {
        StringBuilder res = new StringBuilder();
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR);
        int minutes = cal.get(Calendar.MINUTE);
        String fMonth = month >= 10 ? String.valueOf(month) : "0" + String.valueOf(month);
        String fDay = day >= 10 ? String.valueOf(day) : "0" + String.valueOf(day);
        String fHour = hour >= 10 ? String.valueOf(hour) : "0" + String.valueOf(hour);
        String fMinutes = day >= 10 ? String.valueOf(minutes) : "0" + String.valueOf(minutes);


        Calendar calNow = Calendar.getInstance();
        if (calNow.get(Calendar.YEAR) == year && calNow.get(Calendar.MONTH) == month) {
            if (calNow.get(Calendar.DAY_OF_MONTH) - day > 1) {
                res.append(fDay).append(".").append(fMonth).append(".").append(year).append(", ").append(fHour).append(":").append(fMinutes);
            } else if (calNow.get(Calendar.DAY_OF_MONTH) - day == 1) {
                res.append("Yesterday, ").append(fHour).append(":").append(fMinutes);
            } else {
                res.append("Today, ");
                if (calNow.get(Calendar.HOUR) - hour < 13) {
                    if (calNow.get(Calendar.HOUR) - hour == 0) {
                        res.append(calNow.get(Calendar.MINUTE) - minutes).append(" min. ago");
                    } else {
                        res.append(calNow.get(Calendar.HOUR) - hour).append(" hr. ago");
                    }
                } else {
                    res.append(fHour).append(":").append(fMinutes);
                }
            }
        } else {
            res.append(fDay).append(".").append(fMonth).append(".").append(year).append(", ").append(fHour).append(":").append(fMinutes);
        }

        return res.toString();
    }

}
