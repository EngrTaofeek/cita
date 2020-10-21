package com.taofeek.cita;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class OnboardingScreen extends AppCompatActivity {
    private OnboardingAdapter onboardingAdapter;
    private Button button;
    private ImageView imageView;
    private TextView textView;
    private ViewPager2 viewPager2;
    private LinearLayout layoutOnboardingIndicators;
    private LinearLayout skipLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding_screen);
        button = findViewById(R.id.next_button);
        layoutOnboardingIndicators = findViewById(R.id.layoutOnboardingIndicators);
        textView = findViewById(R.id.skip);

        // Skip text to open another activity when clicked
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OnboardingScreen.this, MainActivity.class);
                startActivity(intent);
            }
        });
        setUpOnboardingItem();
        setUpOnboardingIndicators();
        setCurrentOnboardingIndicators(0);

         final ViewPager2 onboardingViewPager = findViewById(R.id.onboardingViewPager);
        onboardingViewPager.setAdapter(onboardingAdapter);

        onboardingViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentOnboardingIndicators(position);
            }
        });
        // button to move the slides
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onboardingViewPager.getCurrentItem() + 1 < onboardingAdapter.getItemCount()){
                    onboardingViewPager.setCurrentItem(onboardingViewPager.getCurrentItem() + 1);
                } else {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
            }
        });
    }

    // Onboarding data to be displayed in each slide
    private void setUpOnboardingItem() {
        List<OnboardingItem> onboardingItems = new ArrayList<>();

        // first onboard screen details
        OnboardingItem firstOnboard = new OnboardingItem();
        firstOnboard.setTitle("Create Events");
        firstOnboard.setDescription("Cita helps you to create your events for");
        firstOnboard.setExplanation("users to book them.");
        firstOnboard.setImage(R.drawable.slide1);

        // second onboard screen details
        OnboardingItem secondOnboard = new OnboardingItem();
        secondOnboard.setTitle("Manage Bookings");
        secondOnboard.setDescription("You can make bookings of your desired");
        secondOnboard.setExplanation("events anytime, anywhere.");
        secondOnboard.setImage(R.drawable.slide2);

        // third onboard screen details
        OnboardingItem thirdOnboard = new OnboardingItem();
        thirdOnboard.setTitle("Ease of use");
        thirdOnboard.setDescription("With cita, creating events and booking events");
        thirdOnboard.setExplanation("are made at ease at your comfort zone.");
        thirdOnboard.setImage(R.drawable.slide3);

        onboardingItems.add(firstOnboard);
        onboardingItems.add(secondOnboard);
        onboardingItems.add(thirdOnboard);

        onboardingAdapter = new OnboardingAdapter(onboardingItems);
    }

    private void setUpOnboardingIndicators() {
        ImageView[] indicators = new ImageView[onboardingAdapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(8, 0, 8, 0);
        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                    R.drawable.onboarding_inactive));
            indicators[i].setLayoutParams(layoutParams);
            layoutOnboardingIndicators.addView(indicators[i]);

        }
    }

    private void setCurrentOnboardingIndicators(int index) {
        int childCount = layoutOnboardingIndicators.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) layoutOnboardingIndicators.getChildAt(i);
            if (i == index) {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.onboarding_active)
                );
            } else {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.onboarding_inactive)
                );
            }
        }


    }
}
