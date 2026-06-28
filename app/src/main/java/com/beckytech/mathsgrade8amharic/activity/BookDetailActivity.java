package com.beckytech.mathsgrade8amharic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.beckytech.mathsgrade8amharic.AdManager;
import com.beckytech.mathsgrade8amharic.R;
import com.beckytech.mathsgrade8amharic.adapter.ChapterPagerAdapter;
import com.beckytech.mathsgrade8amharic.contents.SubTitleContents;
import com.beckytech.mathsgrade8amharic.contents.TitleContents;
import com.beckytech.mathsgrade8amharic.model.Model;

public class BookDetailActivity extends AppCompatActivity {
    private ViewPager2 viewPager2;
    private TextView subTitle;
    private TextView title;
    private AdManager adManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        adManager = new AdManager();
        adManager.loadCollapsibleBanner(this, findViewById(R.id.collapsible_ad_container));
        adManager.loadRewarded(this);
        adManager.loadRewardedInterstitial(this);

        findViewById(R.id.back_book_detail).setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        Intent intent = getIntent();
        Model model = (Model) intent.getSerializableExtra("data");

        title = findViewById(R.id.title_book_detail);
        title.setSelected(true);

        subTitle = findViewById(R.id.sub_title_book_detail);
        subTitle.setSelected(true);

        viewPager2 = findViewById(R.id.viewPager2_book_detail);
        ChapterPagerAdapter adapter = new ChapterPagerAdapter(this);
        viewPager2.setAdapter(adapter);

        if (model != null) {
            int index = getIndex(model.getTitle());
            if (index != -1) {
                viewPager2.setCurrentItem(index, false);
                updateTitle(index);
            }
        }

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateTitle(position);
                adManager.showRandomRewarded(BookDetailActivity.this, null);
            }
        });
    }

    private void updateTitle(int index) {
        title.setText(TitleContents.title[index]);
        subTitle.setText(SubTitleContents.subTitle[index]);
    }

    private int getIndex(String title) {
        for (int i = 0; i < TitleContents.title.length; i++) {
            if (TitleContents.title[i].equalsIgnoreCase(title)) return i;
        }
        return -1;
    }
}
