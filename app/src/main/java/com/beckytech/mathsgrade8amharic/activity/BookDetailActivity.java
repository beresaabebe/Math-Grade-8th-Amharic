package com.beckytech.mathsgrade8amharic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.beckytech.mathsgrade8amharic.R;
import com.beckytech.mathsgrade8amharic.model.Model;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.List;

public class BookDetailActivity extends AppCompatActivity {
    com.facebook.ads.InterstitialAd interstitialAd;
    private final String TAG = BookDetailActivity.class.getSimpleName();
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        callAds();

        MobileAds.initialize(this, initializationStatus -> {
        });
        //get the reference to your FrameLayout
        FrameLayout adContainerView = findViewById(R.id.adView_container);
        //Create an AdView and put it into your FrameLayout
        adView = new AdView(this);
        adContainerView.addView(adView);
        adView.setAdUnitId("ca-app-pub-8504401574247581/4981219237");
//        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");


        //start requesting banner ads
        loadBanner();

        findViewById(R.id.back_book_detail).setOnClickListener(v -> onBackPressed());

        Intent intent = getIntent();
        Model model = (Model) intent.getSerializableExtra("data");

        TextView title = findViewById(R.id.title_book_detail);
        title.setSelected(true);
        title.setText(model.getTitle());

        TextView subTitle = findViewById(R.id.sub_title_book_detail);
        subTitle.setSelected(true);
        subTitle.setText(model.getSubTitle());

        PDFView pdfView = findViewById(R.id.pdfView);

        int start = model.getStartPage();
        int end = model.getEndPage();

        List<Integer> list = new ArrayList<>();

        for (int i = start; i <= end; i++) {
            list.add(i);
        }

        int[] array = new int[list.size()];

        for (int j = 1; j < array.length; j++) {
            array[j] = list.get(j);
        }

        pdfView.fromAsset("mat8.pdf")
                .pages(array)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .spacing(10)
                .enableDoubletap(true)
                .fitEachPage(true)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
    }
    private AdSize getAdSize() {
        //Determine the screen width to use for the ad width.
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        //you can also pass your selected width here in dp
        int adWidth = (int) (widthPixels / density);

        //return the optimal size depends on your orientation (landscape or portrait)
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }

    private void loadBanner() {
        AdRequest adRequest = new AdRequest.Builder()
                .build();

        AdSize adSize = getAdSize();
        // Set the adaptive ad size to the ad view.
        adView.setAdSize(adSize);

        // Start loading the ad in the background.
        adView.loadAd(adRequest);
    }
    private void callAds() {
        AudienceNetworkAds.initialize(this);

        interstitialAd = new InterstitialAd(this, "587359836775376_587369080107785");
        // Create listeners for the Interstitial Ad
        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
                Log.e(TAG, "Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
                Log.e(TAG, "Interstitial ad dismissed.");
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                // Show the ad
                interstitialAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
                Log.d(TAG, "Interstitial ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
                Log.d(TAG, "Interstitial ad impression logged!");
            }
        };

        // For auto play video ads, it's recommended to load the ad
        // at least 30 seconds before it is shown
        interstitialAd.loadAd(
                interstitialAd.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener)
                        .build());
    }

}