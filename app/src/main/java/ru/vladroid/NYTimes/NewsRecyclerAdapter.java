package ru.vladroid.NYTimes;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import ru.vladroid.NYTimes.DTO.Result;

public class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsRecyclerAdapter.ViewHolder>{
    private final List<Result> news;
    private final LayoutInflater layoutInflater;
    private RequestManager imageLoader;
    private final OnItemClickListener listener;

    static DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    public NewsRecyclerAdapter(Context context, List<Result> news, OnItemClickListener listener) {
        layoutInflater = LayoutInflater.from(context);
        this.news = news;
        imageLoader = Glide.with(context);
        this.listener = listener;
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
        void onItemClick(Result newsItem);
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

        private void bind(Result newsItem) {

            newsCat.setText(newsItem.getSubsection().equals("") ? newsItem.getSection() : newsItem.getSubsection());
            newsTitle.setText(newsItem.getTitle());
            newsText.setText(newsItem.getAbstract());

            Date date = null;
            try {
                date = df.parse(newsItem.getPublishedDate());
            } catch (ParseException e) {
                Log.e("mydaterr", newsItem.getPublishedDate());
            }

            newsDate.setText(dateToString(date));
            if (newsItem.getMultimedia().size() != 0 && newsItem.getMultimedia().get(0).getType().equals("image"))
                imageLoader.load(newsItem.getMultimedia().get(1).getUrl()).into(newsIcon);
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
        if (date == null)
            return "error date parse";
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
        String fMinutes = minutes >= 10 ? String.valueOf(minutes) : "0" + String.valueOf(minutes);


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
