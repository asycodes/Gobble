package com.sutd.t4app.ui.home;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.sutd.t4app.R;
import com.sutd.t4app.data.model.TikTok;

import java.util.List;

public class TikTokAdapter extends RecyclerView.Adapter<TikTokAdapter.ViewHolder> {

    private List<TikTok> tikTokList;
    private Activity context;
    private int layoutID;

    public TikTokAdapter(List<TikTok> tikTokList,int layoutID) {
        this.layoutID=layoutID;
        this.tikTokList = tikTokList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(this.layoutID, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TikTok tikTok = tikTokList.get(position);
        holder.bind(tikTok);

    }

    @Override
    public int getItemCount() {
        return tikTokList != null ? tikTokList.size() : 0;
    }

    public void updateDataTikTok(List<TikTok> newTiktoks){
        tikTokList.clear();
        tikTokList.addAll(newTiktoks);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewNameTikTok;


        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewTikTok);
            textViewNameTikTok= itemView.findViewById(R.id.textViewNameTiktok);
        }

        void bind(TikTok tikTok) {
            Picasso.get()
                    .load(tikTok.getImg())
                    .placeholder(R.drawable.food2t4app)  // Placeholder image while loading
                    .into(imageView);
            itemView.setOnClickListener(v -> {
                openLinkInCustomTab(tikTok.getLink(), itemView.getContext());
            });
            textViewNameTikTok.setText(tikTok.getNameTikTok());
        }

        private void openLinkInCustomTab(String url, Context context) {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(context, Uri.parse(url));
        }
    }
}
