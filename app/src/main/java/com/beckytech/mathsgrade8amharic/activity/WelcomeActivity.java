package com.beckytech.mathsgrade8amharic.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.beckytech.mathsgrade8amharic.MainActivity;
import com.beckytech.mathsgrade8amharic.R;

public class WelcomeActivity extends AppCompatActivity {

    private ViewPager2 viewPager2;
    private Button btnNext, btnSkip;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        prefs = getSharedPreferences("welcome", MODE_PRIVATE);
        if (prefs.getBoolean("rated", false)) {
            startMainActivity();
            return;
        }

        viewPager2 = findViewById(R.id.viewPager_welcome);
        btnNext = findViewById(R.id.btn_next);
        btnSkip = findViewById(R.id.btn_skip);

        WelcomeAdapter adapter = new WelcomeAdapter();
        viewPager2.setAdapter(adapter);

        btnNext.setOnClickListener(v -> {
            int current = viewPager2.getCurrentItem() + 1;
            if (current < 5) {
                viewPager2.setCurrentItem(current);
            } else {
                startMainActivity();
            }
        });

        btnSkip.setOnClickListener(v -> startMainActivity());

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (position == 4) {
                    btnNext.setText("GOT IT");
                    btnSkip.setVisibility(View.GONE);
                } else {
                    btnNext.setText("NEXT");
                    btnSkip.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void startMainActivity() {
        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
        finish();
    }

    private class WelcomeAdapter extends RecyclerView.Adapter<WelcomeAdapter.ViewHolder> {

        private final String[] titles = {
                getString(R.string.rate_1_star),
                getString(R.string.rate_2_star),
                getString(R.string.rate_3_star),
                getString(R.string.rate_4_star),
                getString(R.string.rate_5_star)
        };

        private final String[] descs = {
                "1 Star means our service was bad.",
                "2 Stars means our service was not good.",
                "3 Stars means our service was somewhat good.",
                "4 Stars means our service was very good.",
                "5 Stars means our service was excellent! Please support us."
        };

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.welcome_slide, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.title.setText(titles[position]);
            holder.desc.setText(descs[position]);
            if (position == 4) {
                holder.btnRate.setVisibility(View.VISIBLE);
                holder.btnRate.setOnClickListener(v -> {
                    prefs.edit().putBoolean("rated", true).apply();
                    String pkg = getPackageName();
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + pkg)));
                });
            } else {
                holder.btnRate.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return 5;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView title, desc;
            ImageView image;
            Button btnRate;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.slide_title);
                desc = itemView.findViewById(R.id.slide_desc);
                image = itemView.findViewById(R.id.slide_image);
                btnRate = itemView.findViewById(R.id.btn_rate_now);
            }
        }
    }
}
