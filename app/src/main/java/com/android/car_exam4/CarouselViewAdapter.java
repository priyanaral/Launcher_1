package com.android.car_exam4;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CarouselViewAdapter extends RecyclerView.Adapter<CarouselViewAdapter.ViewHolder> {

    private List<AppInfo> appList;
    private OnItemClickListener itemClickListener;

    public CarouselViewAdapter(List<AppInfo> appList) {
        this.appList = appList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carousel, parent, false);

        // Inflate the layout for the individual pages
        //View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_page, parent, false);
        // Make sure to set the layout params with match_parent for both width and height
        view.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AppInfo appInfo = appList.get(position);
        holder.appIconImageView.setImageDrawable(appInfo.getIcon());
        holder.appNameTextView.setText(appInfo.getAppName());
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView appIconImageView;
        private TextView appNameTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            appIconImageView = itemView.findViewById(R.id.appIconImageView);
            appNameTextView = itemView.findViewById(R.id.appNameTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            itemClickListener.onItemClick(v, position);
                        }
                    }
                }
            });
        }
    }
}
