package com.example.chatapp.OnBoardingActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;

import java.util.ArrayList;
import java.util.List;

public class onBoardingAdapter extends RecyclerView.Adapter<onBoardingAdapter.OnBoardingViewHolder> {

   Context Context;
    List<onBoardingItem> onBoardingItems = new ArrayList<>();

    public onBoardingAdapter(Context onBoardingActivity, List<onBoardingItem> onBoardingItemsList) {
        this.onBoardingItems=onBoardingItemsList;
        this.Context=onBoardingActivity;
    }

    @NonNull
    @Override
    public OnBoardingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OnBoardingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_onboardin, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OnBoardingViewHolder holder, int position) {

       holder.setonBoardingAllData(onBoardingItems.get(position));

    }

    @Override
    public int getItemCount() {
        return onBoardingItems.size();
    }

    class OnBoardingViewHolder extends RecyclerView.ViewHolder {

        private TextView textTitle;
        private TextView textDescription;
        private ImageView imageOnBoarding;

        public OnBoardingViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.texttitle);
            textDescription = itemView.findViewById(R.id.textDescription);
            imageOnBoarding = itemView.findViewById(R.id.imageonboardin);
        }
        void setonBoardingAllData(onBoardingItem onBoardingItem)
        {
            textTitle.setTextAppearance(Context,R.style.fontForNotificationLandingPage);
            textTitle.setText(onBoardingItem.getTitle());
            textDescription.setText(onBoardingItem.getDescription());
            imageOnBoarding.setImageResource(onBoardingItem.getImage());
        }
    }
}
