package com.taofeek.citaa;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OnboardingAdapter extends RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder> {
    private List<OnboardingItem> onboardingItems;

    public OnboardingAdapter(List<OnboardingItem> onboardingItems) {
        this.onboardingItems = onboardingItems;
    }

    @NonNull
    @Override
    public OnboardingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OnboardingViewHolder(
                // Inflate the layout for the activity
                LayoutInflater.from(parent.getContext()).inflate(R.layout.onboarding_items_container,
                        parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull OnboardingViewHolder holder, int position) {
        holder.setOnboardingData(onboardingItems.get(position));

    }

    @Override
    public int getItemCount() {
        return onboardingItems.size();
    }

    class OnboardingViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageOnBoarding;
        private TextView textHead;
        private TextView textDescription;
        private TextView textStory;

        public OnboardingViewHolder(@NonNull View itemView) {
            super(itemView);
            textHead = itemView.findViewById(R.id.textHead);
            textDescription = itemView.findViewById(R.id.textDescription);
            textStory = itemView.findViewById(R.id.textStory);
            imageOnBoarding = itemView.findViewById(R.id.imageOnBoarding);
        }
        void setOnboardingData(OnboardingItem onboardingItem){
            textHead.setText(onboardingItem.getTitle());
            textDescription.setText(onboardingItem.getDescription());
            textStory.setText(onboardingItem.getExplanation());
            imageOnBoarding.setImageResource(onboardingItem.getImage());

        }
    }
}
